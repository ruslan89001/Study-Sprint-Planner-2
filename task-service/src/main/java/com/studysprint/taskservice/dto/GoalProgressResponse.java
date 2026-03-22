package com.studysprint.taskservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalProgressResponse {
    private Long goalId;
    private int totalTasks;
    private int doneTasks;
    private double progressPercent;
}
