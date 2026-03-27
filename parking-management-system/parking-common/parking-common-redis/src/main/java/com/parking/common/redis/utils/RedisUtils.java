package com.parking.common.redis.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;
    private static StringRedisTemplate stringRedisTemplate;

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisUtils.stringRedisTemplate = stringRedisTemplate;
    }

    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public static <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public static Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public static Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    public static Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    public static void setIfAbsent(String key, Object value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public static void setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    public static Boolean setIfPresent(String key, Object value) {
        return redisTemplate.opsForValue().setIfPresent(key, value);
    }

    public static Boolean setIfPresent(String key, Object value, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfPresent(key, value, timeout, unit);
    }

    public static Long increment(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    public static Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    public static Long decrement(String key) {
        return stringRedisTemplate.opsForValue().decrement(key);
    }

    public static Long decrement(String key, long delta) {
        return stringRedisTemplate.opsForValue().decrement(key, delta);
    }

    public static Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public static void hashPut(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public static void hashPutAll(String key, java.util.Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public static Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public static java.util.Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public static Boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public static Long hashDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public static Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    public static void listPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public static void listPushAll(String key, Collection<Object> values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    public static Object listPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public static Object listIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public static Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public static java.util.List<Object> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }
}
