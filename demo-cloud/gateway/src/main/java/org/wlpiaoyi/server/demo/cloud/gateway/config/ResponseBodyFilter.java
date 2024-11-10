package org.wlpiaoyi.server.demo.cloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 全局拦截器拦截响应体
 * </p>
 * <p><b>{@code @date:}</b>2024-11-08 12:43:03</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
public class ResponseBodyFilter implements GlobalFilter, Ordered {

    private GatewayFilter delegate;

    public ResponseBodyFilter(ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory,
                              ResponseRewrite rewriteFunction) {
        delegate = modifyResponseBodyGatewayFilterFactory.apply(new ModifyResponseBodyGatewayFilterFactory.Config()
                        .setRewriteFunction(rewriteFunction)
                        .setInClass(byte[].class)
                        .setOutClass(byte[].class));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return delegate.filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
    }
}
