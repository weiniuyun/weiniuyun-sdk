package cloud.weiniu.sdk.util;

import cloud.weiniu.sdk.json.WNGsonBuilder;

/**
 * 微妞分布式平台-公用工具包
 *
 * @author 广州加叁信息科技有限公司 (tiger@microsoul.com)
 * @version V1.0.0
 */

public class R<T> {

    private int code = 200;
    private String message = "";
    private T data;

    public static R ok() {
        return new R<>();
    }

    public static <T> R<T> ok(T data) {
        return new R<>(data);
    }

    public static <T> R<T> fail() {
        return new R<>();

    }


    public static <T> R<T> fail(int code) {
        return new R<>(code);
    }

    public static <T> R<T> fail(String message, int code) {
        return new R<>(message, code);
    }


    public R(T data) {
        this.data = data;
    }

    public R(String message) {
        this.message = message;
    }

    public R(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public R(int code) {
        this.code = code;
    }


    public R() {
        super();
    }

    public int getCode() {
        return code;
    }

    public R<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public R<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public R<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return WNGsonBuilder.create().toJson(this);
    }
}
