package com.studysprint.plannerservice.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintResponse {
    private Long id;
    private Long userId;
    private Long goalId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String status;
}