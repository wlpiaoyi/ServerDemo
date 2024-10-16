package org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.wlpiaoyi.server.demo.utils.web.annotation.Idempotence;
import org.wlpiaoyi.server.demo.utils.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.utils.web.support.WebSupport;

import java.util.Map;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-09 16:23:26</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
public abstract class IdempotenceSupport implements WebSupport<HttpServletRequest, HttpServletResponse> {

    public abstract String getIdempotenceKey(HttpServletRequest servletRequest);

    public abstract IdempotenceMoon getIdempotenceMoon();

    public abstract IdempotenceUriSet getIdempotenceUriSet();

    @Override
    public boolean shouldInDo(int res) {
        return true;
    }

    @Override
    public String getRequestURI(HttpServletRequest servletRequest) {
        return servletRequest.getRequestURI();
    }

    @Override
    public int doFilter(HttpServletRequest request, HttpServletResponse response, Map obj) throws Exception {
        String uri = this.getRequestURI(request);
        log.debug("IdempotenceFilter.doFilter uri:{}", uri);
        IdempotenceUriSet uriSet = this.getIdempotenceUriSet();
        //如果uriSet对象, 需要确定是否有@Idempotence注解
        if(uriSet == null || !uriSet.contains(uri)){
            return DoFilterEnum.GoNext.getValue();
        }
        Idempotence idempotence = uriSet.get(uri);
        IdempotenceMoon idempotenceMoon = this.getIdempotenceMoon();
        //出现高密度访问的处理逻辑
        if(idempotenceMoon.isIdempotence(getIdempotenceKey(request), idempotence.duriSecond(), idempotence.count(), idempotence.deadLockMinutes())){
            return DoFilterEnum.CloseReq.getValue() | DoFilterEnum.CloseResp.getValue() | DoFilterEnum.UndoChain.getValue();
        }
        return DoFilterEnum.GoNext.getValue();
    }
}
