package org.wlpiaoyi.server.demo.sys.service;

import org.wlpiaoyi.server.demo.common.core.web.support.impl.access.AccessUriSet;
import org.wlpiaoyi.server.demo.common.datasource.service.IBaseService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Access;

import java.util.Collection;
import java.util.List;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	数据权限 服务类接口
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
public interface IAccessService extends IBaseService<Access> {

    /**
     * <p><b>{@code @description:}</b>
     * TODO
     * </p>
     *
     * <p><b>{@code @param}</b> <b>uriSet</b>
     * {@link AccessUriSet}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 21:50</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    void mergeAll(AccessUriSet uriSet);


    /**
     * <p><b>{@code @description:}</b>
     * 根据角色Id查询数据权限
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/16 15:17</p>
     * <p><b>{@code @return:}</b>{@link List< Access>}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    List<Access> listForRoleId(Long roleId);


    /**
     * <p><b>{@code @description:}</b>
     * 添加权限
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>accessIds</b>
     * {@link Collection < Long>}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 22:03</p>
     * <p><b>{@code @return:}</b>{@link int}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    int addAccess(Long roleId, Collection<Long> accessIds);

    /**
     * <p><b>{@code @description:}</b>
     * 修改数据权限
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>addAccessIds</b>
     * {@link Collection< Long>}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>delAccessId</b>
     * {@link Collection< Long>}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 22:05</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    void mergeAccesses(Long roleId, Collection<Long> addAccessIds, Collection<Long> delAccessId);

    /**
     * <p><b>{@code @description:}</b>
     * 删除数据权限
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>accessIds</b>
     * {@link Collection<Long>}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 22:04</p>
     * <p><b>{@code @return:}</b>{@link int}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    int deleteAccess(Long roleId, Collection<Long> accessIds);

}
