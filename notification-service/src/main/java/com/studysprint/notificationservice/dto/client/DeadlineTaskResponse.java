package com.studysprint.notificationservice.dto.client;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeadlineTaskResponse {
    private Long id;
    private Long userId;
    private String title;
    private LocalDate deadline;
    private String status;
}