package com.studysprint.notificationservice.dto.internal;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPlanRequest {
    private Long userId;
    private LocalDate date;
    private List<DailyPlanItemRequest> items;
}