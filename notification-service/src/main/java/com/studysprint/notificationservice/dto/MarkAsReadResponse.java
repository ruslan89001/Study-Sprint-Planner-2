package com.studysprint.notificationservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkAsReadResponse {
    private Long notificationId;
    private String status;
}