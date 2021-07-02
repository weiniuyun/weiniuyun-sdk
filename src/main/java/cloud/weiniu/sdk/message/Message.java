package cloud.weiniu.sdk.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
@Data
@AllArgsConstructor
public class Message {
    private String service;
    private int action;
    private String data;
}
