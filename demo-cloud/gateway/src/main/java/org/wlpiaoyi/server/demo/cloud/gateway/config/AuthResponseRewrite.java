package org.wlpiaoyi.server.demo.cloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.framework.utils.MapUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.tools.web.model.ConfigModel;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-11-13 20:29:29</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
@Component
public class AuthResponseRewrite implements RewriteFunction<Map, Map> {

    @Autowired
    private ConfigModel configModel;

    @Override
    public Publisher<Map> apply(ServerWebExchange exchange, Map respBody) {
        ServerHttpResponse response = exchange.getResponse();
        if(response.getStatusCode().value() != 200){
            return Mono.just(respBody);
        }
//        String token = WebUtils.generateJwtToken(new HashMap(){{
////            put(Common.JWT_AUTH_TALENT_KEY, respBody.get(Common.JWT_AUTH_TALENT_KEY));
//        }},new HashMap(){{
//            put(Common.JWT_AUTH_ACCOUNT_KEY, respBody.get(Common.JWT_AUTH_ACCOUNT_KEY));
//        }}, this.configModel.getJwtOutSecond(), this.configModel.getJwtSecretKey());
        String token = MapUtils.getString(MapUtils.getMap(respBody, "data"), "header.token");
        if(ValueUtils.isNotBlank(token)) response.getHeaders().set(Common.HEADER_TOKEN_KEY,  token);
        return Mono.just(respBody);
    }


}
