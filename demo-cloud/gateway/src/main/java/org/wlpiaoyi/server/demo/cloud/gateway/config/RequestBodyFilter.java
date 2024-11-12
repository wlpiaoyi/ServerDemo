package org.wlpiaoyi.server.demo.cloud.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
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


    private final GatewayFilter delegate;

    private final String[] decryptPatterns;

    public RequestBodyFilter(ModifyRequestBodyGatewayFilterFactory modifyRequestBody, RequestRewrite bodyRewrite, String[] decryptPatterns) {
        this.delegate = modifyRequestBody.apply(new ModifyRequestBodyGatewayFilterFactory.Config()
                        .setRewriteFunction(bodyRewrite)
                        .setInClass(byte[].class)
                        .setOutClass(byte[].class));
        this.decryptPatterns = decryptPatterns;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if(!WebUtils.mathPath(path, this.decryptPatterns)){
            return chain.filter(exchange);
        }
        return delegate.filter(exchange.mutate().request(changeRequest(request)).build(), chain);
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
    }

    private ServerHttpRequest changeRequest(ServerHttpRequest request) {
        ServerHttpRequest.Builder requestBuilder = request.mutate();
        requestBuilder.header("testFilter", "1");
        return requestBuilder.build();
    }

}
