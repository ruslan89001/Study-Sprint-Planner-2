package com.studysprint.taskservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GoalResponse {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private LocalDate deadline;
    private String status;
}