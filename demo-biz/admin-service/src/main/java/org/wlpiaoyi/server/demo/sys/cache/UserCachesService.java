package org.wlpiaoyi.server.demo.sys.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.server.demo.common.datasource.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.sys.domain.entity.User;
import org.wlpiaoyi.server.demo.sys.domain.vo.UserVo;

import java.util.concurrent.TimeUnit;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-14 15:44:27</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Service
public class UserCachesService extends CachesService<User> {

    @Value("${wlpiaoyi.ee.auth.duri_minutes}")
    private int authDuriMinutes;

    @Value("${wlpiaoyi.ee.cache.duri_minutes}")
    private int cacheDuriMinutes;

    @Autowired
    private RoleCachesService roleCachesService;

    @Autowired
    private DeptCachesService deptCachesService;

    private final String keyTag = "user";

    @Override
    protected String getKeyTag() {
        return this.keyTag;
    }

    @Override
    protected long getCacheDuriMinutes() {
        return this.cacheDuriMinutes;
    }

    public User getAuthUser(String token) {
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


    public UserVo getAuthUserVo(String token){
        User user = this.getAuthUser(token);
        if(user == null){
            return null;
        }
        UserVo userVo = ModelWrapper.parseOne(user, UserVo.class);
        userVo.setCurRole(this.roleCachesService.getCurAuthRoleVo(token));
        userVo.setRoles(this.roleCachesService.getAuthRoleVos(token));
        return userVo;
    }


    public void setAuthUserVo(String token, UserVo userVo){
        String key = "auth_user:" + token;
        super.redisTemplate.opsForValue().set(key, userVo.getId(), this.authDuriMinutes, TimeUnit.SECONDS);
        User user = ModelWrapper.parseOne(userVo, User.class);
        super.setCache(user);
        if(userVo.getCurRole() != null){
            this.roleCachesService.setCurAuthRoleVo(token, userVo.getCurRole());
        }
        if(ValueUtils.isNotBlank(userVo.getRoles())){
            this.roleCachesService.setAuthRoleVos(token, userVo.getRoles());
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
}
