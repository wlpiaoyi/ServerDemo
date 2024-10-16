package org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/16 17:46
 * {@code @version:}:       1.0
 */
public interface IdempotenceMoon {

    /**
     * <p><b>{@code @description:}</b>
     * TODO
     * </p>
     *
     * <p><b>@param</b> <b>key</b>
     * {@link String}
     * </p>
     *
     * <p><b>@param</b> <b>duriSecond</b>
     * {@link int}
     * </p>
     *
     * <p><b>@param</b> <b>count</b>
     * {@link int}
     * </p>
     *
     * <p><b>@param</b> <b>deadLockMinutes</b>
     * {@link int}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/16 15:38</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    boolean isIdempotence(String key, int duriSecond, int count, int deadLockMinutes);

}
