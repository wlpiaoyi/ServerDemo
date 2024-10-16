package org.wlpiaoyi.server.demo.config.web.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence.IdempotenceMoon;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 11:01:34</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class IdempotenceMoonImpl implements IdempotenceMoon {


    class PopRunnable implements Runnable{

        private int duriSecond;
        private RedisTemplate redisTemplate;
        private String key;

        PopRunnable(String key, RedisTemplate redisTemplate, int duriSecond){
            this.key = key;
            this.redisTemplate = redisTemplate;
            this.duriSecond = duriSecond;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(this.duriSecond * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.redisTemplate.opsForList().rightPop(key);
        }
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean isIdempotence(String key, int duriSecond, int count, int deadLockMinutes) {
        String lockKey = "lock:" + key;
        if(this.redisTemplate.hasKey(lockKey)){
            return true;
        }
        String duriKey = "duri:" + key;
        Thread.startVirtualThread(new PopRunnable(duriKey, this.redisTemplate, duriSecond));
        long value = this.redisTemplate.opsForList().rightPush(duriKey, 1);
        this.redisTemplate.expire(duriKey, duriSecond, TimeUnit.MILLISECONDS);
        if(value > count){
            this.redisTemplate.opsForValue().set(lockKey, "1", deadLockMinutes, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }



}
