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
 * <p><b>{@code @date:}</b> 2024-10-12 15:44:40</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
@Component
public class DecryptSupport extends org.wlpiaoyi.server.demo.utils.web.support.impl.encrypt.DecryptSupport {


    @Resource(name = "encrypt.aes")
    private Aes aes;

    @Resource(name = "encrypt.rsad")
    private RsaCipher rsaDecrypt;

    @Override
    protected Aes getAes(HttpServletRequest request, HttpServletResponse response) {
        return this.aes;
    }

    @Override
    protected RsaCipher getRsaDecrypt(HttpServletRequest request, HttpServletResponse response) {
        return this.rsaDecrypt;
    }

    @Override
    public String[] getURIRegexes() {
        return new String[]{"/test/auth/login|/test/common/list"};
//        return new String[]{"/abc"};
    }
}
