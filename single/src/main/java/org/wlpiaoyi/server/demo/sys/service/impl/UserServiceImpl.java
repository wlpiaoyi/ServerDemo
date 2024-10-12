package org.wlpiaoyi.server.demo.sys.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.wlpiaoyi.server.demo.sys.service.IUserService;
import org.wlpiaoyi.server.demo.sys.domain.entity.User;
import org.wlpiaoyi.server.demo.sys.domain.mapper.UserMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.UserVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.UserRo;
import org.wlpiaoyi.server.demo.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


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

    @Value("${wlpiaoyi.ee.auth.duri_minutes}")
    private int authDuriMinutes;

    @Override
    public UserVo login(String token, UserRo.UserAuth auth) {
        this.redisTemplate.opsForValue().set(token, System.currentTimeMillis() + "", this.authDuriMinutes, TimeUnit.MINUTES);
        return null;
    }
}
