package cloud.weiniu.sdk.error;

import cloud.weiniu.sdk.json.WNGsonBuilder;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
@Data
@Builder
public class WNError implements Serializable {
    private static final long serialVersionUID = 7869786563361406291L;

    /**
     * 微信错误代码.
     */
    private int errorCode;

    /**
     * 微信错误信息.
     * （如果可以翻译为中文，就为中文）
     */
    private String errorMsg;

    /**
     * 微信接口返回的错误原始信息（英文）.
     */
    private String errorMsgEn;

    private String json;


    public static WNError fromJson(String json) {
        return WNGsonBuilder.create().fromJson(json, WNError.class);
    }

    @Override
    public String toString() {
        if (this.json == null) {
            return "错误代码：" + this.errorCode + ", 错误信息：" + this.errorMsg;
        }

        return "错误代码：" + this.errorCode + ", 错误信息：" + this.errorMsg + "，微信原始报文：" + this.json;
    }

}
