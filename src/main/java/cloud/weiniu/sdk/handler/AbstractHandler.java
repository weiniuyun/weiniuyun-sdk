package cloud.weiniu.sdk.handler;

import cloud.weiniu.sdk.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
public abstract class AbstractHandler implements MessageHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

}
