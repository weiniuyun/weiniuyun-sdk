package cloud.weiniu.sdk.message;

import cloud.weiniu.sdk.api.WeiniuCloudService;
import cloud.weiniu.sdk.error.WNErrorException;
import cloud.weiniu.sdk.handler.WNErrorExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
public class MessageRouterRule {

    private final MessageRouter routerBuilder;

    private List<MessageHandler> handlers = new ArrayList<>();

    private boolean next = false;

    private boolean async = true;

    private String service;

    private int action = 0;


    public MessageRouterRule(MessageRouter routerBuilder) {
        this.routerBuilder = routerBuilder;
    }

    public MessageRouterRule service(String service) {
        this.service = service;
        return this;
    }

    public MessageRouterRule action(int action) {
        this.action = action;
        return this;
    }

    /**
     * 设置是否异步执行，默认是true
     *
     * @param async
     */
    public MessageRouterRule async(boolean async) {
        this.async = async;
        return this;
    }

    /**
     * 添加此规则的处理器
     *
     * @param handler
     */
    public MessageRouterRule handler(MessageHandler handler) {
        return handler(handler, (MessageHandler[]) null);
    }

    /**
     * 添加此规则的多个处理器
     *
     * @param handler
     */

    public MessageRouterRule handler(MessageHandler handler, MessageHandler... otherHandlers) {
        this.handlers.add(handler);
        if (otherHandlers != null && otherHandlers.length > 0) {
            Collections.addAll(this.handlers, otherHandlers);
        }
        return this;
    }

    /**
     * 规则结束，代表如果一个消息匹配该规则，那么它将不再会进入其他规则，消息链条
     */
    public MessageRouter end() {
        this.routerBuilder.getRules().add(this);
        return this.routerBuilder;
    }

    /**
     * 规则结束，但是消息还会进入其他规则
     */
    public MessageRouter next() {
        this.next = true;
        return end();
    }


    protected boolean test(Message message) {
        return (
                this.service == null || this.service.equals(message.getService())
                        && this.action == 0 || this.action == message.getAction()
        );
    }

    public boolean isNext() {
        return this.next;
    }

    public boolean isAsync() {
        return this.async;
    }


    protected String service(Message message,
                             WeiniuCloudService wnService,
                             WNErrorExceptionHandler exceptionHandler) {
        try {
            String res = "";
            for (MessageHandler handler : this.handlers) {
                // 返回最后handler的结果
                res = handler.handle(message, wnService);
            }

            return res;
        } catch (WNErrorException e) {
            exceptionHandler.handle(e);
        }
        return null;
    }

}
