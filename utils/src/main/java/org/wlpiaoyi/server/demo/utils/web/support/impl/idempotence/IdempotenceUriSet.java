package org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence;

/**
 * <p><b>{@code @author:}</b>         wlpiaoyi</p>
 * <p><b>{@code @description:}</b>    幂等URI集合</p>
 * <p><b>{@code @date:}</b>           2024/9/17 19:34</p>
 * <p><b>{@code @version:}</b>       1.0</p>
 */

public interface IdempotenceUriSet {

    boolean contains(String uri);

    Integer get(String uri);

    void put(String uri, int duriTime);

}
