package org.wlpiaoyi.server.demo.sys.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.server.demo.common.datasource.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.sys.domain.entity.Access;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-14 16:00:23</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Service
public class RoleCachesService extends CachesService<Role> {


    @Value("${wlpiaoyi.ee.cache.duri_minutes}")
    private int cacheDuriMinutes;

    @Value("${wlpiaoyi.ee.auth.duri_minutes}")
    private int authDuriMinutes;

    private final String keyTag = "role";

    @Autowired
    private AccessCachesService accessCachesService;

    @Override
    protected long getCacheDuriMinutes() {
        return this.cacheDuriMinutes;
    }

    @Override
    protected String getKeyTag() {
        return this.keyTag;
    }

    /**
     * <p><b>{@code @description:}</b>
     * 切换角色
     * </p>
     *
     * <p><b>@param</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>@param</b> <b>roleId</b>
     * {@link Long}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/18 10:42</p>
     * <p><b>{@code @return:}</b>
     * {@link int}:
     * <br/>
     * -1:未登录 0:没有当前角色 1:成功
     * </p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public int switchRole(String token, Long roleId){
        if(!this.expireAuthRoles(token)){
            return -1;
        }
        String key = "token_cache_roles:" + token;
        if(!this.redisTemplate.expire(key, this.authDuriMinutes, TimeUnit.MINUTES)){
            return -1;
        }
        Object res = this.redisTemplate.opsForValue().get(key);
        if(res == null){
            return -1;
        }
        Object[] ids = (Object[]) res;
        for (Object id : ids){
            if(id.equals(roleId)){
                Role role = this.getCache(roleId);
                if(role == null){
                    return -1;
                }
                this.setCurAuthRoleVo(token, ModelWrapper.parseOne(role, RoleVo.class));
                return 1;
            }
        }
        return 0;
    }


    private void setAuthRoleVo(String key, RoleVo roleVo){
        super.setCache(ModelWrapper.parseOne(roleVo, Role.class));
        Object[] ids;
        Object res = super.redisTemplate.opsForValue().get(key);
        if(res instanceof Object[]){
            ids = (Object[]) res;
        }else{
            ids = new Object[2];
        }
        ids[0] = roleVo.getId();
        if(roleVo.getAccesses() != null){
            if(roleVo.getAccesses().size() == 0){
                ids[1] = null;
            }else{
                ids[1] = roleVo.getAccesses().stream().map(Access::getId).toArray();
            }
            for (Access access : roleVo.getAccesses()){
                if(this.accessCachesService.expire(access.getId())){
                    continue;
                }
                this.accessCachesService.setCache(access);
            }
        }
        super.redisTemplate.opsForValue().set(key, ids, this.authDuriMinutes, TimeUnit.MINUTES);
    }

    private RoleVo getAuthRoleVo(String key){
        Object res = super.redisTemplate.opsForValue().get(key);
        if(res == null){
            return null;
        }
        Object[] ids = (Object[]) res;
        Role role = super.get((Long) ids[0]);
        if(role == null){
            return null;
        }
        List<Access> accesses = null;
        if(ids[1] != null){
            Long[] accessIds = (Long[]) ids[1];
            accesses = new ArrayList<>();
            for(Long accessId : accessIds){
                Access access = this.accessCachesService.getCache(accessId);
                accesses.add(access);
            }
        }
        RoleVo roleVo = ModelWrapper.parseOne(role, RoleVo.class);
        roleVo.setAccesses(accesses);
        return roleVo;
    }

    private boolean expireAuthRoleVo(String key){
        Object res = super.redisTemplate.opsForValue().get(key);
        if(res == null){
            return false;
        }
        Object[] ids = (Object[]) res;
        Object roleId = ids[0];
        if (roleId == null){
            return false;
        }
        if(ids[1] != null){
            Long[] accessIds = (Long[]) ids[1];
            for (Long accessId : accessIds){
                if(!this.accessCachesService.expire(accessId)){
                    return false;
                }
            }
        }
        return this.expire((Long) roleId);
    }

    public Role getCurAuthRole(String token) {
        String key = "token_cache_role:" + token;
        Object res = super.redisTemplate.opsForValue().get(key);
        if(res == null){
            return null;
        }
        Object[] ids = (Object[]) res;
        return super.getCache((Long) ids[0]);
    }

    private void deleteAuthRoleVo(String key){
        super.redisTemplate.delete(key);
    }


    public void setCurAuthRoleVo(String token, RoleVo roleVo){
        String key = "token_cache_role:" + token;
        this.setAuthRoleVo(key, roleVo);
    }

    public RoleVo getCurAuthRoleVo(String token){
        String key = "token_cache_role:" + token;
        return this.getAuthRoleVo(key);
    }

    public boolean expireAuthCurRole(String token){
        String key = "token_cache_role:" + token;
        return this.expireAuthRoleVo(key);
    }

    public void deleteAuthCurRole(String token){
        String key = "token_cache_role:" + token;
        this.deleteAuthRoleVo(key);
    }

    public void setAuthRoleVos(String token, List<RoleVo> roleVos){
        Map<Long, RoleVo> map = roleVos.stream().collect(Collectors.toMap(
                RoleVo::getId,
                Function.identity()
        ));
        Object[] ids = map.keySet().toArray();
        String key = "token_cache_roles:" + token;
        this.redisTemplate.opsForValue().set(key, ids);
        for (Object id : ids){
            this.setAuthRoleVo(key + id.toString(), map.get(id));
        }
    }

    public List<RoleVo> getAuthRoleVos(String token){
        String key = "token_cache_roles:" + token;
        Object res = this.redisTemplate.opsForValue().get(key);
        if(res == null){
            return null;
        }
        Object[] ids = (Object[]) res;
        List<RoleVo> roleVos = new ArrayList<>();
        for (Object id : ids){
            roleVos.add(this.getAuthRoleVo(key + id.toString()));
        }
        return roleVos;
    }

    public boolean expireAuthRoles(String token){
        String key = "token_cache_roles:" + token;
        if(!this.redisTemplate.expire(key, this.authDuriMinutes, TimeUnit.MINUTES)){
            return false;
        }
        Object res = this.redisTemplate.opsForValue().get(key);
        if(res == null){
            return false;
        }
        Object[] ids = (Object[]) res;
        for (Object id : ids){
            if(!this.expireAuthRoleVo(key + id.toString())){
                return false;
            }
        }
        return true;
    }

    public void deleteAuthRoles(String token){
        String key = "token_cache_roles:" + token;
        Object res = this.redisTemplate.opsForValue().get(key);
        if(res != null){
            Object[] ids = (Object[]) res;
            for (Object id : ids){
                this.deleteAuthRoleVo(key + id);
            }
        }
        super.redisTemplate.delete(key);

    }
}
