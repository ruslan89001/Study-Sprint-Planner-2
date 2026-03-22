package com.studysprint.plannerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.studysprint")
public class PlannerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlannerServiceApplication.class, args);
    }
}