package org.wlpiaoyi.server.demo.test.utils.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence.IdempotenceMoonMap;

import java.util.concurrent.TimeUnit;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 11:01:34</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class IdempotenceMoonMapImpl implements IdempotenceMoonMap {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long get(String key) {
        Object value = this.redisTemplate.opsForValue().get(key);
        if(value == null){
            return null;
        }
        return (Long) value;
    }

    @Override
    public void put(String key, Long value, Integer duriTime) {
        this.redisTemplate.opsForValue().set(key, value, duriTime, TimeUnit.MILLISECONDS);
    }


}
