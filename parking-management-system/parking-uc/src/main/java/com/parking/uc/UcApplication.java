package com.parking.uc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.parking", 
    exclude = {
        RedisAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class
    })
public class UcApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcApplication.class, args);
    }
}
