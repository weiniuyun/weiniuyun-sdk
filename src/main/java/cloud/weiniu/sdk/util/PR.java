package cloud.weiniu.sdk.util;

/**
 * @author tiger (tiger@microsoul.com) 2020/10/14
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微妞分布式平台-公用工具包
 *
 * @author 广州加叁信息科技有限公司 (tiger@microsoul.com)
 * @version V1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PR<Entity> extends R<Entity> {

    private PR(Entity data, long total) {
        super(data);
        this.total = total;
    }

    private PR(Entity data, long total, Object summary) {
        this(data, total);
        this.summary = summary;
    }

    public PR(String message) {
        super(message);
    }

    public PR(String message, int code) {
        super(message, code);
    }

    public PR(int code) {
        super(code);
    }


    public static <Entity> PR<Entity> ok(Entity data, long total) {
        return new PR<>(data, total);
    }

    public static <Entity> PR<Entity> ok(Entity data, long total, Object summary) {
        return new PR<>(data, total, summary);
    }

    public static <T> PR<T> fail(int code) {
        return new PR<>(code);
    }

    public static <T> PR<T> fail(String message, int code) {
        return new PR<>(message, code);
    }


    private long total;

    private Object summary;


}
