package cloud.weiniu.sdk.handler;

import cloud.weiniu.sdk.error.WNErrorException;

/**
 * WNErrorExceptionHandler.
 *
 */
public interface WNErrorExceptionHandler {

    void handle(WNErrorException e);

}
