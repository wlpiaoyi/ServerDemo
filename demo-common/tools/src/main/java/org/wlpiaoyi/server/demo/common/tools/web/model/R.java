package org.wlpiaoyi.server.demo.common.tools.web.model;

import lombok.Getter;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.exception.CatchException;
import org.wlpiaoyi.framework.utils.exception.SystemException;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/15 22:23
 * {@code @version:}:       1.0
 */
@Getter
public class R<T> {


    private int code;

    private T data;

    private String message;

    public static <T> R<T>  success(T data){
        return R.data(200, data, "SUCCESS");
    }

    public static <T> R<T>  success(T data, String message){
        return R.data(200, data, message);
    }

    public static <T> R<T>  failed(String message){
        return R.data(501, message);
    }

    public static <T> R<T>  failed(T data, String message){
        return R.data(501, data, message);
    }

    public static  R<Throwable> error(BusinessException exception){
        return R.data(200, exception.getCause(), exception.getMessage());
    }

    public static  R<Throwable> error(SystemException exception){
        return R.data(exception.getCode(), exception.getCause(), exception.getMessage());
    }

    public static  R<Throwable> error(CatchException exception){
        return R.data(exception.getCode(), exception.getCause(), exception.getMessage());
    }

    public static  R<Throwable> error(int code, Exception exception){
        return R.data(code, exception.getCause(), exception.getMessage());
    }

    public static <T> R<T> data(int code){
        R<T> r = new R<>();
        r.code = code;
        return r;
    }

    public static <T> R<T> data(int code, String message){
        R<T> r = new R<>();
        r.message = message;
        r.code = code;
        return r;
    }


    public static <T> R<T> data(int code, T data, String message){
        R<T> r = new R<>();
        r.message = message;
        r.data = data;
        r.code = code;
        return r;
    }
}
