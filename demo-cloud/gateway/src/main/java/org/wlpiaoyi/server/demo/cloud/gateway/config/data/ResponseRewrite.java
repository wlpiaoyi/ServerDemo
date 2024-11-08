package org.wlpiaoyi.server.demo.cloud.gateway.config.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-11-08 11:00:31</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
@Component
public class ResponseRewrite implements RewriteFunction<byte[], byte[]> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] bytes) {
        try {
            log.info("response:{}", new String(bytes));
            Map respBody = objectMapper.readValue(bytes, Map.class);
            respBody.put("respChange", "test");
            return Mono.just(objectMapper.writeValueAsBytes(respBody));
        } catch (Exception ex) {
            return Mono.error(new BusinessException(ex));
        }
    }
}
