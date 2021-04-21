package com.rubber.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.rubber.project.mapper")
@EnableScheduling
public class HotelDataDockApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelDataDockApplication.class, args);
    }

}
