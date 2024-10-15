package org.wlpiaoyi.server.demo.config.web.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence.IdempotenceMoonMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 11:01:34</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class IdempotenceMoonMapImpl implements IdempotenceMoonMap {

    class PopRunnable implements Runnable{

        private long millis;
        private RedisTemplate redisTemplate;
        private String key;

        PopRunnable(String key, RedisTemplate redisTemplate, long millis){
            this.key = key;
            this.redisTemplate = redisTemplate;
            this.millis = millis;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(this.millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.redisTemplate.opsForList().rightPop(key);
        }
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long get(String key) {
        Thread.startVirtualThread(new PopRunnable(key, this.redisTemplate, 10000)).start();
        return this.redisTemplate.opsForList().rightPush(key, 1);
    }

    @Override
    public void put(String key, Long value, Integer duriTime) {
        this.redisTemplate.opsForValue().set(key, value, duriTime, TimeUnit.MILLISECONDS);
    }


}
