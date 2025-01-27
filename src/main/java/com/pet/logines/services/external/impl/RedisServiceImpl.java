package com.pet.logines.services.external.impl;

import com.pet.logines.services.external.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void increment(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    @Override
    public void increment(String key, long timeout) {
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

//    // Lưu dữ liệu dạng Hash
//    public void saveToHash(String key, String hashKey, Object value) {
//        redisTemplate.opsForHash().put(key, hashKey, value);
//    }
//
//    // Lấy dữ liệu từ Hash
//    public Object getFromHash(String key, String hashKey) {
//        return redisTemplate.opsForHash().get(key, hashKey);
//    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}