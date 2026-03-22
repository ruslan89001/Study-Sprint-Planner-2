package com.studysprint.taskservice.dto;

import enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest {

    @NotNull
    private TaskStatus status;
}
