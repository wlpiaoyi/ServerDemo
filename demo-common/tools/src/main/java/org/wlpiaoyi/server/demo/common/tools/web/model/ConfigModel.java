package org.wlpiaoyi.server.demo.common.tools.web.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    public static final String ZONE = "GMT+8";
    @Value("${wlpiaoyi.ee.cors.data.snowflake.workerId}")
    private Byte workerId = 1;
    @Value("${wlpiaoyi.ee.cors.data.snowflake.datacenterId}")
    private Byte datacenterId = 1;
    @Value("${wlpiaoyi.ee.cors.data.snowflake.timerEpoch}")
    private Long timerEpoch = 1729087588173L;
    @Value("${wlpiaoyi.ee.cors.data.charset_name}")
    private String charsetName = StandardCharsets.UTF_8.name();

}
