package com.studysprint.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreatedEvent {
    private Long taskId;
    private Long userId;
    private String title;
    private String priority;
}