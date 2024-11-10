package org.wlpiaoyi.server.demo.cloud.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 全局数据拦截配置
 * </p>
 * <p><b>{@code @date:}</b>2024-10-28 12:43:29</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Configuration
public class GlobalConfig {

   /**
    * <p><b>{@code @description:}</b>
    * 定义全局拦截器拦截请求体
    * </p>
    *
    * <p><b>{@code @param}</b> <b>modifyRequestBody</b>
    * {@link ModifyRequestBodyGatewayFilterFactory}
    * </p>
    *
    * <p><b>{@code @param}</b> <b>bodyRewrite</b>
    * {@link RequestRewrite}
    * </p>
    *
    * <p><b>{@code @date:}</b>2024/11/8 13:01</p>
    * <p><b>{@code @return:}</b>{@link GlobalFilter}</p>
    * <p><b>{@code @author:}</b>wlpiaoyi</p>
    */
    @Bean
    @Order(NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2)   //指定顺序必须在之前
    public GlobalFilter requestFilter(ModifyRequestBodyGatewayFilterFactory modifyRequestBody, RequestRewrite bodyRewrite) {
        return new RequestBodyFilter(modifyRequestBody, bodyRewrite);
    }
    /**
     * <p><b>{@code @description:}</b>
     * 定义全局拦截器拦截响应体
     * </p>
     *
     * <p><b>{@code @param}</b> <b>modifyResponseBody</b>
     * {@link ModifyResponseBodyGatewayFilterFactory}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>bodyRewrite</b>
     * {@link ResponseRewrite}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/8 13:01</p>
     * <p><b>{@code @return:}</b>{@link GlobalFilter}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @Bean
    @Order(NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2)   //指定顺序必须在之前
    public GlobalFilter responseFilter(ModifyResponseBodyGatewayFilterFactory modifyResponseBody, ResponseRewrite bodyRewrite) {
        return new ResponseBodyFilter(modifyResponseBody, bodyRewrite);
    }
}
