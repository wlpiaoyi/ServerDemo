package org.wlpiaoyi.server.demo.common.core.web.support.impl.idempotence;

import org.wlpiaoyi.server.demo.common.core.web.annotation.Idempotence;

/**
 * <p><b>{@code @author:}</b>         wlpiaoyi</p>
 * <p><b>{@code @description:}</b>    幂等URI集合</p>
 * <p><b>{@code @date:}</b>           2024/9/17 19:34</p>
 * <p><b>{@code @version:}</b>       1.0</p>
 */

public interface IdempotenceUriSet {

    boolean contains(String uri);

    Idempotence get(String uri);

    void put(String uri, Idempotence idempotence);

}
