package cloud.weiniu.sdk.message;

import cloud.weiniu.sdk.api.WeiniuCloudService;
import cloud.weiniu.sdk.handler.WNErrorExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
public class MessageRouter {

    private static final int DEFAULT_THREAD_POOL_SIZE = 100;

    private final WeiniuCloudService wnService;

    private final List<MessageRouterRule> rules = new ArrayList<>();

    private ExecutorService executorService;

    private WNErrorExceptionHandler exceptionHandler;

    public MessageRouter(WeiniuCloudService wnService) {
        this.wnService = wnService;
        this.executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    }

    List<MessageRouterRule> getRules() {
        return this.rules;
    }

    public void setExceptionHandler(WNErrorExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * 开始一个新的Route规则.
     */
    public MessageRouterRule rule() {
        return new MessageRouterRule(this);
    }

    public String route(final Message message) {
        /*if (isMsgDuplicated(message)) {
            // 如果是重复消息，那么就不做处理
            return null;
        }*/

        final List<MessageRouterRule> matchRules = new ArrayList<>();
        // 收集匹配的规则
        for (final MessageRouterRule rule : this.rules) {
            if (rule.test(message)) {
                matchRules.add(rule);
                if (!rule.isNext()) {  //并行处理规则
                    break;
                }
            }
        }

        if (matchRules.size() == 0) {
            return null;
        }

        String res = null;
        final List<Future> futures = new ArrayList<>();
        for (final MessageRouterRule rule : matchRules) {
            // 返回最后一个非异步的rule的执行结果
            if (rule.isAsync()) {
                futures.add(
                        this.executorService.submit(() -> {
                            rule.service(message, MessageRouter.this.wnService, MessageRouter.this.exceptionHandler);
                        })
                );
            } else {
                res = rule.service(message, this.wnService, this.exceptionHandler);
                // 在同步操作结束，session访问结束
                //this.log.debug("End session access: async=false, sessionId={}", wxMessage.getFromUserName());
                sessionEndAccess(message);
            }
        }

        if (futures.size() > 0) {
            this.executorService.submit(() -> {
                for (Future future : futures) {
                    try {
                        future.get();
                        // 异步操作结束，session访问结束
                        sessionEndAccess(message);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        //
                    }
                }
            });
        }
        return res;
    }

    private void sessionEndAccess(Message message) {
       /* InternalSession session = ((InternalSessionManager) this.sessionManager).findSession(wxMessage.getFromUserName());
        if (session != null) {
            session.endAccess();
        }*/

    }

}
