package org.wlpiaoyi.server.demo.cloud.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
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

    @Resource(name = "encrypt.rsad")
    private RsaCipher rsaDecrypt;

    @SneakyThrows
    private Aes createAes(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String eSalt = request.getHeaders().get(WebUtils.HEADER_SALT_KEY).get(0);
        String dSalt = new String(this.rsaDecrypt.decrypt(DataUtils.base64Decode(eSalt.getBytes())));
        String[] keys = dSalt.split(",");
        return Aes.create().setKey(keys[0]).setIV(keys[1]).load();
    }


    /**
     * 在执行全局请求或响应的过滤器的时候会执行该方法，并把请求体或响应体传递进来。
     * @param exchange 网关处理上下文
     * @param body 源请求或响应体
     * @return 返回处理过的请求体或响应体
     */
    @SneakyThrows
    @Override
    public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] body) {
        if(body == null || body.length == 0){
            return Mono.empty();
        }
        //路由完成请求
        if(ServerWebExchangeUtils.isAlreadyRouted(exchange)){
           return Mono.just(body);
        }
        return Mono.just(this.createAes(exchange).decrypt(body));
    }


}
