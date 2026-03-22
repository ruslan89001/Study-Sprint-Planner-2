package com.studysprint.plannerservice.dto.client;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanningTaskResponse {
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