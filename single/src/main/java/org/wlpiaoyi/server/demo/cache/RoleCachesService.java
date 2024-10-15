package org.wlpiaoyi.server.demo.cache;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;

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
public class RoleCachesService extends CachesService<Role>{

    @Value("${wlpiaoyi.ee.cache.duri_minutes}")
    private int cacheDuriMinutes;

    @Value("${wlpiaoyi.ee.auth.duri_minutes}")
    private int authDuriMinutes;

    private void setAuthRoleVo(String key, RoleVo roleVo){
        Role role = ModelWrapper.parseOne(roleVo, Role.class);
        super.setCache(role);
        Object[] ids = new Object[3];
        ids[0] = role.getId();
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
        RoleVo roleVo = ModelWrapper.parseOne(role, RoleVo.class);
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
        return this.expire((Long) roleId);
    }

    private void deleteAuthRoleVo(String key){
        super.redisTemplate.delete(key);
    }


    public void setAuthCurRole(String token, RoleVo roleVo){
        String key = "token_cache_role:" + token;
        this.setAuthRoleVo(key, roleVo);
    }

    public RoleVo getAuthCurRole(String token){
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

    public void setAuthRoles(String token, List<RoleVo> roleVos){
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

    public List<RoleVo> getAuthRoles(String token){
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

    @Override
    protected long getCacheDuriMinutes() {
        return this.cacheDuriMinutes;
    }

    @Override
    protected String getKeyTag() {
        return "dept";
    }
}
