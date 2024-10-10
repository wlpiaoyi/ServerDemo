package org.wlpiaoyi.server.demo.utils.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.jetbrains.annotations.Nullable;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author wlpia
 */
@Slf4j
public class ResponseUtils {

    private static final String CONTENT_TYPE_KEY = "content-type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json;charset=utf-8";
    public static void writeResponseJson(int code, @Nullable Object json,
                                         @NonNull HttpServletResponse response) throws IOException {
        response.setHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE_JSON);
        response.setContentType(CONTENT_TYPE_VALUE_JSON);
        ResponseUtils.writeResponseData(code, json, response);
    }

    public static void writeResponseData(int code, @Nullable Object data,
                                         @NonNull HttpServletResponse response) throws IOException {
        String repStr;
        if(data != null){
            if(data instanceof String){
                repStr = (String) data;
            }else if(data instanceof StringBuffer){
                repStr = ((StringBuffer) data).toString();
            }else if(data instanceof StringBuilder){
                repStr = ((StringBuilder) data).toString();
            }else{
                repStr = GsonBuilder.gsonDefault().toJson(data);
            }
        }else{
            repStr = "";
        }
        response.setStatus(code);
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(repStr.getBytes(StandardCharsets.UTF_8).length));
        response.getWriter().write(repStr);
    }
}
