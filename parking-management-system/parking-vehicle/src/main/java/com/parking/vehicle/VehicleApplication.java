package com.parking.vehicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.parking")
public class VehicleApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleApplication.class, args);
    }
}
