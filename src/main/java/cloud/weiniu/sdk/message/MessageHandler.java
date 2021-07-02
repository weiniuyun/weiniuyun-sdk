package cloud.weiniu.sdk.message;

import cloud.weiniu.sdk.api.WeiniuCloudService;
import cloud.weiniu.sdk.error.WNErrorException;

/**
 * 处理微信推送消息的处理器接口
 *
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
public interface MessageHandler {
    String handle(Message message,
                  WeiniuCloudService wnService) throws WNErrorException;
}
