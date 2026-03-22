package com.studysprint.taskservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/tasks/health")
    public Map<String, String> health() {
        return Map.of(
                "service", "task-service",
                "status", "UP"
        );
    }
}
