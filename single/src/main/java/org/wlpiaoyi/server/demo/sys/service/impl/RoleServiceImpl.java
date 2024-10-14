package org.wlpiaoyi.server.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.sys.domain.entity.Access;
import org.wlpiaoyi.server.demo.sys.domain.mapper.AccessMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.AccessVo;
import org.wlpiaoyi.server.demo.sys.service.IRoleService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.mapper.RoleMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.RoleRo;
import org.wlpiaoyi.server.demo.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


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
        detail.setAccesses(ModelWrapper.parseForList(this.accessMapper.selectByRoleIds(id), AccessVo.class));
        return detail;
    }

    @Override
    @Transactional
    public int addAccess(Long roleId, Collection<Long> accessIds) {
        if(this.count(Wrappers.<Role>lambdaQuery().eq(Role::getId, roleId).eq(Role::getCode, "admin")) > 0){
            throw new BusinessException("不能操作管理员权限");
        }
        List<Access> accesses = this.accessMapper.selectByRoleIds(roleId);
        List<Long> insertIds = new ArrayList<>(accessIds.size());
        if(ValueUtils.isNotBlank(accesses)){
            List<Long> exitIds = accesses.stream().map(Access::getId).collect(Collectors.toList());
            for(Long accessId : accessIds){
                if(exitIds.contains(accessId)){
                    continue;
                }
                insertIds.add(accessId);
            }
        }
        if(insertIds.isEmpty()){
            return 0;
        }
        return this.accessMapper.insertAccessRelaBatch(roleId, insertIds);
    }

    @Override
    @Transactional
    public int deleteAccess(Long roleId, Collection<Long> accessIds) {
        if(this.count(Wrappers.<Role>lambdaQuery().eq(Role::getId, roleId).eq(Role::getCode, "admin")) > 0){
            throw new BusinessException("不能操作管理员权限");
        }
        for(Long accessId : accessIds){
            this.accessMapper.deleteAccessRela(roleId, accessId);
        }
        return 1;
    }

    @Override
    @Transactional
    public void mergeAccesses(Long userId, Collection<Long> addAccessIds, Collection<Long> delAccessId) {
        if(ValueUtils.isNotBlank(addAccessIds)){
            this.addAccess(userId, addAccessIds);
        }
        if(ValueUtils.isNotBlank(delAccessId)){
            this.deleteAccess(userId, delAccessId);
        }
    }



    @Override
    public boolean deleteLogic(List<Long> ids) {
        if(this.baseMapper.selectCount(Wrappers.<Role>lambdaQuery().in(Role::getId, ids).eq(Role::getCode, "admin")) > 0){
            throw new BusinessException("不能删除管理员");
        }
        return super.deleteLogic(ids);
    }
}
