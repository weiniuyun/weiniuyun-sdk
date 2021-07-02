package cloud.weiniu.sdk.callback;

import cloud.weiniu.sdk.api.WeiniuCloudService;
import cloud.weiniu.sdk.config.BaseConfiguration;
import cloud.weiniu.sdk.message.Message;
import cloud.weiniu.sdk.message.MessageTools;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
@Slf4j
public class BaseNotifyController {

    public String post(String sevice,
                       int action,
                       String requestBody,
                       String signature,
                       String timestamp,
                       String nonce) {
        final WeiniuCloudService wnService = BaseConfiguration.getService();

        Message inMessage = MessageTools.fromJson(requestBody, wnService.getConfigStorage(),
                sevice, action, timestamp, nonce, signature);
        log.debug("\n消息解密后内容为：\n{} ", requestBody);
        String outMessage = this.route(inMessage);
        if (outMessage == null) {
            return "success";
        }

        return outMessage;
    }

    private String route(Message message) {
        try {
            return BaseConfiguration.getRouter().route(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }


}
