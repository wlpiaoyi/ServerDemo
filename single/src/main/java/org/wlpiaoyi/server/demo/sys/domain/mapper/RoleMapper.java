package org.wlpiaoyi.server.demo.sys.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.ro.RoleRo;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;

import java.util.List;

/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	角色 Mapper 接口
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * <p><b>{@code @description:}</b>
     * 查询指定User的角色
     * </p>
     *
     * <p><b>@param</b> <b>userIds</b>
     * {@link List<Long>}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 11:17</p>
     * <p><b>{@code @return:}</b>{@link List< Role>}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    List<Role> selectByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * <p><b>{@code @description:}</b>
     * 新增用户角色
     * </p>
     *
     * <p><b>@param</b> <b>userId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>@param</b> <b>roleIds</b>
     * {@link List< Long>}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 21:01</p>
     * <p><b>{@code @return:}</b>{@link int}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    int insertUserRelaBatch(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    /**
     * <p><b>{@code @description:}</b>
     * 删除用户角色
     * </p>
     *
     * <p><b>@param</b> <b>userId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>@param</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/13 21:03</p>
     * <p><b>{@code @return:}</b>{@link int}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    int deleteUserRela(@Param("userId") Long userId, @Param("roleId") Long roleId);



}
