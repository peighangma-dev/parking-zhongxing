package com.parking.barrier;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.parking",
    exclude = { MybatisPlusAutoConfiguration.class })
@MapperScan("com.parking.barrier.mapper")
public class BarrierApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarrierApplication.class, args);
    }
}
