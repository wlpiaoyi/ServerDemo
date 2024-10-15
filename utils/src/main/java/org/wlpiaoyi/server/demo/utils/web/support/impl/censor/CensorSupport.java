package org.wlpiaoyi.server.demo.utils.web.support.impl.censor;

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
import java.util.Map;

@Slf4j
public abstract class CensorSupport implements WebSupport<HttpServletRequest, HttpServletResponse> {



    @Override
    public String getRequestURI(HttpServletRequest servletRequest) {
        return servletRequest.getRequestURI();
    }

    /**
     * <p><b>{@code @description:}</b>
     * 令牌审查
     * </p>
     *
     * <p><b>@param</b> <b>servletRequest</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>@param</b> <b>servletResponse</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/9 11:22</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract boolean censor(String token);

    @Override
    public int doFilter(HttpServletRequest request, HttpServletResponse response, Map obj) throws ServletException, IOException {
        String token = request.getHeader(WebUtils.HEADER_TOKEN_KEY);
        if(ValueUtils.isBlank(token)){
            return DoFilterEnum.CloseReq.getValue() | DoFilterEnum.CloseResp.getValue() | DoFilterEnum.UndoChain.getValue();
        }
        if(!this.censor(token)){
            throw new BusinessException(402, "token censor failed");
        }
        return DoFilterEnum.Unknown.getValue();
    }
}
