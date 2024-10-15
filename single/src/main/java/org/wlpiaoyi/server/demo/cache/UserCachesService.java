package org.wlpiaoyi.server.demo.cache;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.entity.User;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;
import org.wlpiaoyi.server.demo.sys.domain.vo.UserVo;
import org.wlpiaoyi.server.demo.utils.SpringUtils;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-14 15:44:27</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Service
public class UserCachesService extends CachesService<User>{

    @Value("${wlpiaoyi.ee.auth.duri_minutes}")
    private int authDuriMinutes;

    @Value("${wlpiaoyi.ee.cache.duri_minutes}")
    private int cacheDuriMinutes;

    @Autowired
    private RoleCachesService roleCachesService;

    @Autowired
    private DeptCachesService deptCachesService;


    public UserVo getAuthUser(String token){
        String key = "auth_user:" + token;
        Object res = this.redisTemplate.opsForValue().get(key);
        if(res == null){
            return null;
        }
        User user = this.getCache((Long) res);
        if(user == null){
            return null;
        }
        UserVo userVo = ModelWrapper.parseOne(user, UserVo.class);
        userVo.setCurRole(this.roleCachesService.getAuthCurRole(token));
        userVo.setRoles(this.roleCachesService.getAuthRoles(token));
        return userVo;
    }

    public void setAuthUser(String token, UserVo userVo){
        String key = "auth_user:" + token;
        super.redisTemplate.opsForValue().set(key, userVo.getId(), this.authDuriMinutes, TimeUnit.SECONDS);
        User user = ModelWrapper.parseOne(userVo, User.class);
        super.setCache(user);
        if(userVo.getCurRole() != null){
            this.roleCachesService.setAuthCurRole(token, userVo.getCurRole());
        }
        if(ValueUtils.isNotBlank(userVo.getRoles())){
            this.roleCachesService.setAuthRoles(token, userVo.getRoles());
        }
    }

    public boolean expireAuthUser(String token){
        String key = "auth_user:" + token;
        if(!super.redisTemplate.expire(key, this.authDuriMinutes, TimeUnit.SECONDS)){
            return false;
        }
        this.roleCachesService.expireAuthCurRole(token);
        this.roleCachesService.expireAuthRoles(token);
        return true;
    }

    public void deleteAuthUser(String token){
        String key = "auth_user:" + token;
        super.redisTemplate.delete(key);
        this.roleCachesService.deleteAuthRoles(token);
        this.roleCachesService.deleteAuthCurRole(token);
    }

    @Override
    protected String getKeyTag() {
        return "user";
    }

    @Override
    protected long getCacheDuriMinutes() {
        return this.cacheDuriMinutes;
    }

    public User getSpringUtilsAuthUser(String token) {
        if(ValueUtils.isBlank(token)){
            return null;
        }
        String key = "auth_user:" + token;
        Object res = this.redisTemplate.opsForValue().get(key);
        if(res == null){
            return null;
        }
        return this.getCache((Long) res);
    }

    public Role getSpringUtilsAuthRole(String token) {
        String key = "token_cache_role:" + token;
        Object res = super.redisTemplate.opsForValue().get(key);
        if(res == null){
            return null;
        }
        Object[] ids = (Object[]) res;
        return this.roleCachesService.getCache((Long) ids[0]);
    }
}
