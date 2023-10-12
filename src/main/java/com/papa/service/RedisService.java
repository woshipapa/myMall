package com.papa.service;

import java.util.List;

public interface RedisService {
    void set(String key,Object value);

    Object get(String key);

    boolean expire(String key,long expire);

    void remove(String key);


    Long increase(String key,long delta);

    Long del(List<String> keys);
}
