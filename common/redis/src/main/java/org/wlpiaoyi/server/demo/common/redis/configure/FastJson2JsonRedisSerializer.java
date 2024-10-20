package org.wlpiaoyi.server.demo.common.redis.configure;


import com.google.gson.Gson;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;

import java.nio.charset.Charset;

/**
 * Redis使用FastJson序列化
 * 
 * @author ruoyi
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T>
{
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    static final Gson gson = GsonBuilder.gsonDefault();

    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return gson.toJson(t).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0)
        {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return gson.fromJson(str, clazz); //JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
    }
}
