package org.wlpiaoyi.server.demo.cloud.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.tools.web.model.ConfigModel;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 全局拦截器拦截请求体
 * </p>
 * <p><b>{@code @date:}</b>2024-11-08 12:45:54</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
class DataRequestFilter implements GlobalFilter, Ordered {


    private final GatewayFilter delegate;

    private final String[] decryptPatterns;

    public DataRequestFilter(ModifyRequestBodyGatewayFilterFactory modifyRequestBody, DataRequestRewrite bodyRewrite) {
        this.delegate = modifyRequestBody.apply(new ModifyRequestBodyGatewayFilterFactory.Config()
                        .setRewriteFunction(bodyRewrite)
                        .setInClass(byte[].class)
                        .setOutClass(byte[].class));
        this.decryptPatterns = SpringUtils.getBean(ConfigModel.class).getDecryptPatterns();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if(!request.getMethod().name().equals(HttpMethod.POST.name())
                && !request.getMethod().name().equals(HttpMethod.PUT.name())){
            return chain.filter(exchange);
        }
        if(!WebUtils.mathPath(path, this.decryptPatterns)){
            return chain.filter(exchange);
        }
        return delegate.filter(exchange.mutate().request(changeRequest(request)).build(), chain);
    }

    @Override
    public int getOrder() {
        return Common.BODY_REQ_FILTER_ORDER;
    }

    private ServerHttpRequest changeRequest(ServerHttpRequest request) {
        String contentType = request.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0);
        if(!contentType.toUpperCase(Locale.ROOT).startsWith(Common.ENCRYPT_CONTENT_TYPE_HEAD_TAG.toUpperCase(Locale.ROOT))){
            return request;
        }
        ServerHttpRequest.Builder requestBuilder = request.mutate();
        contentType = contentType.substring(Common.ENCRYPT_CONTENT_TYPE_HEAD_TAG.length());
        requestBuilder.header(HttpHeaders.CONTENT_TYPE, contentType);
        return requestBuilder.build();
    }

}
