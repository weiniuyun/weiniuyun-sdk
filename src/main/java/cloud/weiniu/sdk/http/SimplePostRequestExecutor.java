package cloud.weiniu.sdk.http;

import cloud.weiniu.sdk.error.WNError;
import cloud.weiniu.sdk.error.WNErrorException;
import cloud.weiniu.sdk.http.apache.ApacheSimplePostRequestExecutor;

import java.io.IOException;

/**
 * 简单的POST请求执行器，请求的参数是String, 返回的结果也是String
 *
 * @author tiger
 */
public abstract class SimplePostRequestExecutor<H, P> implements RequestExecutor<String, String> {
    protected RequestHttp<H, P> requestHttp;

    public SimplePostRequestExecutor(RequestHttp requestHttp) {
        this.requestHttp = requestHttp;
    }

    @Override
    public void execute(String accessToken, String uri, String data, ResponseHandler<String> handler)
            throws WNErrorException, IOException {
        handler.handle(this.execute(accessToken, uri, data));
    }

    public static RequestExecutor<String, String> create(RequestHttp requestHttp) {
        switch (requestHttp.getRequestType()) {
            case APACHE_HTTP:
                return new ApacheSimplePostRequestExecutor(requestHttp);
            /*case JODD_HTTP:
                return new JoddHttpSimplePostRequestExecutor(requestHttp);
            case OK_HTTP:
                return new OkHttpSimplePostRequestExecutor(requestHttp);*/
            default:
                throw new IllegalArgumentException("非法请求参数");
        }
    }

    public String handleResponse(String responseContent) throws WNErrorException {
        if (responseContent.isEmpty()) {
            throw new WNErrorException(WNError.builder().errorCode(500).errorMsg("无响应内容").build());
        }

        WNError error = WNError.fromJson(responseContent);
        if (error.getErrorCode() != 0) {
            throw new WNErrorException(error);
        }
        return responseContent;
    }
}
