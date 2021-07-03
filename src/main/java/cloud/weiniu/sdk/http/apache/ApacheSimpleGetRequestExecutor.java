package cloud.weiniu.sdk.http.apache;

import cloud.weiniu.sdk.error.WNErrorException;
import cloud.weiniu.sdk.http.RequestHttp;
import cloud.weiniu.sdk.http.SimpleGetRequestExecutor;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * @author tiger (tiger@microsoul.com) 7/2/21
 */
public class ApacheSimpleGetRequestExecutor extends SimpleGetRequestExecutor<CloseableHttpClient, HttpHost> {
    public ApacheSimpleGetRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public String execute(String accessToken, String uri, String queryParam) throws WNErrorException, IOException {
        if (queryParam != null) {
            if (uri.indexOf('?') == -1) {
                uri += '?';
            }
            uri += uri.endsWith("?") ? queryParam : '&' + queryParam;
        }
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36");
        httpGet.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        /*if (requestHttp.getRequestHttpProxy() != null) {
            RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
            httpGet.setConfig(config);
        }
*/
        httpGet.addHeader("Authorization", "Bearer " + accessToken);

        try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpGet)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            return handleResponse(responseContent);
        } finally {
            httpGet.releaseConnection();
        }
    }

}
