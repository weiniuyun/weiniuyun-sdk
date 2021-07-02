package cloud.weiniu.sdk.util;

import cloud.weiniu.sdk.config.ConfigStorage;
import lombok.AllArgsConstructor;

/**
 * @author tiger (tiger@microsoul.com) 7/1/21
 */
public class ApiPathConsts {
    public static final String API_DEFAULT_HOST_URL = "https://api.cloud.microsoul.com";


    public static String buildUrl(ConfigStorage storage, String prefix, String path) {
        if (storage == null) {
            return prefix + path;
        }

        if (storage.getApiHost() != null && prefix.equals(API_DEFAULT_HOST_URL)) {
            return storage.getApiHost() + path;
        }


        return prefix + path;
    }


    @AllArgsConstructor
    public enum Other implements ApiUrl {
        /**
         * 获取access_token.
         */
        GET_ACCESS_TOKEN_URL(API_DEFAULT_HOST_URL, "/uaa/auth/access_token?grant_type=client_credential&app_id=%s&app_secret=%s"),
        ;

        @Override
        public String getUrl(ConfigStorage config) {
            return buildUrl(config, prefix, path);
        }

        private String prefix;
        private String path;

    }

    @AllArgsConstructor
    public enum Uaa implements ApiUrl {
        /**
         * 获取access_token.
         */
        GET_USER_INFO_URL(API_DEFAULT_HOST_URL, "/uaa/auth/user_info"),
        ;

        @Override
        public String getUrl(ConfigStorage config) {
            return buildUrl(config, prefix, path);
        }

        private String prefix;
        private String path;

    }
}
