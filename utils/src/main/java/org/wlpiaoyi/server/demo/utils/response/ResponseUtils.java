package org.wlpiaoyi.server.demo.utils.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
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

    public static void writeResponseData(int code, @Nullable Object data,
                                         @NonNull HttpServletResponse response) throws IOException {
        response.setStatus(code);
        if(data instanceof byte[]){
            byte[] buffer = (byte[]) data;
            response.setContentLength(buffer.length);
            OutputStream out = response.getOutputStream();
            out.write(buffer);
            out.flush();
            out.close();
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
                    repStr = GsonBuilder.gsonDefault().toJson(data);
                }
            }else{
                repStr = "";
            }
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(repStr.getBytes(StandardCharsets.UTF_8).length));
            response.getWriter().write(repStr);
        }
//
    }
}
