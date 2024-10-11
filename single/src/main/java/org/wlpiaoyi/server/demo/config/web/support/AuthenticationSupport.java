package org.wlpiaoyi.server.demo.config.web.support;

import org.springframework.stereotype.Component;

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
    public String[] getURIRegexes() {
        return new String[]{"/test/auth/login"};
    }
}
