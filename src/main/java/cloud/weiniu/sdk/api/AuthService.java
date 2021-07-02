package cloud.weiniu.sdk.api;

import cloud.weiniu.sdk.bean.AuthUser;
import cloud.weiniu.sdk.error.WNErrorException;
import cloud.weiniu.sdk.util.R;
import lombok.AllArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static cloud.weiniu.sdk.util.ApiPathConsts.Uaa.GET_USER_INFO_URL;

/**
 * @author tiger (tiger@microsoul.com) 7/2/21
 */
@AllArgsConstructor
public class AuthService {

    private WeiniuCloudService weiniuCloudService;


    public R<AuthUser> getUserInfo(String token) throws WNErrorException {
        try {
            return weiniuCloudService.get(GET_USER_INFO_URL, "token=" + URLEncoder.encode(token, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
