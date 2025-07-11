package org.wlpiaoyi.server.demo.common.core.web.support.impl.encrypt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.tools.web.domain.DoFilterEnum;
import org.wlpiaoyi.server.demo.common.tools.web.domain.ErrorEnum;
import org.wlpiaoyi.server.demo.common.core.web.support.WebSupport;
import org.wlpiaoyi.server.demo.common.core.request.RequestWrapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.Map;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b> 2024-10-12 15:41:21</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
@Slf4j
public abstract class DecryptSupport  implements WebSupport<HttpServletRequest, HttpServletResponse> {

    /**
     * <p><b>{@code @description:}</b>
     * TODO
     * </p>
     *
     * <p><b>{@code @param}</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 9:10</p>
     * <p><b>{@code @return:}</b>{@link DecryptUriSet}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract DecryptUriSet getDecryptUriSet();


    @Override
    public String getRequestURI(HttpServletRequest servletRequest) {
        return servletRequest.getRequestURI();
    }


    /**
     * <p><b>{@code @description:}</b>
     * 构建对称加密
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
     * <p><b>{@code @date:}</b>2024/10/11 23:48</p>
     * <p><b>{@code @return:}</b>{@link Aes}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected abstract Aes createAes(HttpServletRequest request, HttpServletResponse response);

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
    protected abstract RsaCipher getRsaDecrypt(HttpServletRequest request, HttpServletResponse response);

    /**
     * <p><b>{@code @description:}</b>
     * 解密 Request Body
     * </p>
     *
     * <p><b>{@code @param}</b> <b>reqWrapper</b>
     * {@link RequestWrapper}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>aes</b>
     * {@link Aes}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/11 23:36</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    protected void decryptRequestBody(RequestWrapper reqWrapper, Aes aes) throws IllegalBlockSizeException, BadPaddingException {
        byte[] reqBody = reqWrapper.getBody();
        if(!ValueUtils.isBlank(reqBody) && aes != null){
            //解密请求报文
            reqBody = aes.decrypt(reqBody);
        }
        reqWrapper.setBody(reqBody);
    }


    @Override
    public int doFilter(HttpServletRequest request, HttpServletResponse response, Map obj) {
        String uri = getRequestURI(request);
        DecryptUriSet uriSet = this.getDecryptUriSet();
        Aes aes = null;
        if(uriSet != null && uriSet.contains(uri)){
            aes = this.createAes(request, response);
            if(aes == null){
                log.error("EncryptFilter.doFilter unfunded aes object");
                return DoFilterEnum.GoNext.getValue();
            }
        }
        //响应处理 包装响应对象 res 并缓存响应数据
        RequestWrapper reqWrapper;
        try {
            reqWrapper = new RequestWrapper(request, 1);
        } catch (IOException e) {
            throw new BusinessException(ErrorEnum.Unknown, e);
        }
        try {
            this.decryptRequestBody(reqWrapper, aes);
        } catch (IllegalBlockSizeException e) {
            throw new BusinessException(ErrorEnum.Unknown, e);
        } catch (BadPaddingException e) {
            throw new BusinessException(ErrorEnum.Unknown, e);
        }
        String salt = reqWrapper.getHeader(WebUtils.HEADER_SALT_KEY);
        if(ValueUtils.isNotBlank(salt)){
            RsaCipher rsa = this.getRsaDecrypt(request, response);
            salt = new String(rsa.decrypt(DataUtils.base64Decode(salt.getBytes())));
            reqWrapper.setHeader(WebUtils.HEADER_SALT_KEY, salt);
        }
        obj.put("request", reqWrapper);
        obj.put("response", response);
        return DoFilterEnum.GoNext.getValue();
    }
}
