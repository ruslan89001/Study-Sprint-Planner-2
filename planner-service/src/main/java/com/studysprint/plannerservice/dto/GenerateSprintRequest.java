package com.studysprint.plannerservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateSprintRequest {

    @NotNull
    private Long userId;

    private Long goalId;

    @NotNull
    private LocalDate periodStart;

    @NotNull
    private LocalDate periodEnd;
}