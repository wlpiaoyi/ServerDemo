package org.wlpiaoyi.server.demo.test.utils.support;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence.IdempotenceUriSet;

import java.util.HashMap;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 10:54:47</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class IdempotenceSupport extends org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence.IdempotenceSupport {

    @Getter
    @Autowired
    private IdempotenceMoonMapImpl idempotenceMoon;

    @Getter
    private IdempotenceUriSet idempotenceUriSet = IdempotenceUriSetObj;

    @SneakyThrows
    @Override
    public String getIdempotenceKey(HttpServletRequest servletRequest) {
        return DataUtils.MD(servletRequest.getRequestURI() + servletRequest.getRemoteAddr() + servletRequest.getRemotePort(), DataUtils.KEY_MD5);
    }

    @Override
    public String[] getURIRegexes() {
        return new String[]{"/test/.*"};
    }

    public static final IdempotenceUriSet IdempotenceUriSetObj = new IdempotenceUriSet() {

        private HashMap<String, Integer> uriMap = new HashMap<>();

        @Override
        public boolean contains(String uri) {
            return this.uriMap.containsKey(uri);
        }

        @Override
        public Integer get(String uri) {
            return this.uriMap.get(uri);
        }

        @Override
        public void put(String uri, int duriTime) {
            uriMap.put(uri, duriTime);
        }

    };
}
