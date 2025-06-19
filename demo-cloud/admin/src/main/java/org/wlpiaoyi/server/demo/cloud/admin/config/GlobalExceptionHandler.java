package org.wlpiaoyi.server.demo.cloud.admin.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wlpiaoyi.server.demo.common.tools.web.model.R;

import java.io.IOException;


/**
 * <p<b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b>2023/9/16 21:48</p>
 * <p><b>{@code @version:}</b>1.0</p>
 */
@Slf4j
@Order(1)
@ControllerAdvice
public class GlobalExceptionHandler extends org.wlpiaoyi.server.demo.common.core.handler.GlobalExceptionHandler {


    @Override
    protected void doResponse(int code, R<?> r, HttpServletRequest req, HttpServletResponse response, Exception exception) throws IOException {
        super.doResponse(code, r, req, response, exception);
    }


    /**
     * <p><b>{@code @description:}</b>
     * 值范围异常异常处理
     * </p>
     *
     * <p><b>{@code @param}</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>exception</b>
     * {@link IllegalArgumentException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2023/9/16 21:49</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public void illegalArgumentHandler(HttpServletRequest req, HttpServletResponse resp, IllegalArgumentException exception) throws IOException {
        String message = exception.getMessage();
        R<Object> r = R.data(413, null, message);
        doResponse(413, r, req, resp, exception);
    }


    public Object[] expandErrorHandler(HttpServletRequest req, HttpServletResponse resp, Exception exception){
        int code = 0;
        String message = null;
        if (exception instanceof org.springframework.jdbc.BadSqlGrammarException) {
            code = 500;
            message = "服务器错误[sql error]:";
        }
        if(message == null){
            return null;
        }
        return new Object[]{code, message};
    }
}
