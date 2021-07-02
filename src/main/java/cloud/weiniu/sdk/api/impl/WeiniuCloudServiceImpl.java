package cloud.weiniu.sdk.api.impl;

import cloud.weiniu.sdk.api.AuthService;
import cloud.weiniu.sdk.api.WeiniuCloudService;
import cloud.weiniu.sdk.bean.AccessToken;
import cloud.weiniu.sdk.config.ConfigStorage;
import cloud.weiniu.sdk.error.WNError;
import cloud.weiniu.sdk.error.WNErrorException;
import cloud.weiniu.sdk.http.HttpType;
import cloud.weiniu.sdk.http.RequestExecutor;
import cloud.weiniu.sdk.http.RequestHttp;
import cloud.weiniu.sdk.http.SimpleGetRequestExecutor;
import cloud.weiniu.sdk.http.SimplePostRequestExecutor;
import cloud.weiniu.sdk.http.apache.ApacheHttpClientBuilder;
import cloud.weiniu.sdk.http.apache.DefaultApacheHttpClientBuilder;
import cloud.weiniu.sdk.json.WNGsonBuilder;
import cloud.weiniu.sdk.util.ApiUrl;
import cloud.weiniu.sdk.util.R;
import cloud.weiniu.sdk.util.crypto.SHA1;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.locks.Lock;

import static cloud.weiniu.sdk.http.HttpType.APACHE_HTTP;
import static cloud.weiniu.sdk.util.ApiPathConsts.Other.GET_ACCESS_TOKEN_URL;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
@Slf4j
public class WeiniuCloudServiceImpl implements WeiniuCloudService, RequestHttp<CloseableHttpClient, HttpHost> {

    private CloseableHttpClient httpClient;

    protected ConfigStorage configStorage;
    protected AuthService authService = new AuthService(this);

    private int retrySleepMillis = 1000;
    private int maxRetryTimes = 5;

    @Override
    public void setConfigStorage(ConfigStorage configStorage) {
        this.configStorage = configStorage;
        initHttp();
    }


    @Override
    public ConfigStorage getConfigStorage() {
        return configStorage;
    }


    @Override
    public CloseableHttpClient getRequestHttpClient() {
        return httpClient;
    }


    @Override
    public HttpType getRequestType() {
        return APACHE_HTTP;
    }


    @Override
    public String getAccessToken() throws WNErrorException {
        return getAccessToken(false);
    }

    @Override
    public void initHttp() {
        ConfigStorage configStorage = this.getConfigStorage();
        ApacheHttpClientBuilder apacheHttpClientBuilder = configStorage.getApacheHttpClientBuilder();
        if (null == apacheHttpClientBuilder) {
            apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
        }

        this.httpClient = apacheHttpClientBuilder.build();

    }

