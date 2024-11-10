package org.wlpiaoyi.server.demo.sys.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.wlpiaoyi.server.demo.sys.domain.entity.Access;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;

import java.util.List;

/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	数据权限 Mapper 接口
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
public interface AccessMapper extends BaseMapper<Access> {


    /**
     * <p><b>{@code @description:}</b>
     * 查询指定角色的数据权限
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 21:47</p>
     * <p><b>{@code @return:}</b>{@link List<  Role >}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    List<Access> selectByRoleIds(@Param("roleId") Long roleId);

    /**
     * <p><b>{@code @description:}</b>
     * 新增角色数据权限
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>accessIds</b>
     * {@link List<Long>}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 21:48</p>
     * <p><b>{@code @return:}</b>{@link int}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    int insertAccessRelaBatch(@Param("roleId") Long roleId, @Param("accessIds") List<Long> accessIds);

    /**
     * <p><b>{@code @description:}</b>
     * 删除角色数据权限
     * </p>
     *
     * <p><b>{@code @param}</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>accessId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 21:49</p>
     * <p><b>{@code @return:}</b>{@link int}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    int deleteAccessRela(@Param("roleId") Long roleId, @Param("accessId") Long accessId);


}
