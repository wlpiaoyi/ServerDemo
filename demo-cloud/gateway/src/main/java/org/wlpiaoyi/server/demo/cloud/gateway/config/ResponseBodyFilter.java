package org.wlpiaoyi.server.demo.cloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
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

    private final GatewayFilter delegate;

    private final String[] encryptPatterns;

    public ResponseBodyFilter(ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory,
                              ResponseRewrite rewriteFunction) {
        this.delegate = modifyResponseBodyGatewayFilterFactory.apply(new ModifyResponseBodyGatewayFilterFactory.Config()
                        .setRewriteFunction(rewriteFunction)
                        .setInClass(byte[].class)
                        .setOutClass(byte[].class));
        this.encryptPatterns = SpringUtils.resolve("${wlpiaoyi.ee.cors.data.patterns.encrypt}").split(" ");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if(!WebUtils.mathPath(path, this.encryptPatterns)){
            return chain.filter(exchange);
        }
        return delegate.filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
    }
}
