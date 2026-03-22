package com.studysprint.plannerservice.dto.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintNotificationRequest {
    private Long userId;
    private Long sprintId;
    private String message;
}