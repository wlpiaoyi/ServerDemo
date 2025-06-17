package org.wlpiaoyi.server.demo.cloud.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.tools.web.model.ConfigModel;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 令牌认证
 * </p>
 * <p><b>{@code @date:}</b>2024-11-13 20:06:46</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
class AuthResponseFilter implements GlobalFilter, Ordered {


    private final GatewayFilter delegate;

    private final String[] authPatterns;

    public AuthResponseFilter(ModifyRequestBodyGatewayFilterFactory modifyRequestBody, AuthResponseRewrite responseRewrite) {
        this.delegate = modifyRequestBody.apply(new ModifyRequestBodyGatewayFilterFactory.Config()
                .setRewriteFunction(responseRewrite)
                .setInClass(Math.class)
                .setOutClass(Map.class));
        this.authPatterns = SpringUtils.getBean(ConfigModel.class).getAuthPatterns();
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if(!WebUtils.mathPath(path, this.authPatterns)){
            return chain.filter(exchange);
        }
        return delegate.filter(exchange, chain);
    }



    @Override
    public int getOrder() {
        return 0;
    }

}
