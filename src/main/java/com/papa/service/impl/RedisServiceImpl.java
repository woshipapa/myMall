package com.papa.service.impl;

import com.papa.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean expire(String key, long expire) {
        return redisTemplate.expire(key,expire, TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Long increase(String key, long delta) {
        return redisTemplate.opsForValue().increment(key,delta);
    }


    public Long del(List<String> keyList){
        return redisTemplate.delete(keyList);
    }
}
