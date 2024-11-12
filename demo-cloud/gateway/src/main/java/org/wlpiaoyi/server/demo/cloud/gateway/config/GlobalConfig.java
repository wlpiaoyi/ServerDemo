package org.wlpiaoyi.server.demo.cloud.gateway.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.framework.utils.security.SignVerify;

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

    @Value("${wlpiaoyi.ee.cors.data.patterns.decrypt}")
    private String[] decryptPatterns;

    @Value("${wlpiaoyi.ee.cors.data.patterns.encrypt}")
    private String[] encryptPatterns;

    /**
     * <p><b>{@code @description:}</b>
     * 数据签名
     * </p>
     *
     * <p><b>@param</b> <b>privateKey</b>
     * {@link String}
     * </p>
     *
     * <p><b>@param</b> <b>publicKey</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/12 17:06</p>
     * <p><b>{@code @return:}</b>{@link SignVerify}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @SneakyThrows
    @Bean("signature.sign")
    public SignVerify initSign(@Value("${wlpiaoyi.ee.sign.privateKey}") String privateKey, @Value("${wlpiaoyi.ee.sign.publicKey}") String publicKey){
        return SignVerify.build().setPrivateKey(privateKey).setPublicKey(publicKey).loadConfig();
    }

    /**
     * <p><b>{@code @description:}</b>
     * 数据加密
     * </p>
     *
     * <p><b>@param</b> <b>privateKey</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/12 17:07</p>
     * <p><b>{@code @return:}</b>{@link RsaCipher}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @SneakyThrows
    @Bean("encrypt.rsae")
    public RsaCipher initRsaEncrypt(@Value("${wlpiaoyi.ee.rsa.privateKey}") String privateKey) {
        return RsaCipher.build(0).setPrivateKey(privateKey).loadConfig();
    }

    /**
     * <p><b>{@code @description:}</b>
     * 数据解密
     * </p>
     *
     * <p><b>@param</b> <b>privateKey</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/12 17:23</p>
     * <p><b>{@code @return:}</b>{@link RsaCipher}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    @SneakyThrows
    @Bean("encrypt.rsad")
    public RsaCipher initRsaDecrypt(@Value("${wlpiaoyi.ee.rsa.privateKey}") String privateKey) {
        return RsaCipher.build(1).setPrivateKey(privateKey).loadConfig();
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
        return new RequestBodyFilter(modifyRequestBody, bodyRewrite, this.decryptPatterns);
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
        return new ResponseBodyFilter(modifyResponseBody, bodyRewrite, this.encryptPatterns);
    }
}
