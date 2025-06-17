package org.wlpiaoyi.server.demo.common.tools.web.domain;

/**
 * <p<b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 角色
 * </p>
 * <p><b>{@code @date:}</b>2025/1/26 22:53</p>
 * <p><b>{@code @version:}</b>1.0</p>
 */
public interface AuthRole<PK> {

    /**
     * <p><b>{@code @description:}</b>
     * 角色Id
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/27 12:16</p>
     * <p><b>{@code @return:}</b>{@link PK}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    PK getId();

    /**
     * <p><b>{@code @description:}</b>
     * 角色编码
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/26 22:54</p>
     * <p><b>{@code @return:}</b>{@link String}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    String getCode();
}
