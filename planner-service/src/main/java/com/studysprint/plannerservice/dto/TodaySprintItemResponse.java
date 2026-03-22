package com.studysprint.plannerservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodaySprintItemResponse {
    private Long taskId;
    private String taskTitle;
    private Integer plannedHours;
}