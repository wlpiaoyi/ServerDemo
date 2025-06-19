package org.wlpiaoyi.server.demo.common.tools.web.domain;

/**
 * <p<b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 用户
 * </p>
 * <p><b>{@code @date:}</b>2025/1/26 22:54</p>
 * <p><b>{@code @version:}</b>1.0</p>
 */
public interface AuthUser<PK> {

    String keyTag = "authUser";
    /**
     * <p><b>{@code @description:}</b>
     * 用户Id
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/27 12:15</p>
     * <p><b>{@code @return:}</b>{@link PK}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    PK getId();

    /**
     * <p><b>{@code @description:}</b>
     * 账号
     * </p>
     *
     * <p><b>@param</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/31 16:17</p>
     * <p><b>{@code @return:}</b>{@link String}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    String getAccount();

    /**
     * <p><b>{@code @description:}</b>
     * 角色编码
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/26 22:54</p>
     * <p><b>{@code @return:}</b>{@link String}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    String getRoleCode();

}
