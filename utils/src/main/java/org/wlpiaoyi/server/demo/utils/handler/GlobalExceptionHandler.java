package org.wlpiaoyi.server.demo.utils.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.exception.CatchException;
import org.wlpiaoyi.framework.utils.exception.SystemException;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.response.ResponseUtils;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * <p<b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b> 异常处理</p>
 * <p><b>{@code @date:}</b>2024/9/26 17:32</p>
 * <p><b>{@code @version:}</b>1.0</p>
 */
@Slf4j
//@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * <p><b>{@code @description:}</b>
     * 日志打印
     * </p>
     *
     * <p><b>@param</b> <b>code</b>
     * {@link int}
     * </p>
     *
     * <p><b>@param</b> <b>r</b>
     * {@link R}
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link Exception}
     * </p>
     *
     * <p><b>{@code @date:}</b>2023/9/15 12:50</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected void printErrorLog(int code, R r,
                                 HttpServletRequest req,
                                 HttpServletResponse resp,
                                 Exception exception) {
        StringBuffer rqrpInfoSb = new StringBuffer();
        rqrpInfoSb.append("Request:");
        rqrpInfoSb.append("\n\t");
        rqrpInfoSb.append("URI:");
        rqrpInfoSb.append(req.getRequestURI());
        rqrpInfoSb.append("\n\t");
        rqrpInfoSb.append("Method:");
        rqrpInfoSb.append(req.getMethod());
        rqrpInfoSb.append("\n\t");
        rqrpInfoSb.append("ContentType:");
        rqrpInfoSb.append(req.getContentType());
        rqrpInfoSb.append("\n\t");
        rqrpInfoSb.append("Headers:");
        Enumeration<String> reqHeaderNames = req.getHeaderNames();
        while (reqHeaderNames.hasMoreElements()){
            String headerName = reqHeaderNames.nextElement();
            String headerValue = req.getHeader(headerName);
            if(ValueUtils.isBlank(headerValue)){
                continue;
            }
            rqrpInfoSb.append("\n\t\t");
            rqrpInfoSb.append(headerName);
            rqrpInfoSb.append(":");
            rqrpInfoSb.append(headerValue);
        }
        rqrpInfoSb.append("\n");
        rqrpInfoSb.append("Response:");
        rqrpInfoSb.append("\n\t");
        rqrpInfoSb.append("Code:");
        rqrpInfoSb.append(code);
        rqrpInfoSb.append("\n\t");
        rqrpInfoSb.append("Headers:");
        Collection<String> respHeaderNames = resp.getHeaderNames();
        for (String headerName : respHeaderNames){
            String headerValue = req.getHeader(headerName);
            if(ValueUtils.isBlank(headerValue)){
                continue;
            }
            rqrpInfoSb.append("\n\t\t");
            rqrpInfoSb.append(headerName);
            rqrpInfoSb.append(":");
            rqrpInfoSb.append(headerValue);
        }
        rqrpInfoSb.append("\n");
        rqrpInfoSb.append("ClientInfo:");
        rqrpInfoSb.append("\n\tRemoteHost:");
        rqrpInfoSb.append(req.getRemoteHost());
        rqrpInfoSb.append("\n\tRemoteAddress:");
        rqrpInfoSb.append(req.getRemoteAddr());
        rqrpInfoSb.append("\n\tRemotePort:");
        rqrpInfoSb.append(req.getRemotePort());
        log.error("Request hashcode:{}, Response hashCode:{}, error: \n{}" , req.hashCode(), resp.hashCode(), rqrpInfoSb, exception);
    }

    protected void doResponse(int code, R<?> r,
                              HttpServletRequest req,
                              HttpServletResponse resp,
                              Exception exception) throws IOException {
        try{
            ResponseUtils.prepareHeader(req, resp);
            ResponseUtils.writeResponseJson(code, r, resp);
        }finally {
            printErrorLog(code, r, req, resp, exception);
        }
    }


    /**
     * <p><b>{@code @description:}</b>
     * 业务异常处理
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link BusinessException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2023/9/15 12:50</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(value = BusinessException.class)
    public void businessHandler(HttpServletRequest req, HttpServletResponse resp, BusinessException exception) throws IOException {
        String message = exception.getMessage();
        R<Object> r = R.data(exception.getCode(), null, message);
        doResponse(200, r, req, resp, exception);
    }

    /**
     * <p><b>{@code @description:}</b>
     * 系统异常处理
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link SystemException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:33</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(value = SystemException.class)
    public void systemHandler(HttpServletRequest req, HttpServletResponse resp, SystemException exception) throws IOException {
        String message = exception.getMessage();
        R<Object> r = R.data(exception.getCode(), null, message);
        doResponse(exception.getCode(), r, req, resp, exception);
    }

    /**
     * <p><b>{@code @description:}</b>
     * 请求参数缺失
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link MissingServletRequestParameterException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:35</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public void missingServletRequestParameterExceptionHandler(HttpServletRequest req, HttpServletResponse resp, MissingServletRequestParameterException exception) throws IOException {
        String message = String.format("请求参数缺失:%s", exception.getParameterName());
        R<Object> r = R.data(400, null, message);
        doResponse(r.getCode(), r, req, resp, exception);
    }

    /**
     * <p><b>{@code @description:}</b>
     * 请求参数类型错误
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link MethodArgumentTypeMismatchException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:35</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void methodArgumentTypeMismatchExceptionHandler(HttpServletRequest req, HttpServletResponse resp, MethodArgumentTypeMismatchException exception) throws IOException {
        String message = String.format(String.format("请求参数类型错误:%s", exception.getMessage()));
        R<Object> r = R.data(400, null, message);
        doResponse(r.getCode(), r, req, resp, exception);
    }

    /**
     * <p><b>{@code @description:}</b>
     * 参数校验不正确
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link MethodArgumentNotValidException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:36</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void methodArgumentNotValidExceptionExceptionHandler(HttpServletRequest req, HttpServletResponse resp, MethodArgumentNotValidException exception) throws IOException {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message;
        if(fieldError == null){
            message = "请求参数未知异常";
        }else{
            message = String.format("请求参数不正确:%s", fieldError.getDefaultMessage());
        }
        R<Object> r = R.data(400, null, message);
        doResponse(r.getCode(), r, req, resp, exception);
    }


    /**
     * <p><b>{@code @description:}</b>
     * 参数绑定不正确，本质上也是通过 Validator 校验
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link BindException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:36</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(BindException.class)
    public void bindExceptionHandler(HttpServletRequest req, HttpServletResponse resp, BindException exception) throws IOException {
        FieldError fieldError = exception.getFieldError();
        String message;
        if(fieldError == null){
            message = "请求参数未知异常";
        }else{
            message = String.format("请求参数异常:%s", fieldError.getDefaultMessage());
        }
        R<Object> r = R.data(400, null, message);
        doResponse(r.getCode(), r, req, resp, exception);
    }

    /**
     * <p><b>{@code @description:}</b>
     * 处理 Validator 校验不通过产生的异常
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link ConstraintViolationException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:38</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationExceptionHandler(HttpServletRequest req, HttpServletResponse resp, ConstraintViolationException exception) throws IOException {
        Iterator<ConstraintViolation<?>> iterator = exception.getConstraintViolations().iterator();
        StringBuilder message = new StringBuilder();
        while (iterator.hasNext()){
            ConstraintViolation<?> constraintViolation = iterator.next();
            message.append("\r\n").append(constraintViolation.getMessage());
        }

        message.insert(0, "请求参数不正确:");
        R<Object> r = R.data(400, null, message.toString());
        doResponse(r.getCode(), r, req, resp, exception);
    }

    /**
     * <p><b>{@code @description:}</b>
     * 处理 Dubbo Consumer 本地参数校验时，抛出的 ValidationException 异常
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link ValidationException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:39</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(ValidationException.class)
    public void validationException(HttpServletRequest req, HttpServletResponse resp, ValidationException exception) throws IOException {
        String message = "请求参数不正确";
        R<Object> r = R.data(400, null, message);
        doResponse(r.getCode(), r, req, resp, exception);
    }

    /**
     * <p><b>{@code @description:}</b>
     * 处理 Spring Security 权限不足的异常
     * 来源是，使用 @PreAuthorize 注解，AOP 进行权限拦截
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link AccessDeniedException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:39</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public void accessDeniedExceptionHandler(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException exception) throws IOException {
        String message = "没有该操作权限";
        R<Object> r = R.data(403, null, message);
        doResponse(r.getCode(), r, req, resp, exception);
    }

    /**
     */
    /**
     * <p><b>{@code @description:}</b>
     * 请求地址不存在
     *
     * 注意，它需要设置如下两个配置项：
     * 1. spring.throw-exception-if-no-handler-found 为 true
     * 2. spring.static-path-pattern 为 /statics/**
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link NoHandlerFoundException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:39</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public void noHandlerFoundExceptionHandler(HttpServletRequest req, HttpServletResponse resp, NoHandlerFoundException exception) throws IOException {
        String message = String.format("请求地址不存在:%s", exception.getRequestURL());
        R<Object> r = R.data(404, null, message);
        doResponse(r.getCode(), r, req, resp, exception);
    }

    /**
     * <p><b>{@code @description:}</b>
     * 请求方法不正确
     *
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link HttpRequestMethodNotSupportedException}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:40</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest req, HttpServletResponse resp, HttpRequestMethodNotSupportedException exception) throws IOException {
        String message = String.format("请求方法不正确:%s", exception.getMessage());
        R<Object> r = R.data(405, null, message);
        doResponse(r.getCode(), r, req, resp, exception);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public void httpMediaTypeNotSupportedExceptionHandler(HttpServletRequest req, HttpServletResponse resp, HttpRequestMethodNotSupportedException exception) throws IOException {
        String message = String.format("请求数据类型异常:%s", exception.getMessage());
        R<Object> r = R.data(406, null, message);
        doResponse(r.getCode(), r, req, resp, exception);
    }



    /**
     * <p><b>{@code @description:}</b>
     * 系统异常处理
     * </p>
     *
     * <p><b>@param</b> <b>req</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>resp</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>exception</b>
     * {@link Exception}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/9/26 17:40</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @ExceptionHandler(value = Exception.class)
    public void defaultErrorHandler(HttpServletRequest req, HttpServletResponse resp, Exception exception) throws IOException {
        String message;
        int code;
        if (exception instanceof BusinessException) {
            if(((BusinessException) exception).getCode() == 0){
                code = 501;
            }else{
                code = ((BusinessException) exception).getCode();
            }
            doResponse(200, R.data(code, exception.getMessage()), req, resp, exception);
            return;
        }
        if (exception instanceof SystemException) {
            if(((SystemException) exception).getCode() == 0){
                code = 401;
            }else{
                code = ((SystemException) exception).getCode();
            }
            doResponse(code, R.data(code, exception.getMessage()), req, resp, exception);
            return;
        }
        if (exception instanceof CatchException) {
            if(((CatchException) exception).getCode() == 0){
                code = 401;
            }else{
                code = ((CatchException) exception).getCode();
            }
            doResponse(code, R.data(code, exception.getMessage()), req, resp, exception);
            return;
        }
        Object[] res = httpErrorHandler(req, resp, exception);
        if(res != null){
            code = (int) res[0];
            message = res[1].toString();
            doResponse(code, R.data(code, message), req, resp, exception);
            return;
        }
        res = expandErrorHandler(req, resp, exception);
        if(res != null){
            code = (int) res[0];
            message = res[1].toString();
            doResponse(code, R.data(code, message), req, resp, exception);
            return;
        }
        code = 500;
        message = "服务器错误[500]";
        doResponse(code, R.data(code, message), req, resp, exception);
    }

    protected Object[] httpErrorHandler(HttpServletRequest req, HttpServletResponse resp, Exception exception) {
        int code = 0;
        String message = null;
        if (exception instanceof HttpMessageNotReadableException) {
            code = 400;
            message = "参数序列化异常:" + exception.getMessage();
        } else if (exception instanceof NoHandlerFoundException) {
            code = 404;
            message = "没有找到接口";
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            code = 405;
            message = "不支持的方法:" + exception.getMessage();
        } else if (exception instanceof MissingServletRequestParameterException) {
            code = 412;
            message = "参数错误:" + exception.getMessage();
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            code = 413;
            message = "参数错误:" + exception.getMessage();
        }else if (exception instanceof MethodArgumentNotValidException) {
            BindingResult br = ((MethodArgumentNotValidException)exception).getBindingResult();
            StringBuilder errorMsg = new StringBuilder();
            if (br.hasErrors()) {
                br.getAllErrors().forEach(error -> {
                    errorMsg.append(error.getDefaultMessage()).append("\n");
                });
            }
            code = 500;
            message = errorMsg.toString();
        }
        if(message == null){
            return null;
        }
        return new Object[]{code, message};
    }

    protected Object[] expandErrorHandler(HttpServletRequest req, HttpServletResponse resp, Exception exception){
        int code = 0;
        String message = null;
        if (exception instanceof BadSqlGrammarException) {
            code = 500;
            message = "服务器错误[sql error]:";
        }
        if(message == null){
            return null;
        }
        return new Object[]{code, message};
    }
}

