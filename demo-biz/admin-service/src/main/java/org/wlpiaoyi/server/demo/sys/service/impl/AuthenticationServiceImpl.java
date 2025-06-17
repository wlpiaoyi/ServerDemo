package org.wlpiaoyi.server.demo.sys.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthUser;
import org.wlpiaoyi.server.demo.sys.service.AuthenticationService;

import java.util.HashMap;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>令牌认证</p>
 * <p><b>{@code @date:}</b>2025-02-01 15:45:15</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public String login(AuthUser user, String client) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        WebUtils.generateJwtToken(
//                new HashMap(){{
//
//                }},
//
//        )
        return "";
    }

    @Override
    public boolean logout(String token) {
        return false;
    }

    @Override
    public String refresh(String token) {
        return "";
    }
}
