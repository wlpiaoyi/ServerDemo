package org.wlpiaoyi.server.demo.sys.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.wlpiaoyi.server.demo.common.datasource.domain.entity.BaseEntity;

import java.util.concurrent.TimeUnit;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-14 15:24:40</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
public abstract class CachesService<T extends BaseEntity> {

    protected abstract String getKeyTag();

    @Autowired
    protected RedisTemplate redisTemplate;

    protected abstract long getCacheDuriMinutes();


    protected void set(T entity, long minutes){
        this.redisTemplate.opsForValue().set("cache_db_" + this.getKeyTag() + entity.getId(), entity, minutes, TimeUnit.MINUTES);
    }
    protected T get(Long id){
        Object res = this.redisTemplate.opsForValue().get("cache_db_" + this.getKeyTag() + id);
        if(res == null){
            return null;
        }
        return (T) res;
    }


    protected boolean expire(Long id, long minutes){
        return this.redisTemplate.expire("cache_db_" + this.getKeyTag() + id, minutes, TimeUnit.MINUTES);
    }


    public void setCache(T entity){
        this.set(entity, this.getCacheDuriMinutes());
    }

    public T getCache(Long id){
        return this.get(id);
    }

    public boolean remove(Long id){
        return this.redisTemplate.delete("cache_db_" + this.getKeyTag() + id);
    }

    public boolean expire(Long id){
        return this.expire(id, this.getCacheDuriMinutes());
    }


}
