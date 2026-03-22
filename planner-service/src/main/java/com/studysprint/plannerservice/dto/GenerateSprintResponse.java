package com.studysprint.plannerservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateSprintResponse {
    private Long sprintId;
    private String status;
    private Integer itemsCount;
}