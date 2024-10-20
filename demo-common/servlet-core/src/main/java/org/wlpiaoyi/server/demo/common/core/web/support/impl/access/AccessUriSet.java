package org.wlpiaoyi.server.demo.common.core.web.support.impl.access;

import java.util.Set;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 13:54:02</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
public interface AccessUriSet {

    Set<String> getAllUri();

    boolean contains(String uri);

    String get(String uri);

    void put(String uri, String value);
}
