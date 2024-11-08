package org.wlpiaoyi.server.demo.cloud.gateway.config.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-28 12:34:17</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
@Component
public class RequestRewrite implements RewriteFunction<byte[], byte[]> {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 在执行全局请求或响应的过滤器的时候会执行该方法，并把请求体或响应体传递进来。
     * @param exchange 网关处理上下文
     * @param body 源请求或响应体
     * @return 返回处理过的请求体或响应体
     */
    @Override
    public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] body) {
        if(body == null || body.length == 0){
            return Mono.empty();
        }
        //路由完成请求
        if(ServerWebExchangeUtils.isAlreadyRouted(exchange)){
           return Mono.just(body);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(body);
        out.writeBytes("test before ready routed".getBytes());
        body = out.toByteArray();
        return Mono.just(body);
    }


}
