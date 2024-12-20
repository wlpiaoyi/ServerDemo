package org.wlpiaoyi.server.demo.config.web.support;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.server.demo.common.tools.web.annotation.Idempotence;
import org.wlpiaoyi.server.demo.common.core.web.support.impl.idempotence.IdempotenceUriSet;

import java.util.HashMap;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 10:54:47</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class IdempotenceSupport extends org.wlpiaoyi.server.demo.common.core.web.support.impl.idempotence.IdempotenceSupport {

    @Getter
    @Autowired
    private IdempotenceMoonImpl idempotenceMoon;

    @Getter
    private IdempotenceUriSet idempotenceUriSet = IdempotenceUriSetObj;


    @SneakyThrows
    @Override
    public String getIdempotenceKey(HttpServletRequest servletRequest) {
        return DataUtils.MD(servletRequest.getRequestURI() + servletRequest.getRemoteAddr(), DataUtils.KEY_MD5);
    }

    @Value("${wlpiaoyi.ee.cors.data.patterns.idempotence}")
    private String[] patterns;

    @Override
    public String[] getURIRegexes() {
        return this.patterns;
//        return new String[]{"/test/.*"};
    }

    public static final IdempotenceUriSet IdempotenceUriSetObj = new IdempotenceUriSet() {

        private HashMap<String, Idempotence> uriMap = new HashMap<>();

        @Override
        public boolean contains(String uri) {
            return this.uriMap.containsKey(uri);
        }

        @Override
        public Idempotence get(String uri) {
            return this.uriMap.get(uri);
        }

        @Override
        public void put(String uri, Idempotence idempotence) {
            uriMap.put(uri, idempotence);
        }

    };
}
