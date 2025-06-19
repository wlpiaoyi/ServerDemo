package org.wlpiaoyi.server.demo.sys.auth;

import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthUser;

/**
 * <p<b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 令牌认证
 * </p>
 * <p><b>{@code @date:}</b>2025/1/31 16:15</p>
 * <p><b>{@code @version:}</b>1.0</p>
 */
public interface AuthenticationService<U extends AuthUser> {

    /**
     * <p><b>{@code @description:}</b>
     * 登录
     * </p>
     *
     * <p><b>@param</b> <b>user</b>
     * {@link U}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/6/19 13:51</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    String login(U user);

    /**
     * <p><b>{@code @description:}</b>
     * 登录
     * </p>
     *
     * <p><b>@param</b> <b>user</b>
     * {@link U}
     * </p>
     *
     * <p><b>@param</b> <b>deviceType</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/6/19 13:26</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    String login(U user, String deviceType);

    /**
     * <p><b>{@code @description:}</b>
     * 是否登录
     * </p>
     *
     * <p><b>@param</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/6/19 18:16</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    boolean isLogin();

    /**
     * <p><b>{@code @description:}</b>
     * 是否登录
     * </p>
     *
     * <p><b>@param</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/6/19 18:16</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    boolean isLogin(String token);

    /**
     * <p><b>{@code @description:}</b>
     * 登出
     * </p>
     *
     * <p><b>@param</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/6/19 13:30</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    void logout();
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
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    void logout(String token);


    /**
     * <p><b>{@code @description:}</b>
     * 刷新
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/6/19 13:39</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    boolean refresh();

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
     * <p><b>{@code @return:}</b>{@link boolean}
     * 返回新的token
     * </p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    boolean refresh(String token);

}
