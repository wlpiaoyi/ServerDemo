package org.wlpiaoyi.server.demo.common.core.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.wlpiaoyi.framework.utils.MapUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;
import org.wlpiaoyi.server.demo.common.tools.web.model.R;
import org.wlpiaoyi.server.demo.common.core.response.ResponseUtils;
import org.wlpiaoyi.server.demo.common.core.response.ResponseWrapper;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.tools.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.common.core.web.support.WebSupport;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
public abstract class WebBaseFilter implements Filter {


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
        Map obj = new HashMap<>();
        List<WebSupport> afterDos = new ArrayList<>();
        WebSupport finalDo = null;
        for (WebSupport webSupport : webSupports){
            if(webSupport.isSupportExecResponse(request, response, obj) == 1){
                finalDo = webSupport;
                break;
            }
        }
        if(webSupports != null && !webSupports.isEmpty()){
            for (WebSupport webSupport : webSupports){
                if(!WebUtils.mathPath(webSupport.getRequestURI(request),webSupport.getURIRegexes())){
                    continue;
                }
                int res = DoFilterEnum.Unknown.getValue();
                try{
                    if(goNext == -1)
                        goNext = 0;
                    else if(!webSupport.shouldInDo(goNext | undoChain))
                        continue;
                    res = webSupport.doFilter(request,response, obj);
                    switch (webSupport.isSupportExecResponse(request, response, obj)){
                        case 0:{
                            afterDos.add(webSupport);
                        }
                        break;
                    }
                    request = MapUtils.get(obj, "request", request);
                    response = MapUtils.get(obj, "response", response);
                }catch (Exception e){
                    log.error("Web base filter do filter unknown error", e);
                    this.doFilterError(
                            (HttpServletRequest) servletRequest,
                            (HttpServletResponse) servletResponse,
                            request, response, finalDo, obj, e);
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
        if(undoChain != DoFilterEnum.UndoChain.getValue()){
            try{
                filterChain.doFilter(request, response);
                int total = afterDos.size() + (finalDo != null ? 1 : 0);
                int index = 0;
                if(afterDos != null && !afterDos.isEmpty()){
                    for (WebSupport webSupport : afterDos){
                        webSupport.execResponse(servletRequest, servletResponse, obj, index++, total);
                    }
                }
                if(finalDo != null){
                    finalDo.execResponse(servletRequest, servletResponse, obj, total - 1, total);
                }
            }catch (Exception e){
                this.doFilterError(
                        (HttpServletRequest) servletRequest,
                        (HttpServletResponse) servletResponse,
                        request, response, finalDo, obj, e);
            }
        }
        if(closeResp == DoFilterEnum.CloseResp.getValue()){
            response.getOutputStream().close();
        }
        if(closeReq == DoFilterEnum.CloseReq.getValue()) {
            request.getInputStream().close();
        }
    }

    @SneakyThrows
    protected void doFilterError(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                                 HttpServletRequest request, HttpServletResponse response,
                                 WebSupport finalDos, Map obj, Exception e){

        int code;
        if(e instanceof BusinessException){
            code = ((BusinessException) e).getCode();
        }else{
            code = 500;
        }
        R r = R.data(code, e.getMessage());

        if(finalDos == null || !WebUtils.mathPath(finalDos.getRequestURI(request),finalDos.getURIRegexes())){
            ResponseUtils.prepareHeader(servletRequest, servletResponse);
            ResponseUtils.writeResponseData(code, r, servletResponse);
            return;
        }
        ResponseWrapper respWrapper = new ResponseWrapper(response);
        respWrapper.writeBuffer(GsonBuilder.gsonDefault().toJson(r).getBytes(StandardCharsets.UTF_8));
        obj.put("response", respWrapper);
        obj.put("encrypt_tag", true);
        ResponseUtils.prepareHeader(servletRequest, servletResponse);
        finalDos.execResponse(servletRequest, servletResponse, obj, 0, 1);
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
