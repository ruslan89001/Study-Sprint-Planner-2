package com.studysprint.plannerservice.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintItemResponse {
    private Long id;
    private Long sprintId;
    private Long taskId;
    private String taskTitle;
    private LocalDate plannedDate;
    private Integer plannedHours;
    private String status;
}