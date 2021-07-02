package cloud.weiniu.sdk.message;

import cloud.weiniu.sdk.config.ConfigStorage;
import cloud.weiniu.sdk.config.WeiniuCloudProperties;
import cloud.weiniu.sdk.util.crypto.CryptUtil;

/**
 * @author tiger (tiger@microsoul.com) 7/1/21
 */
public class MessageTools {

    public static Message fromJson(String json,
                                   ConfigStorage configStorage,
                                   String service,
                                   int action,
                                   String timestamp,
                                   String nonce,
                                   String signature) {
        CryptUtil cryptUtil = new CryptUtil(configStorage);

        String plainText = cryptUtil.decrypt(signature, timestamp, nonce, json);
        return new Message(service, action, plainText);
    }
}
