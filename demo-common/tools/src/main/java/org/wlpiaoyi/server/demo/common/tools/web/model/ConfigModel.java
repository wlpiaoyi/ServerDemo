package org.wlpiaoyi.server.demo.common.tools.web.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 配置信息
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/15 20:47
 * {@code @version:}:       1.0
 */
@Getter
@Component
public class ConfigModel {

    // Environment evn
    public ConfigModel(){
        this.workerId       = SpringUtils.resolve("wlpiaoyi.ee.snowflake.workerId", Byte.class, (byte)1);
        this.datacenterId   = SpringUtils.resolve("wlpiaoyi.ee.snowflake.datacenterId", Byte.class, (byte)1);
        this.timerEpoch     = SpringUtils.resolve("wlpiaoyi.ee.snowflake.timerEpoch", Long.class, 1729087588173L);

        this.charsetName    = Charset.forName(SpringUtils.resolve("wlpiaoyi.ee.charsetName", String.class, StandardCharsets.UTF_8.name()));
        this.rsaPrivateKey  = SpringUtils.resolve("wlpiaoyi.ee.rsa.privateKey", String.class, null);
        this.rsaPublicKey   = SpringUtils.resolve("wlpiaoyi.ee.rsa.publicKey", String.class, null);
        this.signPrivateKey = SpringUtils.resolve("wlpiaoyi.ee.sign.privateKey", String.class, null);
        this.signPublicKey  = SpringUtils.resolve("wlpiaoyi.ee.sign.publicKey", String.class, null);

        this.decryptPatterns        = SpringUtils.resolve("wlpiaoyi.ee.cors.data.patterns.decrypt", "").split(", ");
        this.encryptPatterns        = SpringUtils.resolve("wlpiaoyi.ee.cors.data.patterns.encrypt", "").split(", ");
        this.authPatterns           = SpringUtils.resolve("wlpiaoyi.ee.cors.data.patterns.authentication", "").split(", ");
        this.exclusionCensorPatterns = SpringUtils.resolve("wlpiaoyi.ee.cors.data.patterns.exclusionCensor", "").split(", ");
    }

    /** 默认时区 */
    public static final String ZONE = "GMT+8";

    //雪花算法配置=============================>
    private final Byte workerId;
    private final Byte datacenterId;
    private final Long timerEpoch;
    //雪花算法配置=============================<

    /** 编码方式 */
    private final Charset charsetName;

    //密钥==================================>
    private final String rsaPrivateKey;
    private final String rsaPublicKey;
    private final String signPrivateKey;
    private final String signPublicKey;
    //密钥==================================<


    //加密解密认证==================================>
    private final String[] decryptPatterns;
    private final String[] encryptPatterns;
    //加密解密认证==================================<
    private final String[] authPatterns;
    private final String[] exclusionCensorPatterns;


}
