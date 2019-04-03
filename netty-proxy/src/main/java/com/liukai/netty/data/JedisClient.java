package com.liukai.netty.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

public class JedisClient {
    private final Logger logger = LoggerFactory.getLogger(JedisClient.class);

    private JedisPool jedisPool = new JedisPool();

    public String hget(){
        Jedis jedis = this.jedisPool.getResource();
        String address = jedis.hget("server","address");
        jedis.close();
        return address;
    }

    public Map<String,String> hGetAll(String key){
        Jedis jedis = this.jedisPool.getResource();
        Map<String,String> address = jedis.hgetAll(key);
        jedis.close();
        return address;
    }

    public void hset(String key, String host, String port){

        Jedis jedis = jedisPool.getResource();
        jedis.hset(key,"host", host);
        jedis.hset(key,"port", port);
        jedis.expire(key,30);
        jedis.close();

    }
}
