package org.wlpiaoyi.server.demo.cloud.gateway.config;

import com.google.gson.Gson;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;
import org.wlpiaoyi.server.demo.common.redis.service.RedisService;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthUser;
import org.wlpiaoyi.server.demo.common.tools.web.model.ConfigModel;
import org.wlpiaoyi.server.demo.common.tools.web.model.R;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2025-06-19 18:32:23</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
class ExclusionBizFilter implements GlobalFilter, Ordered {

    private final GatewayFilter delegate;

    private final String[] exclusionBizPatterns;

    private final Gson gson = GsonBuilder.gsonDefault();

    private final RedisService redisService;

    public ExclusionBizFilter(ModifyRequestBodyGatewayFilterFactory modifyRequestBody) {
        this.delegate = modifyRequestBody.apply(new ModifyRequestBodyGatewayFilterFactory.Config());
        this.exclusionBizPatterns = SpringUtils.getBean(ConfigModel.class).getExclusionBizPatterns();
        this.redisService = SpringUtils.getBean(RedisService.class);
    }


    /**
     * 返回自定义 JSON 响应
     */
    private Mono<Void> writeCustomResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        R res = R.data(status.value(), message);
        byte[] bytes = gson.toJson(res).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(buffer));
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if(WebUtils.mathPath(path, this.exclusionBizPatterns)){
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst(Common.HEADER_TOKEN_KEY);
        if(ValueUtils.isBlank(token)){
            return writeCustomResponse(exchange, HttpStatus.FORBIDDEN, "No token!");
        }
        if(!this.redisService.hasKey(AuthUser.keyTag + ":" + token)){
            return writeCustomResponse(exchange, HttpStatus.FORBIDDEN, "Access Denied!");
        }
        return chain.filter(exchange);
    }



    @Override
    public int getOrder() {
        return Common.EXCLUSION_BIZ_ORDER;
    }
}
