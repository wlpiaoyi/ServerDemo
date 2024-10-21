package org.wlpiaoyi.server.demo.common.redis.configure;


import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;

import java.nio.charset.Charset;

/**
 * Redis使用FastJson序列化
 * 
 * @author ruoyi
 */
public class GsonRedisSerializer implements RedisSerializer<Object>
{
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Gson gson = GsonBuilder.gsonDefault();



    @Override
    public byte[] serialize(Object t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return (t.getClass().getName() + ":"  + gson.toJson(t)).getBytes(DEFAULT_CHARSET);
    }

    @SneakyThrows
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length == 0)
        {
            return null;
        }
        String res = new String(bytes, DEFAULT_CHARSET);
        int index = res.indexOf(":");
        Class clazz = Class.forName(res.substring(0, index));
        return gson.fromJson(res.substring(index + 1), clazz); //JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
    }
}
