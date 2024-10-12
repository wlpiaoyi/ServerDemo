package org.wlpiaoyi.server.demo.config.web.support;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.security.RsaCipher;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b> 2024-10-12 09:57:11</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
@Component
public class EncryptSupport extends org.wlpiaoyi.server.demo.utils.web.support.impl.encrypt.EncryptSupport {

    @Resource(name = "encrypt.aes")
    private Aes aes;

    @Resource(name = "encrypt.rsae")
    private RsaCipher rsaEncrypt;

    @Override
    protected Aes getAes(HttpServletRequest request, HttpServletResponse response) {
        return this.aes;
    }

    @Override
    protected RsaCipher getRsaEncrypt(HttpServletRequest request, HttpServletResponse response) {
        return this.rsaEncrypt;
    }

    @Override
    public String[] getURIRegexes() {
        return new String[]{"/test/auth/login|/test/common/list"};
//        return new String[]{"/abc"};
    }
}
