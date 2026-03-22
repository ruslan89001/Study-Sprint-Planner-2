package com.studysprint.taskservice.dto;

import enums.TaskPriority;
import enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskRequest {

    @NotNull
    private Long goalId;

    @NotNull
    private Long userId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private TaskPriority priority;

    @NotNull
    @FutureOrPresent
    private LocalDate deadline;

    @NotNull
    @Min(1)
    private Integer estimatedHours;

    @NotNull
    private TaskStatus status;
}
