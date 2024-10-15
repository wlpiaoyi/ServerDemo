package org.wlpiaoyi.server.demo.utils.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.wlpiaoyi.framework.utils.MapUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.response.ResponseUtils;
import org.wlpiaoyi.server.demo.utils.response.ResponseWrapper;
import org.wlpiaoyi.server.demo.utils.web.support.WebSupport;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;
import org.wlpiaoyi.server.demo.utils.web.domain.DoFilterEnum;

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
                    switch (webSupport.isSupportExecResponse(request, response, obj)){
                        case 0: case 1:{
                            afterDos.add(webSupport);
                        }
                        break;
                    }
                    request = MapUtils.get(obj, "request", request);
                    response = MapUtils.get(obj, "response", response);
                }catch (Exception e){
                    log.error("Web base filter do filter unknown error", e);
                    this.doFilterError((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, request, response, webSupports, obj, e);
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
                int total = afterDos.size();
                int index = 0;
                if(afterDos != null && !afterDos.isEmpty()){
                    for (WebSupport webSupport : afterDos){
                        webSupport.execResponse(servletRequest, servletResponse, obj, index++, total);
                    }
                }
            }catch (Exception e){
                this.doFilterError((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, request, response, webSupports, obj, e);
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
                                 List<WebSupport> webSupports, Map obj, Exception e){

        int code;
        if(e instanceof BusinessException){
            code = ((BusinessException) e).getCode();
        }else{
            code = 500;
        }
        R r = R.data(code, e.getMessage());
        boolean hasFinalSupport = false;
        for (WebSupport temp : webSupports){
            if(temp.isSupportExecResponse(request, response, obj) == 1){
                if(!WebUtils.patternUri(temp.getRequestURI(request),temp.getURIRegexes())){
                    continue;
                }
                ResponseWrapper respWrapper = new ResponseWrapper(response);
                respWrapper.writeBuffer(GsonBuilder.gsonDefault().toJson(r).getBytes(StandardCharsets.UTF_8));
                obj.put("response", respWrapper);
                obj.put("encrypt_tag", true);
                ResponseUtils.prepareHeader(servletRequest, servletResponse);
                temp.execResponse(servletRequest, servletResponse, obj, 0, 1);
                break;
            }
        }
        if(!hasFinalSupport){
            ResponseUtils.prepareHeader(servletRequest, servletResponse);
            ResponseUtils.writeResponseData(code, r, servletResponse);
        }
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
