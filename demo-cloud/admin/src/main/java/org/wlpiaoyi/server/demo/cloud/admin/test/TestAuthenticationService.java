package org.wlpiaoyi.server.demo.cloud.admin.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.server.demo.auth.impl.AuthenticationServiceImpl;
import org.wlpiaoyi.server.demo.common.redis.service.RedisService;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2025-06-19 14:37:49</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
@Service
public class TestAuthenticationService extends AuthenticationServiceImpl<AuthUserI, AuthRoleI> {

    private RedisService redisService;

    @Override
    protected RedisService getRedisServer() {
        if(this.redisService == null) this.redisService = SpringUtils.getBean(RedisService.class);
        return this.redisService;
    }
}