    @Override
    public String getAccessToken(boolean forceRefresh) throws WNErrorException {
        final ConfigStorage config = this.getConfigStorage();

        if (!config.isAccessTokenExpired() && !forceRefresh) {
            return config.getAccessToken();
        }
        Lock lock = config.getAccessTokenLock();
        lock.lock();
        try {
            // 拿到锁之后，再次判断一下最新的token是否过期，避免重刷
            if (!config.isAccessTokenExpired() && !forceRefresh) {
                return config.getAccessToken();
            }
            String url = String.format(GET_ACCESS_TOKEN_URL.getUrl(config),
                    this.configStorage.getAppId(), this.configStorage.getAppSecret());
            try {
                HttpGet httpGet = new HttpGet(url);
                try (CloseableHttpResponse response = getRequestHttpClient().execute(httpGet)) {
                    return this.extractAccessToken(new BasicResponseHandler().handleResponse(response));
                } finally {
                    httpGet.releaseConnection();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            lock.unlock();
        }
    }

    protected String extractAccessToken(String resultContent) throws WNErrorException {
        ConfigStorage config = this.getConfigStorage();
        WNError error = WNError.fromJson(resultContent);
        if (error.getErrorCode() != 0) {
            throw new WNErrorException(error);
        }
        AccessToken accessToken = AccessToken.fromJson(resultContent);
        config.updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
        return config.getAccessToken();
    }


    @Override
    public boolean checkSignature(String timestamp, String nonce, String signature) {
        try {
            return SHA1.gen(this.configStorage.getToken(), timestamp, nonce)
                    .equals(signature);
        } catch (Exception e) {
            log.error("Checking signature failed, and the reason is :" + e.getMessage());
            return false;
        }
    }


    @Override
    public <T> R<T> get(String url, String queryParam) throws WNErrorException {
        return execute(SimpleGetRequestExecutor.create(this), url, queryParam);
    }

    @Override
    public <T> R<T> get(ApiUrl url, String queryParam) throws WNErrorException {
        return this.get(url.getUrl(this.getConfigStorage()), queryParam);
    }

    @Override
    public <T> R<T> post(String url, String postData) throws WNErrorException {
        return execute(SimplePostRequestExecutor.create(this), url, postData);
    }

    @Override
    public <T> R<T> post(ApiUrl url, String postData) throws WNErrorException {
        return this.post(url.getUrl(this.getConfigStorage()), postData);
    }

    @Override
    public <T> R<T> post(String url, Object obj) throws WNErrorException {
        return this.execute(SimplePostRequestExecutor.create(this), url, WNGsonBuilder.create().toJson(obj));
    }

    @Override
    public <T, E> R<T> execute(RequestExecutor<String, E> executor, ApiUrl url, E data) throws WNErrorException {
        return this.execute(executor, url.getUrl(this.getConfigStorage()), data);
    }

    /**
     * 向微信端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求.
     */
    @Override
    public <T, E> R<T> execute(RequestExecutor<String, E> executor, String uri, E data) throws WNErrorException {
        int retryTimes = 0;
        do {
            try {
                R<T> r = this.executeInternal(executor, uri, data);
                if (r.getCode() == 403) {
                    return reExecute(executor, uri, data);
                }
                return r;
            } catch (WNErrorException e) {
                WNError error = e.getError();
                /*
                 * 发生以下情况时尝试刷新access_token
                 * 401 获取 access_token 时 AppSecret 错误，或者 access_token 无效。请开发者认真比对 AppSecret 的正确性，或查看是否正在为恰当的公众号调用接口
                 * 403 access_token 超时，请检查 access_token 的有效期，请参考基础支持 - 获取 access_token 中，对 access_token 的详细机制说明
                 */
                if (error.getErrorCode() == 403) {
                    return reExecute(executor, uri, data);
                }


                if (retryTimes + 1 > this.maxRetryTimes) {
                    log.warn("重试达到最大次数【{}】", maxRetryTimes);
                    //最后一次重试失败后，直接抛出异常，不再等待
                    throw new RuntimeException("微信服务端异常，超出重试次数");
                }

                // -1 系统繁忙, 1000ms后重试
                if (error.getErrorCode() == -1) {
                    int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
                    try {
                        log.warn("系统繁忙，{} ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw e;
                }
            }
        } while (retryTimes++ < this.maxRetryTimes);

        log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
        throw new RuntimeException("服务端异常，超出重试次数");
    }

    protected <E, T> R<T> executeInternal(RequestExecutor<String, E> executor, String uri, E data) throws WNErrorException {
        //E dataForLog = DataUtils.handleDataWithSecret(data);

        if (uri.contains("access_token=")) {
            throw new IllegalArgumentException("uri参数中不允许有access_token: " + uri);
        }

        String accessToken = getAccessToken(false);

        try {
            String result = executor.execute(accessToken, uri, data);
            Type type = new TypeToken<R<T>>() {
            }.getType();

            R<T> r = WNGsonBuilder.create().fromJson(result, type);

            log.debug("\n【URI】: {}, 【AccessToken】： {}, \n【RET】：{}", uri, accessToken, result);
            return r;
        } catch (IOException e) {
            log.debug("\n【URI】: {}, 【AccessToken】： {}, \n【RET】：{}", uri, accessToken, e.getMessage());
            throw new WNErrorException(WNError.builder().errorMsg(e.getMessage()).build(), e);
        }
    }

    private <E, T> R<T> reExecute(RequestExecutor<String, E> executor, String uri, E data) throws WNErrorException {
        // 强制设置wxMpConfigStorage它的access token过期了，这样在下一次请求里就会刷新access token
        Lock lock = this.getConfigStorage().getAccessTokenLock();
        lock.lock();
        try {
            this.getConfigStorage().expireAccessToken();
        } catch (Exception ex) {
            this.getConfigStorage().expireAccessToken();
        } finally {
            lock.unlock();
        }

        if (this.getConfigStorage().autoRefreshToken()) {
            return this.executeInternal(executor, uri, data);
        }
        return null;
    }


    public AuthService getAuthService() {
        return authService;
    }

}
