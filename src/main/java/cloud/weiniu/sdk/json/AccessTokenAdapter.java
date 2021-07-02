package cloud.weiniu.sdk.json;

import cloud.weiniu.sdk.bean.AccessToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author tiger (tiger@microsoul.com) 7/1/21
 */
public class AccessTokenAdapter implements JsonDeserializer<AccessToken> {
    @Override
    public AccessToken deserialize(JsonElement json,
                                   Type type,
                                   JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        AccessToken accessToken = new AccessToken();
        JsonObject accessTokenJsonObject = json.getAsJsonObject();
        JsonObject data = accessTokenJsonObject.getAsJsonObject("data");
        if (data != null && !data.isJsonNull()) {
            if (data.get("value") != null && !data.get("value").isJsonNull()) {
                accessToken.setAccessToken(GsonHelper.getAsString(data.get("value")));
            }
            if (accessTokenJsonObject.get("expiresIn") != null && !accessTokenJsonObject.get("expiresIn").isJsonNull()) {
                accessToken.setExpiresIn(GsonHelper.getAsPrimitiveInt(accessTokenJsonObject.get("expiresIn")));
            }
        }

        return accessToken;
    }
}
