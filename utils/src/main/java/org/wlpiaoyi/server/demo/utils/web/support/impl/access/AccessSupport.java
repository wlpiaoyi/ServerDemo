package org.wlpiaoyi.server.demo.utils.web.support.impl.access;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;
import org.wlpiaoyi.server.demo.utils.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.utils.web.domain.WebError;
import org.wlpiaoyi.server.demo.utils.web.support.WebSupport;

import java.util.Map;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 数据权限
 * </p>
 * <p><b>{@code @date:}</b>2024-10-10 13:51:59</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
public abstract class AccessSupport implements WebSupport<HttpServletRequest, HttpServletResponse> {

    protected abstract AccessUriSet getAccessUriSet();

    protected abstract boolean preAuthorize(String value, String token);

    @Override
    public String getRequestURI(HttpServletRequest servletRequest) {
        return servletRequest.getRequestURI();
    }

    @Override
    public boolean shouldInDo(int res) {
        return true;
    }

    @Override
    public int doFilter(HttpServletRequest request, HttpServletResponse response, Map obj) throws BusinessException {
        AccessUriSet accessUriSet = this.getAccessUriSet();
        if(accessUriSet == null){
            return DoFilterEnum.GoNext.getValue();
        }
        String uri = this.getRequestURI(request);
        String value = accessUriSet.get(uri);
        if(value == null || value.isEmpty()){
            return DoFilterEnum.GoNext.getValue();
        }
        String token = request.getHeader(WebUtils.HEADER_TOKEN_KEY);
        if(token == null || token.isEmpty()){
            return DoFilterEnum.CloseReq.getValue() | DoFilterEnum.CloseResp.getValue() | DoFilterEnum.UndoChain.getValue();
        }
        if(!this.preAuthorize(value, token)){
            throw new BusinessException(WebError.UnAccess);
        }
        return DoFilterEnum.Unknown.getValue();
    }
}
