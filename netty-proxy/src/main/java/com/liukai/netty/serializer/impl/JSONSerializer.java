package com.liukai.netty.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.liukai.netty.serializer.Serializer;


public class JSONSerializer implements Serializer {

    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
