package cloud.weiniu.sdk.http;

import cloud.weiniu.sdk.error.WNErrorException;
import cloud.weiniu.sdk.http.apache.ApacheSimpleGetRequestExecutor;

import java.io.IOException;

/**
 * 简单的GET请求执行器.
 * 请求的参数是String, 返回的结果也是String
 *
 * @author tiger
 */
public abstract class SimpleGetRequestExecutor<H, P> implements RequestExecutor<String, String> {
    protected RequestHttp<H, P> requestHttp;

    public SimpleGetRequestExecutor(RequestHttp<H, P> requestHttp) {
        this.requestHttp = requestHttp;
    }

    @Override
    public void execute(String accessToken, String uri, String data, ResponseHandler<String> handler) throws WNErrorException, IOException {
        handler.handle(this.execute(accessToken, uri, data));
    }

    public static RequestExecutor<String, String> create(RequestHttp requestHttp) {
        switch (requestHttp.getRequestType()) {
            case APACHE_HTTP:
                return new ApacheSimpleGetRequestExecutor(requestHttp);
           /* case JODD_HTTP:
                return new JoddHttpSimpleGetRequestExecutor(requestHttp);
            case OK_HTTP:
                return new OkHttpSimpleGetRequestExecutor(requestHttp);*/
            default:
                throw new IllegalArgumentException("非法请求参数");
        }
    }

    protected String handleResponse(String responseContent) throws WNErrorException {
        return responseContent;
    }
}
