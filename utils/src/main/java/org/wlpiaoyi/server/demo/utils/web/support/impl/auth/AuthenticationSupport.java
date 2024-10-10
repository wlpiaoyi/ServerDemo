package org.wlpiaoyi.server.demo.utils.web.support.impl.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;
import org.wlpiaoyi.server.demo.utils.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.utils.web.support.WebSupport;

import java.io.IOException;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-09 14:22:15</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
public abstract class AuthenticationSupport implements WebSupport<HttpServletRequest, HttpServletResponse> {

    @Override
    public String getRequestURI(HttpServletRequest servletRequest) {
        return servletRequest.getRequestURI();
    }
    /**
     * <p><b>{@code @description:}</b>
     * 令牌认证
     * </p>
     *
     * <p><b>@param</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/9 16:50</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract boolean authenticationToken(String token);

    @Override
    public int doFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader(WebUtils.headerTokenKey);
        if(ValueUtils.isBlank(token)){
            return DoFilterEnum.CloseReq.getValue() | DoFilterEnum.CloseResp.getValue() | DoFilterEnum.UndoChain.getValue();
        }
        if(!this.authenticationToken(token)){
            throw new BusinessException(402, "token authentication failed");
        }
        return DoFilterEnum.Unknown.getValue();
    }
}
