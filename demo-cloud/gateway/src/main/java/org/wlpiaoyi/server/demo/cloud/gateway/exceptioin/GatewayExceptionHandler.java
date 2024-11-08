package org.wlpiaoyi.server.demo.cloud.gateway.exceptioin;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 网关统一异常处理
 *
 * @author ruoyi
 */
@Slf4j
@Order(-200)
@Configuration
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted())
        {
            return Mono.error(ex);
        }

        String msg;
        int code;
        if (ex instanceof NotFoundException) {
            msg = "服务未找到";
            code = 404;
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            msg = responseStatusException.getMessage();
            code = 401;
        } else {
            msg = "内部服务器错误:" + ex.getMessage();
            code = 500;
        }

        log.error("[网关异常处理]请求路径:{},http状态码:{} 异常信息:{}", exchange.getRequest().getPath(), code, msg);
        return webFluxResponseWriter(exchange, HttpStatus.valueOf(code), msg, code);
    }

    private static final Gson defaultGson = GsonBuilder.gsonDefault();

    /**
     * <p><b>{@code @description:}</b>
     * 设置webflux模型响应
     * </p>
     *
     * <p><b>@param</b> <b>exchange</b>
     * {@link ServerWebExchange}
     * </p>
     *
     * <p><b>@param</b> <b>status</b>
     * {@link HttpStatus}
     * http状态码
     * </p>
     *
     * <p><b>@param</b> <b>message</b>
     * {@link String}
     * http状态码
     * </p>
     *
     * <p><b>@param</b> <b>code</b>
     * {@link int}
     * 响应状态码
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/8 13:15</p>
     * <p><b>{@code @return:}</b>{@link Mono< Void>}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    private static Mono<Void> webFluxResponseWriter(ServerWebExchange exchange, HttpStatus status, String message, int code) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Map respMap = new HashMap<>();
        respMap.put("code", code);
        respMap.put("message", message);
        byte[] respBytes = defaultGson.toJson(respMap).getBytes();
        DataBuffer dataBuffer = response.bufferFactory().wrap(respBytes);
        return response.writeWith(Mono.just(dataBuffer));
    }


}