package org.wlpiaoyi.server.demo.utils.web.support.impl.encrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.wlpiaoyi.framework.utils.MapUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.server.demo.utils.request.RequestWrapper;
import org.wlpiaoyi.server.demo.utils.response.ResponseUtils;
import org.wlpiaoyi.server.demo.utils.response.ResponseWrapper;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;
import org.wlpiaoyi.server.demo.utils.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.utils.web.support.WebSupport;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
     * 非对称加密
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
     * <p><b>{@code @date:}</b>2024/10/12 15:11</p>
     * <p><b>{@code @return:}</b>{@link RsaCipher}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract RsaCipher getRsaEncrypt(HttpServletRequest request, HttpServletResponse response);

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
//        RequestWrapper reqWrapper = new RequestWrapper(request);
        ResponseWrapper respWrapper = new ResponseWrapper(response);
//        this.decryptRequestBody(reqWrapper, aes);
        obj.put("request", request);
        obj.put("response", respWrapper);
        //执行业务逻辑 交给下一个过滤器或servlet处理
//        filterChain.doFilter(reqWrapper, respWrapper);
//        this.encryptResponseBody(respWrapper, response, aes);
        return DoFilterEnum.GoNext.getValue();
    }

    @Override
    public int isSupportExecResponse(HttpServletRequest request, HttpServletResponse response, Map obj) {
        return 1;
    }

    @Override
    public void execResponse(HttpServletRequest request, HttpServletResponse response, Map obj, int indexSupport, int totalSupports) {
        if(indexSupport == totalSupports - 1){
            ResponseWrapper respWrapper = MapUtils.get(obj, "response");
            if(respWrapper != null){
                try {;
                    String salt = response.getHeader(WebUtils.HEADER_SALT_KEY);
                    if(ValueUtils.isNotBlank(salt)){
                        RsaCipher rsa = this.getRsaEncrypt(request, response);
                        salt = new String(DataUtils.base64Encode(rsa.encrypt(salt.getBytes(StandardCharsets.UTF_8))));
                        response.setHeader(WebUtils.HEADER_SALT_KEY, salt);
                    }
                    Aes aes = this.getAes(request, response);
                    String resContentType = respWrapper.getContentType();
                    if(ValueUtils.isBlank(resContentType)){
                        resContentType = request.getHeader(HttpHeaders.ACCEPT);
                        respWrapper.setContentType(resContentType);
                    }
                    ResponseUtils.prepareHeader(request, response);
                    this.encryptResponseBody(respWrapper, response, aes);
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
