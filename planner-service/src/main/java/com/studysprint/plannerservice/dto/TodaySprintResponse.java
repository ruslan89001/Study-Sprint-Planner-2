package com.studysprint.plannerservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodaySprintResponse {
    private LocalDate date;
    private List<TodaySprintItemResponse> items;
}