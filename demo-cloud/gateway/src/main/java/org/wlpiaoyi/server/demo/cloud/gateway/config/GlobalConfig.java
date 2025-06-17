package org.wlpiaoyi.server.demo.cloud.gateway.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.framework.utils.security.SignVerify;
import org.wlpiaoyi.server.demo.common.tools.web.model.ConfigModel;

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

    @Autowired
    private ConfigModel configModel;

    /**
     * <p><b>{@code @description:}</b>
     * 数据签名
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/12 17:06</p>
     * <p><b>{@code @return:}</b>{@link SignVerify}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @SneakyThrows
    @Bean("signature.sign")
    public SignVerify initSign(){
        return SignVerify.build()
                .setPrivateKey(configModel.getSignPrivateKey())
                .setPublicKey(configModel.getSignPublicKey())
                .loadConfig();
    }

    /**
     * <p><b>{@code @description:}</b>
     * 数据加密
     * </p>
     *
     * <p><b>@param</b> <b>configModel</b>
     * {@link ConfigModel}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/12 17:07</p>
     * <p><b>{@code @return:}</b>{@link RsaCipher}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @SneakyThrows
    @Bean("encrypt.rsae")
    public RsaCipher initRsaEncrypt() {
        return RsaCipher.build(0).setPrivateKey(configModel.getRsaPrivateKey()).loadConfig();
    }

    /**
     * <p><b>{@code @description:}</b>
     * 数据解密
     * </p>
     *
     * <p><b>@param</b> <b>configModel</b>
     * {@link ConfigModel}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/12 17:23</p>
     * <p><b>{@code @return:}</b>{@link RsaCipher}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @SneakyThrows
    @Bean("encrypt.rsad")
    public RsaCipher initRsaDecrypt() {
        return RsaCipher.build(1).setPrivateKey(configModel.getRsaPrivateKey()).loadConfig();
    }


    /**
     * <p><b>{@code @description:}</b>
     * 认证
     * </p>
     *
     * <p><b>@param</b> <b>modifyRequestBody</b>
     * {@link ModifyRequestBodyGatewayFilterFactory}
     * </p>
     *
     * <p><b>@param</b> <b>bodyRewrite</b>
     * {@link AuthResponseRewrite}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/13 20:54</p>
     * <p><b>{@code @return:}</b>{@link AuthResponseFilter}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @Bean
    @Order(Common.AUTH_FILTER_ORDER)
    public AuthResponseFilter authResponseFilter(ModifyRequestBodyGatewayFilterFactory modifyRequestBody, AuthResponseRewrite bodyRewrite) {
        return new AuthResponseFilter(modifyRequestBody, bodyRewrite);
    }

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
    * {@link DataRequestRewrite}
    * </p>
    *
    * <p><b>{@code @date:}</b>2024/11/8 13:01</p>
    * <p><b>{@code @return:}</b>{@link GlobalFilter}</p>
    * <p><b>{@code @author:}</b>wlpiaoyi</p>
    */
    @Bean
    @Order(Common.BODY_REQ_FILTER_ORDER)   //指定顺序必须在之前
    public DataRequestFilter dataRequestFilter(ModifyRequestBodyGatewayFilterFactory modifyRequestBody, DataRequestRewrite bodyRewrite) {
        return new DataRequestFilter(modifyRequestBody, bodyRewrite);
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
     * {@link DataResponseRewrite}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/8 13:01</p>
     * <p><b>{@code @return:}</b>{@link DataResponseFilter}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @Bean
    @Order(Common.BODY_RESP_FILTER_ORDER)   //指定顺序必须在之前
    public DataResponseFilter dataResponseFilter(ModifyResponseBodyGatewayFilterFactory modifyResponseBody, DataResponseRewrite bodyRewrite) {
        return new DataResponseFilter(modifyResponseBody, bodyRewrite);
    }
}
