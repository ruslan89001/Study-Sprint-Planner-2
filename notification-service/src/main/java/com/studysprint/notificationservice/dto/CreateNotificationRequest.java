package com.studysprint.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String type;

    @NotBlank
    private String title;

    @NotBlank
    private String message;
}