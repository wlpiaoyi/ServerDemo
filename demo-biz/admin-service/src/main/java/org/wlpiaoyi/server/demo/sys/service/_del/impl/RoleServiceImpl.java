package org.wlpiaoyi.server.demo.sys.service._del.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.common.datasource.service.impl.BaseServiceImpl;
import org.wlpiaoyi.server.demo.common.datasource.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.mapper.AccessMapper;
import org.wlpiaoyi.server.demo.sys.domain.mapper.RoleMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;
import org.wlpiaoyi.server.demo.sys.service._del.IRoleService;

import java.util.List;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	角色 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private AccessMapper accessMapper;

    @Override
    public RoleVo getDetail(Long id) {
        Role role = this.baseMapper.selectById(id);
        if(role == null){
            return null;
        }
        RoleVo detail = ModelWrapper.parseOne(role, RoleVo.class);
        detail.setAccesses(this.accessMapper.selectByRoleIds(id));
        return detail;
    }

    @Override
    public boolean save(Role entity) {
        if("admin".equals(entity.getCode())){
            throw new BusinessException("不能新增管理员角色");
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(Role entity) {
        Role db = this.baseMapper.selectById(entity.getId());
        if("admin".equals(db.getCode())){
            throw new BusinessException("不能操作管理员角色");
        }
        if("admin".equals(entity.getCode())){
            throw new BusinessException("不能修改为管理员角色");
        }
        return super.updateById(entity);
    }

    @Override
    public boolean deleteLogic(List<Long> ids) {
        if(this.baseMapper.selectCount(Wrappers.<Role>lambdaQuery().in(Role::getId, ids).eq(Role::getCode, "admin")) > 0){
            throw new BusinessException("不能删除管理员");
        }
        return super.deleteLogic(ids);
    }
}
