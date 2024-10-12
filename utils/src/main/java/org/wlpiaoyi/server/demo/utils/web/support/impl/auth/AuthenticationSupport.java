package org.wlpiaoyi.server.demo.utils.web.support.impl.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.wlpiaoyi.framework.utils.MapUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.utils.request.RequestWrapper;
import org.wlpiaoyi.server.demo.utils.response.ResponseUtils;
import org.wlpiaoyi.server.demo.utils.response.ResponseWrapper;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;
import org.wlpiaoyi.server.demo.utils.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.utils.web.support.WebSupport;

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

    /**
     * <p><b>{@code @description:}</b> 
     * TODO
     * </p>
     * 
     * <p><b>@param</b> <b>request</b>
     * {@link HttpServletRequest}
     * </p>
     * 
     * <p><b>@param</b> <b>response</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/12 14:56</p>
     * <p><b>{@code @return:}</b>{@link String}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract String getSalt(HttpServletRequest request, HttpServletResponse response);

    @Override
    public int doFilter(HttpServletRequest request, HttpServletResponse response, Map obj) throws ServletException, IOException {
        String token = request.getHeader(WebUtils.HEADER_TOKEN_KEY);
        if(ValueUtils.isBlank(token)){
            return DoFilterEnum.CloseReq.getValue() | DoFilterEnum.CloseResp.getValue() | DoFilterEnum.UndoChain.getValue();
        }
        if(!this.authenticationToken(token)){
            throw new BusinessException(402, "token authentication failed");
        }
//        RequestWrapper reqWrapper = new RequestWrapper(request, 0);
        ResponseWrapper respWrapper = new ResponseWrapper(response);
//        obj.put("request", reqWrapper);
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
        return 0;
    }

    @Override
    public void execResponse(HttpServletRequest request, HttpServletResponse response, Map obj, int indexSupport, int totalSupports) {
        response.setHeader(WebUtils.HEADER_SALT_KEY, this.getSalt(request, response));
        if(indexSupport == totalSupports - 1){
            ResponseWrapper respWrapper = MapUtils.get(obj, "response");
            if(respWrapper != null){
                try {
                    ResponseUtils.prepareHeader(request, response);
                    this.responseBody(respWrapper, response);
                } catch (Exception e) {
                    try {
                        request.getInputStream().close();
                    } catch (Exception ex) {}
                    try {
                        response.getOutputStream().close();
                    } catch (Exception ex) {}
                    throw new BusinessException(e);
                }
            }
        }
    }
}
