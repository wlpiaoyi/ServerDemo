package org.wlpiaoyi.server.demo.sys.auth.impl;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.common.redis.service.RedisService;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthRole;
import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthUser;
import org.wlpiaoyi.server.demo.sys.auth.AuthenticationService;

import java.util.concurrent.TimeUnit;

import static org.wlpiaoyi.server.demo.common.tools.web.domain.AuthUser.keyTag;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>令牌认证</p>
 * <p><b>{@code @date:}</b>2025-02-01 15:45:15</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
public abstract class AuthenticationServiceImpl<U extends AuthUser,R extends AuthRole>implements AuthenticationService<U>, SpringUtils.AuthDomainContext<U ,R> {


    protected abstract RedisService getRedisServer();

    private String getUserKeyTag(String token){
        if(ValueUtils.isBlank(token)){
            token = StpUtil.getTokenValue();
        }
        if(ValueUtils.isBlank(token)){
            throw new BusinessException("没有登录");
        }
        return keyTag + ":" + token;
    }

    @Override
    public String login(U user) {
        return this.login(user, null);
    }

    @Override
    public String login(U user, String deviceType) {
        if(ValueUtils.isBlank(deviceType)){
            StpUtil.login(user.getId());
        }else{
            StpUtil.login(user.getId(), deviceType);
        }
        this.getRedisServer().setCacheObject(getUserKeyTag(StpUtil.getTokenValue()), user, SaManager.getConfig().getTimeout(), TimeUnit.SECONDS);
        return StpUtil.getTokenValue();
    }

    @Override
    public void logout() {
        this.logout(null);
    }

    @Override
    public void logout(String token) {
        if(ValueUtils.isBlank(token)){
            this.getRedisServer().deleteObject(getUserKeyTag(StpUtil.getTokenValue()));
            StpUtil.logout();
        }else{
            this.getRedisServer().deleteObject(getUserKeyTag(token));
            StpUtil.logoutByTokenValue(token);
        }
    }

    public boolean isLogin(){
        return StpUtil.isLogin();
    }

    public boolean isLogin(String token){
        return StpUtil.isLogin(StpUtil.getLoginIdByToken(token));
    }

    @Override
    public boolean refresh() {
        return this.refresh(null);
    }

    @Override
    public boolean refresh(String token) {
        if(ValueUtils.isBlank(token)){
            token = StpUtil.getTokenValue();
        }
        if(ValueUtils.isBlank(token)){
            return false;
        }
        Object loginId = StpUtil.getLoginId();
        if(loginId == null){
            return false;
        }
        if(StpUtil.isLogin(loginId)){
            return false;
        }
        StpUtil.setTokenValue(token, (int) SaManager.getConfig().getTimeout());
        this.getRedisServer().deleteObject(keyTag + ":" + getUserKeyTag(token));
        return true;
    }

    @Override
    public U getSpringUtilsAuthUser() {
        return this.getRedisServer().getCacheObject(this.getUserKeyTag(null));
    }

    @Override
    public R getSpringUtilsAuthRole() {
        return null;
    }
}
