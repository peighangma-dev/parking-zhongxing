package com.parking.common.redis.config;

import com.parking.common.redis.utils.RedisUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RedisTemplateInitializer {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        RedisUtils.setRedisTemplate(redisTemplate);
        RedisUtils.setStringRedisTemplate(stringRedisTemplate);
    }
}
