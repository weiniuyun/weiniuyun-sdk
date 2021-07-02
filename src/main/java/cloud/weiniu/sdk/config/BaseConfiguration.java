package cloud.weiniu.sdk.config;

import cloud.weiniu.sdk.api.WeiniuCloudService;
import cloud.weiniu.sdk.api.impl.WeiniuCloudServiceImpl;
import cloud.weiniu.sdk.config.impl.DefaultConfigStorageImpl;
import cloud.weiniu.sdk.message.MessageRouter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
@EnableConfigurationProperties(WeiniuCloudProperties.class)
public abstract class BaseConfiguration {

    private static WeiniuCloudService service;

    private static MessageRouter router;

    @Resource
    private WeiniuCloudProperties properties;


    @PostConstruct
    public void initService() {
        service = new WeiniuCloudServiceImpl();
        ConfigStorage configStorage = new DefaultConfigStorageImpl();
        configStorage.setAppId(properties.getAppId());
        configStorage.setAppSecret(properties.getAppSecret());
        configStorage.setApiHost(properties.getApiHost());
        service.setConfigStorage(configStorage);
        router = new MessageRouter(service);
        this.newRules(router);
    }


    public static WeiniuCloudService getService() {
        return service;
    }

    public static MessageRouter getRouter() {
        return router;
    }


    protected abstract void newRules(MessageRouter router);

}
