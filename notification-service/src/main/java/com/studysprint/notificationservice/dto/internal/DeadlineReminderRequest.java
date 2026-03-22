package com.studysprint.notificationservice.dto.internal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeadlineReminderRequest {
    private Long userId;
    private Long taskId;
    private String taskTitle;
    private String message;
}