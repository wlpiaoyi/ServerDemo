package org.wlpiaoyi.server.demo.cloud.gateway.config;

import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-11-13 20:10:54</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
public class Common {

    public static final int BODY_REQ_FILTER_ORDER = NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
    public static final int BODY_RESP_FILTER_ORDER = NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
    public static final int AUTH_FILTER_ORDER = NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;


    public static final String JWT_AUTH_ACCOUNT_KEY = "account";
    public static final String JWT_AUTH_TALENT_KEY = "talent";
    public static final String HEADER_TOKEN_KEY = "token";
    public static final String HEADER_SALT_KEY = "salt";
    public static final String HEADER_SIGN_KEY = "sign";
    public static final String ENCRYPT_CONTENT_TYPE_HEAD_TAG = "#ENCRYPT#";

}
