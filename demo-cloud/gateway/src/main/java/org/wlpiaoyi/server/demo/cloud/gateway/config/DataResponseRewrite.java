package org.wlpiaoyi.server.demo.cloud.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.framework.utils.StringUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.framework.utils.security.SignVerify;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.tools.web.model.ConfigModel;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-11-08 11:00:31</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
@Component
class DataResponseRewrite implements RewriteFunction<byte[], byte[]> {

    @Autowired
    private ObjectMapper objectMapper;

    @Resource(name = "encrypt.rsae")
    private RsaCipher rsaEncrypt;

    @Resource(name = "signature.sign")
    private SignVerify signVerify;

    @Autowired
    private ConfigModel configModel;


    @SneakyThrows
    private Aes createAes(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        String resContentType = null;
        if(response.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE)){
            resContentType = response.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0);
        }
        if(ValueUtils.isBlank(resContentType)){
            if(request.getHeaders().containsKey(HttpHeaders.ACCEPT)){
                resContentType = request.getHeaders().get(HttpHeaders.ACCEPT).get(0);
            }
        }
        if(ValueUtils.isNotBlank(resContentType)){
            if(!resContentType.startsWith(Common.ENCRYPT_CONTENT_TYPE_HEAD_TAG)){
                resContentType = Common.ENCRYPT_CONTENT_TYPE_HEAD_TAG + resContentType + ";charset=" + this.configModel.getCharsetName();
                response.getHeaders().set(HttpHeaders.CONTENT_TYPE, resContentType);
            }
        }
        String key = StringUtils.getUUID32();
        String iv = StringUtils.getUUID32().substring(0, 16);
        String dSalt = key + "," + iv;
        String eSalt = new String(DataUtils.base64Encode(this.rsaEncrypt.encrypt(dSalt.getBytes(StandardCharsets.UTF_8))));
        response.getHeaders().set(Common.HEADER_SALT_KEY, eSalt);
        String[] keys = dSalt.split(",");
        return Aes.create().setKey(keys[0]).setIV(keys[1]).load();
    }

    @Override
    public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] bytes) {
        try {
//            log.info("response:{}", new String(bytes));
//            Map respBody = objectMapper.readValue(bytes, Map.class);
//            respBody.put("respChange", "test");
//            return Mono.just(objectMapper.writeValueAsBytes(respBody));

            Aes aes = this.createAes(exchange);
            String sign = null;
            if(!ValueUtils.isBlank(bytes) && aes != null){
                sign = new String(DataUtils.base64Encode(signVerify.sign(bytes)));
            }
            if(ValueUtils.isNotBlank(sign)){
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().set(Common.HEADER_SIGN_KEY, sign);
            }

            return Mono.just(aes.encrypt(bytes));
        } catch (Exception ex) {
            return Mono.error(new BusinessException(ex));
        }
    }
}
