package org.wlpiaoyi.server.demo.common.core.web.support.impl.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.wlpiaoyi.framework.utils.MapUtils;
import org.wlpiaoyi.framework.utils.StringUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;
import org.wlpiaoyi.server.demo.common.core.web.WebUtils;
import org.wlpiaoyi.server.demo.common.core.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.common.core.request.RequestWrapper;
import org.wlpiaoyi.server.demo.common.core.response.R;
import org.wlpiaoyi.server.demo.common.core.response.ResponseUtils;
import org.wlpiaoyi.server.demo.common.core.response.ResponseWrapper;
import org.wlpiaoyi.server.demo.common.core.web.support.WebSupport;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.Map;

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

    protected abstract void authSuccess(String token);

    @Override
    public int doFilter(HttpServletRequest request, HttpServletResponse response, Map obj) throws ServletException, IOException {
        RequestWrapper reqWrapper = new RequestWrapper(request, 0);
        String token = reqWrapper.getHeader(WebUtils.HEADER_TOKEN_KEY);
        if(ValueUtils.isBlank(token)){
            token = StringUtils.getUUID32() + StringUtils.getUUID32().substring(0,16);
        }
        reqWrapper.setHeader(WebUtils.HEADER_TOKEN_KEY, token);
        ResponseWrapper respWrapper = new ResponseWrapper(response);
        obj.put("request", reqWrapper);
        obj.put("response", respWrapper);
        return DoFilterEnum.Unknown.getValue();
    }

    protected void responseBody(ResponseWrapper respWrapper, HttpServletResponse response) throws IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] respData = respWrapper.getResponseData();
        for (String headerName : respWrapper.getHeaderNames()){
            response.setHeader(headerName, respWrapper.getHeader(headerName));
        }
        response.setStatus(respWrapper.getStatus());
        ResponseUtils.writeResponseData(response.getStatus(), respData, response);
    }


    @Override
    public int isSupportExecResponse(HttpServletRequest request, HttpServletResponse response, Map obj) {
        String uri = this.getRequestURI(request);
        if(WebUtils.patternUri(uri, this.getURIRegexes())){
            return 0;
        }else{
            return WebSupport.super.isSupportExecResponse(request, response, obj);
        }
    }

    @Override
    public void execResponse(HttpServletRequest request, HttpServletResponse response, Map obj, int indexSupport, int totalSupports) {
        RequestWrapper reqWrapper = MapUtils.get(obj, "request");
        ResponseWrapper respWrapper = MapUtils.get(obj, "response");
        String token = reqWrapper.getHeader(WebUtils.HEADER_TOKEN_KEY);
        response.setHeader(WebUtils.HEADER_TOKEN_KEY, null);
        if(respWrapper.getStatus() == 200 && ValueUtils.isNotBlank(token)){
            try{
                R r = GsonBuilder.gsonDefault().fromJson(new String(respWrapper.getResponseData()), R.class);
                if(r.getCode() == 200 ){
                    this.authSuccess(token);
                    response.setHeader(WebUtils.HEADER_TOKEN_KEY, token);
                }
            }catch (Exception e){}
        }
        if(indexSupport == totalSupports - 1){
            try {
                ResponseUtils.prepareHeader(reqWrapper, response);
                this.responseBody(respWrapper, response);
            } catch (Exception e) {
                throw new BusinessException(e);
            }finally {
                try {
                    reqWrapper.getInputStream().close();
                } catch (Exception ex) {}
                try {
                    respWrapper.getOutputStream().close();
                } catch (Exception ex) {}
            }
        }
    }
}
