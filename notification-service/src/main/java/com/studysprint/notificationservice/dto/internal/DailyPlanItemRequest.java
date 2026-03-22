package com.studysprint.notificationservice.dto.internal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPlanItemRequest {
    private Long taskId;
    private String title;
    private Integer plannedHours;
}