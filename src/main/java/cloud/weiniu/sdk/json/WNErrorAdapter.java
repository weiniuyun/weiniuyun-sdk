package cloud.weiniu.sdk.json;

import cloud.weiniu.sdk.error.WNError;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author tiger (tiger@microsoul.com) 7/1/21
 */
public class WNErrorAdapter implements JsonDeserializer<WNError> {
    @Override
    public WNError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        WNError.WNErrorBuilder errorBuilder = WNError.builder();
        JsonObject wxErrorJsonObject = json.getAsJsonObject();
        int code = GsonHelper.getAsPrimitiveInt(wxErrorJsonObject.get("code"));
        if (code != 200) {
            errorBuilder.errorCode(code);
            errorBuilder.errorMsg(GsonHelper.getAsString(wxErrorJsonObject.get("message")));
        }
        errorBuilder.json(json.toString());
        return errorBuilder.build();
    }

}
