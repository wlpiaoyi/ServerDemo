package org.wlpiaoyi.server.demo.config.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b> 2024-10-12 09:57:11</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
@Component
public class EncryptSupport extends org.wlpiaoyi.server.demo.utils.web.support.impl.encrypt.EncryptSupport {


    private Aes aes;
    {
        try {
            aes = Aes.create().setKey("abcd567890ABCDEF1234567890ABCDEF").setIV("abcd567890123456").load();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    protected Aes getAes(HttpServletRequest request, HttpServletResponse response) {
        return this.aes;
    }

    @Override
    public String[] getURIRegexes() {
        return new String[]{"/test/auth/login"};
    }
}
