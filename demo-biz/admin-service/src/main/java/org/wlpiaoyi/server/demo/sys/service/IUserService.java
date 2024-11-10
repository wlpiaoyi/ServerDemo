package org.wlpiaoyi.server.demo.sys.service;

import org.wlpiaoyi.framework.utils.exception.SystemException;
import org.wlpiaoyi.server.demo.common.datasource.service.IBaseService;
import org.wlpiaoyi.server.demo.sys.domain.entity.User;
import org.wlpiaoyi.server.demo.sys.domain.ro.UserRo;
import org.wlpiaoyi.server.demo.sys.domain.vo.UserVo;

import java.util.Collection;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	用户表 服务类接口
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
public interface IUserService extends IBaseService<User> {


    /**
     * <p><b>{@code @description:}</b>
     * 切换角色
     * </p>
     *
     * <p><b>{@code @param}</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/18 10:48</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    void switchRole(String token, Long roleId) ;

    /**
     * <p><b>{@code @description:}</b>
     * 登录
     * </p>
     *
     * <p><b>{@code @param}</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>auth</b>
     * {@link UserRo.UserAuth}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 11:23</p>
     * <p><b>{@code @return:}</b>{@link UserVo}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    UserVo login(String token, UserRo.UserAuth auth);

    /**
     * <p><b>{@code @description:}</b>
     * token续期
     * </p>
     *
     * <p><b>{@code @param}</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 16:08</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    void expire(String token) throws SystemException;

    /**
     * <p><b>{@code @description:}</b>
     * 详情
     * </p>
     *
     * <p><b>{@code @param}</b> <b>id</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 11:23</p>
     * <p><b>{@code @return:}</b>{@link UserVo}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    UserVo getDetail(Long id);

    /**
     * <p><b>{@code @description:}</b>
     * 添加角色
     * </p>
     *
     * <p><b>{@code @param}</b> <b>userId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleIds</b>
     * {@link Collection<Long>}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 21:15</p>
     * <p><b>{@code @return:}</b>{@link int}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    int addRoles(Long userId, Collection<Long> roleIds);

    /**
     * <p><b>{@code @description:}</b>
     * 删除角色
     * </p>
     *
     * <p><b>{@code @param}</b> <b>userId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleIds</b>
     * {@link Collection< Long>}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 21:17</p>
     * <p><b>{@code @return:}</b>{@link int}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    int deleteRoles(Long userId, Collection<Long> roleIds);

    /**
     * <p><b>{@code @description:}</b>
     * 修改角色
     * </p>
     *
     * <p><b>{@code @param}</b> <b>userId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>addRoleIds</b>
     * {@link Collection< Long>}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>delRoleIds</b>
     * {@link Collection< Long>}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 21:19</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    void mergeRoles(Long userId, Collection<Long> addRoleIds, Collection<Long> delRoleIds);

}
