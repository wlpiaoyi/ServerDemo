package org.wlpiaoyi.server.demo.sys.service;

import org.wlpiaoyi.server.demo.sys.domain.entity.Access;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.RoleRo;
import org.wlpiaoyi.server.demo.service.IBaseService;

import java.util.Collection;
import java.util.List;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	角色 服务类接口
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
public interface IRoleService extends IBaseService<Role> {

    /**
     * <p><b>{@code @description:}</b>
     * 角色详情
     * </p>
     *
     * <p><b>@param</b> <b>id</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/14 14:45</p>
     * <p><b>{@code @return:}</b>{@link RoleVo}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    RoleVo getDetail(Long id);
}
