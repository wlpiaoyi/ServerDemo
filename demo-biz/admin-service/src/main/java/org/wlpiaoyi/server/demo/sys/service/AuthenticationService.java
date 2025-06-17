package org.wlpiaoyi.server.demo.sys.service;

import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthUser;

/**
 * <p<b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 令牌认证
 * </p>
 * <p><b>{@code @date:}</b>2025/1/31 16:15</p>
 * <p><b>{@code @version:}</b>1.0</p>
 */
public interface AuthenticationService {

    /**
     * <p><b>{@code @description:}</b>
     * 登录
     * </p>
     *
     * <p><b>@param</b> <b>user</b>
     * {@link AuthUser}
     * </p>
     *
     * <p><b>@param</b> <b>client</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/2/1 15:52</p>
     * <p><b>{@code @return:}</b>{@link String}
     * 返回token
     * </p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    String login(AuthUser user, String client);

    /**
     * <p><b>{@code @description:}</b>
     * 登出
     * </p>
     *
     * <p><b>@param</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/31 16:23</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    boolean logout(String token);

    /**
     * <p><b>{@code @description:}</b>
     * 刷新
     * </p>
     *
     * <p><b>@param</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/31 16:44</p>
     * <p><b>{@code @return:}</b>{@link String}
     * 返回新的token
     * </p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    String refresh(String token);

}
