package com.papa.service;

public interface RedisService {
    void set(String key,Object value);

    Object get(String key);

    boolean expire(String key,long expire);

    void remove(String key);


    Long increase(String key,long delta);


}
