package cloud.weiniu.sdk.http;

import cloud.weiniu.sdk.error.WNErrorException;

import java.io.IOException;

/**
 * http请求执行器.
 *
 * @param <T> 返回值类型
 * @param <E> 请求参数类型
 * @author tiger
 */
public interface RequestExecutor<T, E> {

    /**
     * 执行http请求.
     *
     * @param uri  uri
     * @param data 数据
     * @return 响应结果
     * @throws WNErrorException 自定义异常
     * @throws IOException      io异常
     */
    String execute(String accessToken, String uri, E data) throws WNErrorException, IOException;

    /**
     * 执行http请求.
     *
     * @param uri     uri
     * @param data    数据
     * @param handler http响应处理器
     * @throws WNErrorException 自定义异常
     * @throws IOException      io异常
     */
    void execute(String accessToken, String uri, E data, ResponseHandler<T> handler) throws WNErrorException, IOException;
}
