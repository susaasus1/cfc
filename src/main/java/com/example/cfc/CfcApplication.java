package com.example.cfc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Objects;

@SpringBootApplication
@EnableScheduling
public class CfcApplication {

    public static void main(String[] args) {
        SpringApplication.run(CfcApplication.class, args);
    }

}