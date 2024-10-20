package org.wlpiaoyi.server.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.config.web.support.AccessSupport;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.mapper.RoleMapper;
import org.wlpiaoyi.server.demo.sys.service.IAccessService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Access;
import org.wlpiaoyi.server.demo.sys.domain.mapper.AccessMapper;
import org.wlpiaoyi.server.demo.common.datasource.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.server.demo.common.core.utils.IdUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	数据权限 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class AccessServiceImpl extends BaseServiceImpl<AccessMapper, Access> implements IAccessService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public void mergeAll(){
        List<Long> ids = new ArrayList<>();
        for (String uri : AccessSupport.ACCESS_URI_SET.getAllUri()){
            String value = AccessSupport.ACCESS_URI_SET.get(uri);
            if(ValueUtils.isBlank(value)){
                continue;
            }
            ids.add(this.merge(uri, value).getId());
        }
        if(ValueUtils.isNotBlank(ids)){
            List<Access> removes = this.baseMapper.selectList(Wrappers.<Access>lambdaQuery().notIn(Access::getId, ids));
            if(ValueUtils.isNotBlank(removes)){
                this.baseMapper.deleteBatchIds(removes.stream().map(Access::getId).collect(Collectors.toList()));
            }
        }
    }

    @Transactional
    public Access merge(String uri, String value) {
        Access access = this.getOne(Wrappers.<Access>lambdaQuery().eq(Access::getPath, uri).eq(Access::getValue, value));
        if(access != null){
            return access;
        }
        access = new Access();
        access.setId(IdUtils.nextId());
        access.setPath(uri);
        access.setValue(value);
        this.save(access);
        return access;
    }

    @Override
    public List<Access> listForRoleId(Long roleId) {
        return this.baseMapper.selectByRoleIds(roleId);
    }

    @Override
    @Transactional
    public int addAccess(Long roleId, Collection<Long> accessIds) {
        if(this.roleMapper.selectCount(Wrappers.<Role>lambdaQuery().eq(Role::getId, roleId).eq(Role::getCode, "admin")) > 0){
            throw new BusinessException("不能操作管理员权限");
        }
        List<Access> accesses = this.baseMapper.selectByRoleIds(roleId);
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
        return this.baseMapper.insertAccessRelaBatch(roleId, insertIds);
    }

    @Override
    @Transactional
    public void mergeAccesses(Long AccessSubmit, Collection<Long> addAccessIds, Collection<Long> delAccessId) {
        if(ValueUtils.isNotBlank(addAccessIds)){
            this.addAccess(AccessSubmit, addAccessIds);
        }
        if(ValueUtils.isNotBlank(delAccessId)){
            this.deleteAccess(AccessSubmit, delAccessId);
        }
    }

    @Override
    @Transactional
    public int deleteAccess(Long roleId, Collection<Long> accessIds) {
        if(this.roleMapper.selectCount(Wrappers.<Role>lambdaQuery().eq(Role::getId, roleId).eq(Role::getCode, "admin")) > 0){
            throw new BusinessException("不能操作管理员权限");
        }
        for(Long accessId : accessIds){
            this.baseMapper.deleteAccessRela(roleId, accessId);
        }
        return 1;
    }
}
