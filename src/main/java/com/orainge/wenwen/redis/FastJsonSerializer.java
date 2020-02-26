package com.orainge.wenwen.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * 要实现对象的缓存，定义自己的序列化和反序列化器。使用阿里的 Fastjson 来实现的比较多。
 */
public class FastJsonSerializer {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static <T> byte[] serialize(T t) throws SerializationException {
        if (null == t) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
        if (null == bytes || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return (T) JSON.parseObject(str, clazz);
    }
}