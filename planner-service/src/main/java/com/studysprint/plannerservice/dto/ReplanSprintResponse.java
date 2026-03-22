package com.studysprint.plannerservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplanSprintResponse {
    private Long sprintId;
    private String status;
    private Integer itemsCount;
}