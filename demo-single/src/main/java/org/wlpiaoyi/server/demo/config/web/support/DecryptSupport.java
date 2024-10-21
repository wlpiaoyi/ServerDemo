package org.wlpiaoyi.server.demo.config.web.support;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.server.demo.common.core.utils.WebUtils;
import org.wlpiaoyi.server.demo.common.core.web.support.impl.encrypt.DecryptUriSet;

import java.util.HashSet;
import java.util.Set;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b> 2024-10-12 15:44:40</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
@Component
public class DecryptSupport extends org.wlpiaoyi.server.demo.common.core.web.support.impl.encrypt.DecryptSupport {


//    @Resource(name = "encrypt.aes")
//    private Aes aes;

    @Resource(name = "encrypt.rsad")
    private RsaCipher rsaDecrypt;

    @Override
    protected DecryptUriSet getDecryptUriSet() {
        return DECRYPT_URI_SET;
    }
    @SneakyThrows
    @Override
    protected Aes createAes(HttpServletRequest request, HttpServletResponse response) {
        String eSalt = request.getHeader(WebUtils.HEADER_SALT_KEY);
        String dSalt = new String(this.rsaDecrypt.decrypt(DataUtils.base64Decode(eSalt.getBytes())));
        String[] keys = dSalt.split(",");
        return Aes.create().setKey(keys[0]).setIV(keys[1]).load();
    }

    @Override
    protected RsaCipher getRsaDecrypt(HttpServletRequest request, HttpServletResponse response) {
        return this.rsaDecrypt;
    }

    @Value("${wlpiaoyi.ee.cors.data.patterns.decrypt}")
    private String[] patterns;

    @Override
    public String[] getURIRegexes() {
        return this.patterns;
//        return new String[]{"/test/.*"};
//        return new String[]{"/abc"};
    }

    public static final DecryptUriSet DECRYPT_URI_SET = new DecryptUriSet() {

        private Set<String> uriSet = new HashSet<>();

        @Override
        public boolean contains(String uri) {
            return this.uriSet.contains(uri);
        }

        @Override
        public void add(String uri) {
            this.uriSet.add(uri);
        }
    };
}
