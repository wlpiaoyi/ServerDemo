package org.wlpiaoyi.server.demo.config.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-09 18:00:22</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class AuthenticationSupport extends org.wlpiaoyi.server.demo.utils.web.support.impl.auth.AuthenticationSupport {

    @Override
    protected boolean authenticationToken(String token) {
        return token.startsWith("wl");
    }

    @Override
    protected String getSalt(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(WebUtils.HEADER_TOKEN_KEY);
        return "salt" + token;
    }

    @Value("${wlpiaoyi.ee.cors.data.patterns.authentication}")
    private String[] patterns;

    @Override
    public String[] getURIRegexes() {
        return this.patterns;
//        return new String[]{"/test/auth/login"};
    }
}
