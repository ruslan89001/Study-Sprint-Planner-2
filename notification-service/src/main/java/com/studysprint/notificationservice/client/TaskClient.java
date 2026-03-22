package com.studysprint.notificationservice.client;

import com.studysprint.notificationservice.dto.client.DeadlineTaskResponse;
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

    public List<DeadlineTaskResponse> getDeadlineSoonTasks(int days) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/internal/tasks/deadline-soon")
                        .queryParam("days", days)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<DeadlineTaskResponse>>() {});
    }

    public List<DeadlineTaskResponse> getOverdueTasks() {
        return restClient.get()
                .uri("/api/internal/tasks/overdue")
                .retrieve()
                .body(new ParameterizedTypeReference<List<DeadlineTaskResponse>>() {});
    }
}