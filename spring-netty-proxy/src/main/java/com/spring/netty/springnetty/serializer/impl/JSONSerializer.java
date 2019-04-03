package com.spring.netty.springnetty.serializer.impl;


import com.alibaba.fastjson.JSON;
import com.spring.netty.springnetty.serializer.Serializer;
import org.springframework.stereotype.Component;

@Component
public class JSONSerializer implements Serializer {

    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
