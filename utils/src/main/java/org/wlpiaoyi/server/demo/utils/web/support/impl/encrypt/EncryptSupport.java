package org.wlpiaoyi.server.demo.utils.web.support.impl.encrypt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.wlpiaoyi.framework.utils.MapUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.response.ResponseUtils;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;
import org.wlpiaoyi.server.demo.utils.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.utils.web.support.WebSupport;
import org.wlpiaoyi.server.demo.utils.web.support.impl.access.AccessUriSet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> 数据加解密</p>
 * <p><b>{@code @date:}</b> 2024-10-11 23:21:51</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
@Slf4j
public abstract class EncryptSupport implements WebSupport<HttpServletRequest, HttpServletResponse> {

    @Override
    public String getRequestURI(HttpServletRequest servletRequest) {
        return servletRequest.getRequestURI();
    }


    /**
     * <p><b>{@code @description:}</b> 
     * 获取对称加密
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
     * <p><b>{@code @date:}</b>2024/10/11 23:48</p>
     * <p><b>{@code @return:}</b>{@link Aes}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract Aes getAes(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * <p><b>{@code @description:}</b>
     * 解密 Request Body
     * </p>
     *
     * <p><b>@param</b> <b>reqWrapper</b>
     * {@link RequestWrapper}
     * </p>
     *
     * <p><b>@param</b> <b>aes</b>
     * {@link Aes}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/11 23:36</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected void decryptRequestBody(RequestWrapper reqWrapper, Aes aes) throws IllegalBlockSizeException, BadPaddingException {
        byte[] reqBody = reqWrapper.getBody();
        if(!ValueUtils.isBlank(reqBody)){
            //解密请求报文
            reqBody = aes.decrypt(reqBody);
        }
        reqWrapper.setBody(reqBody);

    }

    /**
     * <p><b>{@code @description:}</b>
     * 加密 Response Body
     * </p>
     *
     * <p><b>@param</b> <b>respWrapper</b>
     * {@link ResponseWrapper}
     * </p>
     *
     * <p><b>@param</b> <b>servletResponse</b>
     * {@link ServletResponse}
     * </p>
     *
     * <p><b>@param</b> <b>aes</b>
     * {@link Aes}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/11 23:51</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected void encryptResponseBody(ResponseWrapper respWrapper, HttpServletResponse response, Aes aes) throws IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] respData = respWrapper.getResponseData();
        if(!ValueUtils.isBlank(respData)){
            //加密响应报文
            respData = aes.encrypt(respData);
        }
        for (String headerName : respWrapper.getHeaderNames()){
            response.setHeader(headerName, respWrapper.getHeader(headerName));
        }
        String contentType = respWrapper.getContentType();
        if(!contentType.startsWith(WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG)){
            contentType = WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + contentType;
        }
        response.setContentType(contentType);
        ResponseUtils.writeResponseData(response.getStatus(), respData, response);
    }

    @Override
    public int doFilter(HttpServletRequest request, HttpServletResponse response, Map obj) throws BusinessException, IOException, IllegalBlockSizeException, BadPaddingException, ServletException {
        Aes aes = this.getAes(request, response);
        if(aes == null){
            log.error("EncryptFilter.doFilter unfunded aes object");
            return DoFilterEnum.GoNext.getValue();
        }

        //响应处理 包装响应对象 res 并缓存响应数据
        RequestWrapper reqWrapper = new RequestWrapper(request);
        ResponseWrapper respWrapper = new ResponseWrapper(response);
        this.decryptRequestBody(reqWrapper, aes);
        obj.put("request", reqWrapper);
        obj.put("response", respWrapper);
        obj.put("aes", aes);
        //执行业务逻辑 交给下一个过滤器或servlet处理
//        filterChain.doFilter(reqWrapper, respWrapper);
//        this.encryptResponseBody(respWrapper, response, aes);
        return DoFilterEnum.GoNext.getValue();
    }

    @Override
    public void afterDoFilter(HttpServletRequest request, HttpServletResponse response, Map obj) {
        try {
            Aes aes = MapUtils.get(obj, "aes");
            ResponseWrapper respWrapper = MapUtils.get(obj, "response");
            this.encryptResponseBody(respWrapper, response, aes);
        } catch (Exception e) {
            log.error("afterDoFilter error", e);
            try {
                request.getInputStream().close();
            } catch (Exception ex) {}
            try {
                response.getOutputStream().close();
            } catch (Exception ex) {}
        }
    }
}
