package com.studysprint.notificationservice.dto.internal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreatedResponse {
    private Long notificationId;
    private String status;
}