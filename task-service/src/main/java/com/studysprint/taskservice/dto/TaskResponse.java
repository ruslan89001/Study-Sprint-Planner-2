package com.studysprint.taskservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private Long goalId;
    private Long userId;
    private String title;
    private String description;
    private String priority;
    private LocalDate deadline;
    private Integer estimatedHours;
    private String status;
}