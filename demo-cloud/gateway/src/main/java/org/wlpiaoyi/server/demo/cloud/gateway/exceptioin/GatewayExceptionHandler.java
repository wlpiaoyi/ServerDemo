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
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex)
    {
        ServerHttpResponse response = exchange.getResponse();

        if (exchange.getResponse().isCommitted())
        {
            return Mono.error(ex);
        }

        String msg;
        int code = 500;
        if (ex instanceof NotFoundException)
        {
            msg = "服务未找到";
            code = 404;
        }
        else if (ex instanceof ResponseStatusException)
        {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            msg = responseStatusException.getMessage();
            code = 401;
        }
        else
        {
            msg = "内部服务器错误";
        }

        log.error("[网关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().getPath(), ex.getMessage());
        return webFluxResponseWriter(response, HttpStatus.valueOf(code), msg, code);
    }

    private static final Gson defaultGson = GsonBuilder.gsonDefault();

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param status http状态码
     * @param message 响应内容
     * @param code 响应状态码
     * @return Mono<Void>
     */
    private static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus status, String message, int code) {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Map res = new HashMap<>();
        res.put("code", code);
        res.put("message", message);
        DataBuffer dataBuffer = response.bufferFactory().wrap(defaultGson.toJson(res).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }


}