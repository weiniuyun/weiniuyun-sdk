package cloud.weiniu.sdk.bean;

import cloud.weiniu.sdk.json.WNGsonBuilder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tiger (tiger@microsoul.com) 7/1/21
 */
@Data
public class AccessToken implements Serializable {
    private static final long serialVersionUID = 8709719312922168909L;

    private String accessToken;

    private int expiresIn = -1;

    public static AccessToken fromJson(String json) {
        return WNGsonBuilder.create().fromJson(json, AccessToken.class);
    }

}
