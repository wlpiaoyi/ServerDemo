package org.wlpiaoyi.server.demo.common.core.response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * @author wlpia
 */
@Slf4j
public class ResponseUtils {

    private static final String CONTENT_TYPE_VALUE_JSON = "application/json;charset=utf-8";
    public static void writeResponseJson(int code, @Nullable Object json,
                                         @NonNull HttpServletResponse response) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_VALUE_JSON);
        response.setContentType(CONTENT_TYPE_VALUE_JSON);
        ResponseUtils.writeResponseData(code, json, response);
    }

    public static void prepareHeader(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        String contentType = response.getHeader(HttpHeaders.CONTENT_TYPE);
        if(ValueUtils.isBlank(contentType)){
            contentType = request.getHeader(HttpHeaders.ACCEPT);
        }
        if(ValueUtils.isBlank(contentType)){
            contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        }
        if(ValueUtils.isBlank(contentType)){
            contentType = request.getContentType();
        }
        if(ValueUtils.isBlank(contentType)){
            contentType = ResponseUtils.CONTENT_TYPE_VALUE_JSON;
        }
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        response.setContentType(contentType);
    }

    public static void writeResponseData(int code, @Nullable Object data,
                                         @NonNull HttpServletResponse response) throws IOException {
        String contentType = response.getContentType();
        if(ValueUtils.isBlank(contentType)){
            contentType = response.getHeader(HttpHeaders.CONTENT_TYPE);
        }
        if(ValueUtils.isBlank(contentType)){
            throw new BusinessException("response content-type is null");
        }
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        response.setStatus(code);
        byte[] buffer;
        if(data instanceof byte[]){
            buffer = (byte[]) data;
        }else{
            String repStr;
            if(data != null){
                if(data instanceof String){
                    repStr = (String) data;
                }else if(
                        data instanceof StringBuffer
                                || data instanceof StringBuilder
                                || data instanceof Integer
                                || data instanceof Long
                                || data instanceof Boolean
                                || data instanceof Byte
                                || data instanceof Short
                                || data instanceof Float
                                || data instanceof Double
                                || data instanceof BigInteger
                                || data instanceof BigDecimal
                ){
                    repStr = data.toString();
                }else{
                    repStr = GsonBuilder.gsonDefault().toJsonTree(data).toString();
                }
            }else{
                repStr = "";
            }
            String encode = response.getCharacterEncoding();
            if(encode == null){
                encode = StandardCharsets.UTF_8.name();
            }
            buffer = repStr.getBytes(encode);
        }
        response.setContentLength(buffer.length);
        OutputStream out = response.getOutputStream();
        try {
            out.write(buffer);
            out.flush();
        }finally {
            out.close();
        }
//        if(data instanceof byte[]){
//            byte[] buffer = (byte[]) data;
//            response.setContentLength(buffer.length);
//            OutputStream out = response.getOutputStream();
//            out.write(buffer);
//            out.flush();
//            out.close();
//        }else{
//            String repStr;
//            if(data != null){
//                if(data instanceof String){
//                    repStr = (String) data;
//                }else if(
//                        data instanceof StringBuffer
//                        || data instanceof StringBuilder
//                        || data instanceof Integer
//                        || data instanceof Long
//                        || data instanceof Boolean
//                        || data instanceof Byte
//                        || data instanceof Short
//                        || data instanceof Float
//                        || data instanceof Double
//                        || data instanceof BigInteger
//                        || data instanceof BigDecimal
//                ){
//                    repStr = data.toString();
//                }else{
//                    repStr = GsonBuilder.gsonDefault().toJson(data);
//                }
//            }else{
//                repStr = "";
//            }
//            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(repStr.getBytes(StandardCharsets.UTF_8).length));
//            response.getWriter().write(repStr);
//        }
//
    }
}
