package org.wlpiaoyi.server.demo.config.web.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.framework.utils.PatternUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 10:07:34</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
@Component
public class CensorSupport extends org.wlpiaoyi.server.demo.utils.web.support.impl.censor.CensorSupport {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${wlpiaoyi.ee.auth.duri_minutes}")
    private int authDuriMinutes;

    @Override
    protected boolean censor(String token, String salt) {
        if(ValueUtils.isBlank(token)){
            return false;
        }
        String saltKey = WebUtils.HEADER_SALT_KEY + token;
        if(!this.redisTemplate.hasKey(token)){
            this.redisTemplate.delete(saltKey);
            return false;
        }
        String value = this.redisTemplate.opsForValue().get(saltKey);
        if(ValueUtils.isBlank(value)){
            return false;
        }
        int i = value.indexOf("#");
        if(i == -1){
            return false;
        }
        String preTimeMillisStr = value.substring(0, i);
        if(!PatternUtils.isNumber(preTimeMillisStr)){
            return false;
        }
        Long preTimeMillis = Long.valueOf(preTimeMillisStr);
        if(preTimeMillis + (this.authDuriMinutes * 60 * 1000) < System.currentTimeMillis()){
            return false;
        }
        value = value.substring(i + 1);
        if(!value.equals(salt)){
            return false;
        }
        log.info("request header(token:{}, salt:{})", token, salt);
        return true;
    }

    @Value("${wlpiaoyi.ee.cors.data.patterns.censor}")
    private String[] patterns;

    @Override
    public String[] getURIRegexes() {
        return this.patterns;
//        return new String[]{"/test/censor/.*|/test/common/.*","(^(?!/test/common/do).*$)"};
    }

}
