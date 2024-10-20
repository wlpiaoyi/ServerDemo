package org.wlpiaoyi.server.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.wlpiaoyi.framework.utils.StringUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.exception.SystemException;
import org.wlpiaoyi.server.demo.cache.RoleCachesService;
import org.wlpiaoyi.server.demo.cache.UserCachesService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.mapper.DeptMapper;
import org.wlpiaoyi.server.demo.sys.domain.mapper.RoleMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.DeptVo;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;
import org.wlpiaoyi.server.demo.sys.service.IUserService;
import org.wlpiaoyi.server.demo.sys.domain.entity.User;
import org.wlpiaoyi.server.demo.sys.domain.mapper.UserMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.UserVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.UserRo;
import org.wlpiaoyi.server.demo.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.server.demo.utils.SpringUtils;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.web.domain.WebError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	用户表 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserCachesService userCachesService;

    @Autowired
    private RoleCachesService roleCachesService;

    @Override
    public void switchRole(String token, Long roleId) {
        User authUser = SpringUtils.getAuthUser();
        if(authUser == null){
            throw new BusinessException(WebError.UnLogin);
        }
        User user = new User();
        user.setId(authUser.getId());
        user.setCurRoleId(roleId);
        super.updateById(user);
        this.roleCachesService.switchRole(token, roleId);
    }

    @Override
    public UserVo login(String token, UserRo.UserAuth auth) {
        List<User> users = this.list(Wrappers.<User>lambdaQuery().eq(User::getAccount, auth.getAccount()).eq(User::getPassword, auth.getPassword()));
        if(ValueUtils.isBlank(users)){
            throw new BusinessException("账号或者密码不对");
        }
        User user = users.getFirst();
        UserVo detail = this.getDetail(user.getId());
        this.userCachesService.setAuthUserVo(token, detail);
        return detail;
    }

    @Value("${wlpiaoyi.ee.auth.duri_minutes}")
    private int authDuriMinutes;

    @Override
    public void expire(String token) throws SystemException {
        if(!this.userCachesService.expireAuthUser(token)){
            throw new BusinessException("Auth user 续期失败");
        }
    }

    public UserVo getDetail(Long id){
        User user = this.getById(id);
        if(user == null){
            return null;
        }
        UserVo detail = ModelWrapper.parseOne(user, UserVo.class);
        detail.setRoles(ModelWrapper.parseForList(this.roleMapper.selectByUserIds(new ArrayList(){{add(detail.getId());}}), RoleVo.class));
        if(ValueUtils.isNotBlank(detail.getRoles())){
            detail.setCurRole(detail.getRoles().getFirst());
            if(ValueUtils.isNotBlank(detail.getCurRoleId())){
                for (RoleVo roleVo : detail.getRoles()){
                    if(roleVo.getId().longValue() == detail.getCurRoleId().longValue()){
                        detail.setCurRole(roleVo);
                        break;
                    }
                }
            }
        }
        detail.setDept(ModelWrapper.parseOne(this.deptMapper.selectById(user.getDeptId()), DeptVo.class));
        return detail;
    }

    @Override
    public boolean updateById(User entity) {
        User db = this.getById(entity.getId());
        List<Role> roles = this.roleMapper.selectByUserIds(new ArrayList(){{add(db.getId());}});
        for (Role role : roles){
            if(role.getCode().equals("admin")){
                throw new BusinessException("不能修改管理员");
            }
        }
        return super.updateById(entity);
    }

    public int addRoles(Long userId, Collection<Long> roleIds){
        if(this.roleMapper.selectCount(Wrappers.<Role>lambdaQuery().in(Role::getId, roleIds).eq(Role::getCode, "admin")) > 0){
            throw new BusinessException("不能添加管理员权限");
        }
        List<Role> roles = this.roleMapper.selectByUserIds(new ArrayList(){{add(userId);}});
        List<Long> insertIds = new ArrayList<>(roleIds.size());
        if(ValueUtils.isNotBlank(roles)){
            List<Long> exitIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            for(Long roleId : roleIds){
                if(exitIds.contains(roleId)){
                    continue;
                }
                insertIds.add(roleId);
            }
        }
        if(insertIds.isEmpty()){
            return 0;
        }
        return this.roleMapper.insertUserRelaBatch(userId, insertIds);
    }

    @Override
    @Transactional
    public int deleteRoles(Long userId, Collection<Long> roleIds) {
        if(this.roleMapper.selectCount(Wrappers.<Role>lambdaQuery().in(Role::getId, roleIds).eq(Role::getCode, "admin")) > 0){
            throw new BusinessException("不能删除管理员权限");
        }
        for(Long roleId : roleIds){
            this.roleMapper.deleteUserRela(userId, roleId);
        }
        return 1;
    }

    @Override
    public void mergeRoles(Long userId, Collection<Long> addRoleIds, Collection<Long> delRoleIds) {
        if(ValueUtils.isNotBlank(addRoleIds)){
            this.addRoles(userId, addRoleIds);
        }
        if(ValueUtils.isNotBlank(delRoleIds)){
            this.deleteRoles(userId, delRoleIds);
        }
    }

    @Override
    public boolean deleteLogic(List<Long> ids) {
        List<Role> roles = this.roleMapper.selectByUserIds(ids);
        for (Role role : roles){
            if(role.getCode().equals("admin")){
                throw new BusinessException("不能删除管理员");
            }
        }
        return super.deleteLogic(ids);
    }
}
