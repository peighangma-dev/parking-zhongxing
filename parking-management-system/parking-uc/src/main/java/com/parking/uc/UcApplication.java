package com.parking.uc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.parking")
public class UcApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcApplication.class, args);
    }
}
