package com.example.mybatisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MybatisdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisdemoApplication.class, args);
    }

}
