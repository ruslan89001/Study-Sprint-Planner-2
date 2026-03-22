package com.studysprint.taskservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskStatusResponse {
    private Long id;
    private String status;
}
