package org.wlpiaoyi.server.demo.common.core.web.support.impl.encrypt;

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
import org.wlpiaoyi.framework.utils.security.SignVerify;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.tools.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.common.core.web.support.WebSupport;
import org.wlpiaoyi.server.demo.common.core.response.ResponseUtils;
import org.wlpiaoyi.server.demo.common.core.response.ResponseWrapper;
import org.wlpiaoyi.server.demo.common.tools.web.domain.ErrorEnum;

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

    /**
     * <p><b>{@code @description:}</b>
     * TODO
     * </p>
     *
     * <p><b>{@code @param}</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 9:13</p>
     * <p><b>{@code @return:}</b>{@link EncryptUriSet}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract EncryptUriSet getEncryptUriSet();


    @Override
    public String getRequestURI(HttpServletRequest servletRequest) {
        return servletRequest.getRequestURI();
    }

    /**
     * <p><b>{@code @description:}</b>
     * 非对称加密
     * </p>
     *
     * <p><b>{@code @param}</b> <b>request</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>response</b>
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
     * 数据签名
     * </p>
     *
     * <p><b>{@code @param}</b> <b>request</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>response</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/15 15:54</p>
     * <p><b>{@code @return:}</b>{@link SignVerify}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract SignVerify getSignVerify(HttpServletRequest request, HttpServletResponse response);

    /**
     * <p><b>{@code @description:}</b>
     * TODO
     * </p>
     *
     * <p><b>{@code @param}</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/12 14:56</p>
     * <p><b>{@code @return:}</b>{@link String}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract String loadSalt(String token);

    /**
     * <p><b>{@code @description:}</b>
     * 加密 Response Body
     * </p>
     *
     * <p><b>{@code @param}</b> <b>respWrapper</b>
     * {@link ResponseWrapper}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>servletResponse</b>
     * {@link ServletResponse}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>aes</b>
     * {@link Aes}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/11 23:51</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected void encryptResponseBody(ResponseWrapper respWrapper, HttpServletResponse response, Aes aes, SignVerify signVerify) throws Exception {
        byte[] respData = respWrapper.getResponseData();
        String sign = null;
        if(!ValueUtils.isBlank(respData) && aes != null){
            sign = new String(DataUtils.base64Encode(signVerify.sign(respData)));
            //加密响应报文
            respData = aes.encrypt(respData);
        }
        for (String headerName : respWrapper.getHeaderNames()){
            response.setHeader(headerName, respWrapper.getHeader(headerName));
        }
        String contentType = respWrapper.getContentType();
        if(aes != null){
            if(!contentType.startsWith(WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG)){
                contentType = WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + contentType;
            }
        }else{
            if(contentType.startsWith(WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG)){
                contentType = contentType.substring(WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG.length());
            }
        }
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        if(ValueUtils.isNotBlank(sign)){
            response.setHeader(WebUtils.HEADER_SIGN_KEY, sign);
        }
        ResponseUtils.writeResponseData(response.getStatus(), respData, response);
    }

    @Override
    public int doFilter(HttpServletRequest request, HttpServletResponse response, Map obj) {

        String uri = this.getRequestURI(request);
        EncryptUriSet uriSet = this.getEncryptUriSet();
        if(uriSet != null && uriSet.contains(uri)){
            ResponseWrapper respWrapper;
            try {
                respWrapper = new ResponseWrapper(response);
            } catch (IOException e) {
                throw new BusinessException(ErrorEnum.Unknown, e);
            }
            obj.put("response", respWrapper);
            obj.put("encrypt_tag", true);
        }
        return DoFilterEnum.GoNext.getValue();
    }

    @Override
    public int isSupportExecResponse(HttpServletRequest request, HttpServletResponse response, Map obj) {
        String uri = this.getRequestURI(request);
        EncryptUriSet uriSet = this.getEncryptUriSet();
        if(uriSet != null && uriSet.contains(uri)){
            return 1;
        }else{
            return WebSupport.super.isSupportExecResponse(request, response, obj);
        }
    }

    @Override
    public void execResponse(HttpServletRequest request, HttpServletResponse response, Map obj, int indexSupport, int totalSupports) {
        if(indexSupport == totalSupports - 1){
            Object resp = MapUtils.get(obj, "response");
            if(resp == null){
                return;
            }
            if(!(resp instanceof ResponseWrapper)){
                return;
            }
            ResponseWrapper respWrapper = (ResponseWrapper) resp;
            try {
                String token = request.getHeader(WebUtils.HEADER_TOKEN_KEY);
                Aes aes = null;
                SignVerify signVerify = null;
                if(obj.containsKey("encrypt_tag")){
                    String dSalt = this.loadSalt(token);
                    if(ValueUtils.isNotBlank(dSalt)){
                        RsaCipher rsa = this.getRsaEncrypt(request, response);
                        String eSalt = new String(DataUtils.base64Encode(rsa.encrypt(dSalt.getBytes(StandardCharsets.UTF_8))));
                        response.setHeader(WebUtils.HEADER_SALT_KEY, eSalt);
                        String[] keys = dSalt.split(",");
                        aes = Aes.create().setKey(keys[0]).setIV(keys[1]).load();
                        String resContentType = respWrapper.getContentType();
                        if(ValueUtils.isBlank(resContentType)){
                            resContentType = request.getHeader(HttpHeaders.ACCEPT);
                            respWrapper.setContentType(resContentType);
                        }
                        signVerify = this.getSignVerify(request, response);
                    }
                }
                HttpServletRequest servletRequest = MapUtils.get(obj, "request", request);
                ResponseUtils.prepareHeader(servletRequest, response);
                this.encryptResponseBody(respWrapper, response, aes, signVerify);
            } catch (Exception e) {
                try {
                    request.getInputStream().close();
                } catch (Exception ex) {}
                try {
                    response.getOutputStream().close();
                } catch (Exception ex) {}
                throw new BusinessException(ErrorEnum.Unknown, e);
            }
        }
    }
}
