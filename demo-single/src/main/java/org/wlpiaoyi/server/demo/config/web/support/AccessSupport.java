package org.wlpiaoyi.server.demo.config.web.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.common.core.web.support.impl.access.AccessUriSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 14:13:08</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class AccessSupport extends org.wlpiaoyi.server.demo.common.core.web.support.impl.access.AccessSupport {

    @Override
    protected AccessUriSet getAccessUriSet() {
        return ACCESS_URI_SET;
    }


    @Override
    protected boolean preAuthorize(String value, String token) {
        return true;
    }

    @Value("${wlpiaoyi.ee.cors.data.patterns.access}")
    private String[] patterns;

    @Override
    public String[] getURIRegexes() {
        return this.patterns;
//        return new String[]{"/test/.*"};
    }

    public static final AccessUriSet ACCESS_URI_SET = new AccessUriSet() {

        private Map<String, String> uriMap = new HashMap<>();

        @Override
        public Set<String> getAllUri() {
            return uriMap.keySet();
        }

        @Override
        public boolean contains(String uri) {
            return this.uriMap.containsKey(uri);
        }

        @Override
        public String get(String uri) {
            return this.uriMap.get(uri);
        }

        @Override
        public void put(String uri, String value) {
            this.uriMap.put(uri, value);
        }
    };
}
