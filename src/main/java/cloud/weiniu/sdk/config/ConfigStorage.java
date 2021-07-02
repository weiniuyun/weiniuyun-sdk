package cloud.weiniu.sdk.config;

import cloud.weiniu.sdk.http.apache.ApacheHttpClientBuilder;

import java.util.concurrent.locks.Lock;

/**
 * @author tiger (tiger@microsoul.com) 7/1/21
 */
public interface ConfigStorage {
    String getAccessToken();

    Lock getAccessTokenLock();

    boolean isAccessTokenExpired();

    /**
     * 强制将access token过期掉
     */
    void expireAccessToken();

    /**
     * 是否自动刷新token
     */
    boolean autoRefreshToken();


    String getToken();

    String getAesKey();

    String getAppId();

    void setAppId(String appId);

    String getAppSecret();

    void setAppSecret(String appSecret);


    String getApiHost();

    void setApiHost(String apiHost);

    void updateAccessToken(String accessToken, int expiresIn);

    ApacheHttpClientBuilder getApacheHttpClientBuilder();


}
