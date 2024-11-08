package org.wlpiaoyi.server.demo.cloud.gateway.config.data;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 全局拦截器拦截请求体
 * </p>
 * <p><b>{@code @date:}</b>2024-11-08 12:45:54</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
public class RequestBodyFilter implements GlobalFilter, Ordered {


    private GatewayFilter delegate;

    public RequestBodyFilter(ModifyRequestBodyGatewayFilterFactory modifyRequestBody, RequestRewrite bodyRewrite) {
        delegate = modifyRequestBody
                .apply(new ModifyRequestBodyGatewayFilterFactory.Config()
                        .setRewriteFunction(bodyRewrite)
                        .setInClass(byte[].class)
                        .setOutClass(byte[].class));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        return delegate.filter(exchange.mutate().request(changeRequest(request)).build(), chain);
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
    }

    private ServerHttpRequest changeRequest(ServerHttpRequest request) {
        ServerHttpRequest.Builder requestBuilder = request.mutate();
        requestBuilder.header("testFilter", "1");
        requestBuilder.header("testAdd", "1");
        requestBuilder.header("testEdit", "2");
        return requestBuilder.build();
    }

}
