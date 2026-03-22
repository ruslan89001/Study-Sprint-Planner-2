package com.studysprint.plannerservice.client;

import com.studysprint.plannerservice.dto.client.PlanningTaskResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class TaskClient {

    private final RestClient restClient;

    public TaskClient(@Value("${services.task-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<PlanningTaskResponse> getPlanningTasks(Long userId) {
        return restClient.get()
                .uri("/api/internal/tasks/planning/{userId}", userId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PlanningTaskResponse>>() {});
    }

    public List<PlanningTaskResponse> getPlanningTasksByGoal(Long userId, Long goalId) {
        return restClient.get()
                .uri("/api/internal/tasks/planning/{userId}/goal/{goalId}", userId, goalId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PlanningTaskResponse>>() {});
    }
}