package org.wlpiaoyi.server.demo.config.web.support;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.framework.utils.StringUtils;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.framework.utils.security.SignVerify;
import org.wlpiaoyi.server.demo.common.core.web.support.impl.encrypt.EncryptUriSet;

import java.util.HashSet;
import java.util.Set;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b> 2024-10-12 09:57:11</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
@Component
public class EncryptSupport extends org.wlpiaoyi.server.demo.common.core.web.support.impl.encrypt.EncryptSupport {


    @Resource(name = "encrypt.rsae")
    private RsaCipher rsaEncrypt;
    @Resource(name = "signature.sign")
    private SignVerify signVerify;

    @Override
    protected EncryptUriSet getEncryptUriSet() {
        return ENCRYPT_URI_SET;
    }

    @Override
    protected RsaCipher getRsaEncrypt(HttpServletRequest request, HttpServletResponse response) {
        return this.rsaEncrypt;
    }

    @Override
    protected SignVerify getSignVerify(HttpServletRequest request, HttpServletResponse response) {
        return this.signVerify;
    }

    @Override
    protected String loadSalt(String token) {
        String key = StringUtils.getUUID32();
        String iv = StringUtils.getUUID32().substring(0, 16);
        String dSalt = key + "," + iv;
        return dSalt;
    }

    @Value("${wlpiaoyi.ee.cors.data.patterns.encrypt}")
    private String[] patterns;

    @Override
    public String[] getURIRegexes() {
        return this.patterns;
//        return new String[]{"/test/.*"};
//        return new String[]{"/abc"};
    }

    public static final EncryptUriSet ENCRYPT_URI_SET = new EncryptUriSet() {

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
