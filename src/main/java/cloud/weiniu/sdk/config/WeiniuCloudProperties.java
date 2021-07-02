package cloud.weiniu.sdk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tiger (tiger@microsoul.com) 2020/4/28
 */
@Data
@ConfigurationProperties(prefix = "weiniu.cloud")
public class WeiniuCloudProperties {
    /**
     * 设置微妞云平台的orgId
     */
    private String orgId;

    /**
     * 设置微妞云平台的apiHost
     */
    private String apiHost;

    /**
     * 设置微妞云平台的appId
     */
    private String appId;

    /**
     * 设置微妞云平台的appSecret
     */
    private String appSecret;

    /**
     * 设置token
     */
    private String token;

    /**
     * 设置EncodingAESKey
     */
    private String aesKey;

}
