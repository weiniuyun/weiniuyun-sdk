package cloud.weiniu.sdk.config.impl;

import cloud.weiniu.sdk.config.ConfigStorage;
import cloud.weiniu.sdk.http.apache.ApacheHttpClientBuilder;
import lombok.Data;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tiger (tiger@microsoul.com) 7/1/21
 */
@Data
public class DefaultConfigStorageImpl implements ConfigStorage {

    protected Lock accessTokenLock = new ReentrantLock();

    protected String appId;

    protected String appSecret;

    protected String apiHost;

    protected String accessToken;

    private volatile long expiresTime;

    private String token;

    private String aesKey;

    private ApacheHttpClientBuilder apacheHttpClientBuilder;


    @Override
    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > this.expiresTime;
    }

    @Override
    public void expireAccessToken() {
        this.expiresTime = 0;
    }

    @Override
    public boolean autoRefreshToken() {
        return true;
    }

    @Override
    public void updateAccessToken(String accessToken, int expiresInSeconds) {
        this.accessToken = accessToken;
        this.expiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
    }

}
