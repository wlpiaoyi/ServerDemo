package org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/16 17:46
 * {@code @version:}:       1.0
 */
public interface IdempotenceMoonMap {

    Long get(String key);

    void put(String key, Long value, Integer duriTime);

    default void remove(String key) {}

    default void iterator(IdempotenceMoonIterator iterator) {}

    interface IdempotenceMoonIterator {
        void iterator(String key, Long value);
    }

}
