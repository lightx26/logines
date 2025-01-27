package com.pet.logines.services.external;

public interface RedisService {
    void set(String key, String value);
    void set(String key, String value, long timeout);
    void increment(String key);
    void increment(String key, long timeout);
    String get(String key);
    <T> T get(String key, Class<T> clazz);
    Long getExpire(String key);
    void delete(String key);
}
