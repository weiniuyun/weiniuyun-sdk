package cloud.weiniu.sdk.json;

import com.google.gson.JsonElement;

/**
 * @author tiger (tiger@microsoul.com) 7/1/21
 */
public class GsonHelper {

    public static boolean isNull(JsonElement element) {
        return element == null || element.isJsonNull();
    }


    public static String getAsString(JsonElement element) {
        return isNull(element) ? null : element.getAsString();
    }

    public static Integer getAsInteger(JsonElement element) {
        return isNull(element) ? null : element.getAsInt();
    }

    public static int getAsPrimitiveInt(JsonElement element) {
        Integer r = getAsInteger(element);
        return r == null ? 0 : r;
    }

}
