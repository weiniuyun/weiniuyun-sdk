package cloud.weiniu.sdk.api;

import cloud.weiniu.sdk.config.ConfigStorage;
import cloud.weiniu.sdk.error.WNErrorException;
import cloud.weiniu.sdk.http.RequestExecutor;
import cloud.weiniu.sdk.util.ApiUrl;
import cloud.weiniu.sdk.util.R;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
public interface WeiniuCloudService {
    /**
     * 获取access_token, 不强制刷新access_token
     */
    String getAccessToken() throws WNErrorException;


    /**
     * 获取access_token
     */
    String getAccessToken(boolean forceRefresh) throws WNErrorException;


    /**
     * <pre>
     * 验证推送过来的消息的正确性
     * </pre>
     *
     * @param timestamp 时间戳
     * @param nonce     随机数
     */
    boolean checkSignature(String timestamp, String nonce, String signature);

    void setConfigStorage(ConfigStorage configStorage);

    /**
     * 获取WeiniuProperties 对象
     *
     * @return WeiniuProperties
     */
    ConfigStorage getConfigStorage();


    void initHttp();


    <T> R<T> get(String url, String queryParam) throws WNErrorException;

    <T> R<T> get(ApiUrl url, String queryParam) throws WNErrorException;

    <T> R<T> post(String url, String postData) throws WNErrorException;

    <T> R<T> post(ApiUrl url, String postData) throws WNErrorException;

    <T> R<T> post(String url, Object obj) throws WNErrorException;


    <T, E> R<T> execute(RequestExecutor<String, E> executor, ApiUrl url, E data) throws WNErrorException;

    <T, E> R<T> execute(RequestExecutor<String, E> executor, String uri, E data) throws WNErrorException;


    AuthService getAuthService();
}
