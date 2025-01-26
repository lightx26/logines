package com.pet.logines.services.external;

public interface RedisService {
    void set(String key, String value);
    void set(String key, String value, long timeout);
    String get(String key);
    void delete(String key);
}
