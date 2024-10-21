package org.wlpiaoyi.server.demo.config.web.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-09 18:00:22</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class AuthenticationSupport extends org.wlpiaoyi.server.demo.common.core.web.support.impl.auth.AuthenticationSupport {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${wlpiaoyi.ee.auth.duri_minutes}")
    private int authDuriMinutes;

    @Value("${wlpiaoyi.ee.cors.data.patterns.authentication}")
    private String[] patterns;

    @Override
    public String[] getURIRegexes() {
        return this.patterns;
//        return new String[]{"/test/auth/login"};
    }

    @Override
    protected void authSuccess(String token) {
        this.redisTemplate.opsForValue().set(token, "1", this.authDuriMinutes, TimeUnit.MINUTES);
    }
}
