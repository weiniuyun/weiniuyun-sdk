package cloud.weiniu.sdk.json;

import cloud.weiniu.sdk.bean.AccessToken;
import cloud.weiniu.sdk.error.WNError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author tiger (tiger@microsoul.com) 7/1/21
 */
public class WNGsonBuilder {

    private static final GsonBuilder INSTANCE = new GsonBuilder();

    static {
        INSTANCE.disableHtmlEscaping();
        INSTANCE.registerTypeAdapter(AccessToken.class, new AccessTokenAdapter());
        INSTANCE.registerTypeAdapter(WNError.class, new WNErrorAdapter());

    }

    public static Gson create() {
        return INSTANCE.create();
    }


}
