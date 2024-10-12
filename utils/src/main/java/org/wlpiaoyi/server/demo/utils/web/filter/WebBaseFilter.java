package org.wlpiaoyi.server.demo.utils.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.wlpiaoyi.framework.utils.MapUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.response.ResponseUtils;
import org.wlpiaoyi.server.demo.utils.web.support.WebSupport;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;
import org.wlpiaoyi.server.demo.utils.web.domain.DoFilterEnum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-09 09:24:00</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
@Order(Integer.MIN_VALUE)
public abstract class WebBaseFilter implements Filter{


    protected abstract List<WebSupport> getWebSupports();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(!(servletRequest instanceof HttpServletRequest)){
            filterChain.doFilter(servletRequest, servletResponse);
            log.error("Filter.doFilter unknown type({}) for request", servletRequest.getClass().getName());
            return;
        }
        if(!(servletResponse instanceof HttpServletResponse)) {
            filterChain.doFilter(servletRequest, servletResponse);
            log.error("Filter.doFilter unknown type({}) for response", servletResponse.getClass().getName());
            return;
        }
        List<WebSupport> webSupports = this.getWebSupports();
        int undoChain = 0;
        int closeReq = 0;
        int closeResp = 0;
        int goNext = -1;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        List<WebSupport> afterDos = new ArrayList<>();
        Map obj = new HashMap<>();
        if(webSupports != null && !webSupports.isEmpty()){
            for (WebSupport webSupport : webSupports){
                if(!WebUtils.patternUri(webSupport.getRequestURI(request),webSupport.getURIRegexes())){
                    continue;
                }
                int res = DoFilterEnum.Unknown.getValue();
                try{
                    if(goNext == -1)
                        goNext = 0;
                    else if(!webSupport.shouldInDo(goNext | undoChain))
                        continue;
                    res = webSupport.doFilter(request,response, obj);
                    afterDos.add(webSupport);
                    request = MapUtils.get(obj, "request", request);
                    response = MapUtils.get(obj, "response", response);
                }catch (BusinessException e){
                    log.error("Web base filter do filter biz error", e);
                    ResponseUtils.writeResponseJson(
                            e.getCode(),
                            R.data(e.getCode(), e.getMessage()),
                            response
                    );
                    undoChain = 0;
                    closeReq = 0;
                    closeResp = 0;
                    goNext = 0;
                    res = DoFilterEnum.ErrorResp.getValue() | DoFilterEnum.UndoChain.getValue();
                    afterDos.clear();
                    break;
                }catch (Exception e){
                    log.error("Web base filter do filter unknown error", e);
                    ResponseUtils.writeResponseJson(
                            500,
                            R.data(500, e.getMessage()),
                            response
                    );
                    undoChain = 0;
                    closeReq = 0;
                    closeResp = 0;
                    goNext = 0;
                    res = DoFilterEnum.ErrorResp.getValue() | DoFilterEnum.UndoChain.getValue();
                    afterDos.clear();
                    break;
                }finally {
                    undoChain = undoChain | (res & DoFilterEnum.UndoChain.getValue());
                    closeResp = closeResp | (res & DoFilterEnum.CloseResp.getValue());
                    closeReq = closeReq | (res & DoFilterEnum.CloseReq.getValue());
                    goNext = goNext | (res & DoFilterEnum.GoNext.getValue());
                }
            }
        }
        if(closeResp == DoFilterEnum.CloseResp.getValue()){
            response.getOutputStream().close();
        }
        if(closeReq == DoFilterEnum.CloseReq.getValue()) {
            request.getInputStream().close();
        }
        if(undoChain != DoFilterEnum.UndoChain.getValue()){
            filterChain.doFilter(request, response);
            if(afterDos != null && !afterDos.isEmpty()){
                for (WebSupport webSupport : afterDos){
                    webSupport.afterDoFilter(servletRequest, servletResponse, obj);
                }
            }
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
