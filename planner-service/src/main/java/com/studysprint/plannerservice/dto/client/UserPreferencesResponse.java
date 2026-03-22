package com.studysprint.plannerservice.dto.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferencesResponse {
    private Long userId;
    private Integer dailyStudyHours;
    private String preferredStudyTime;
    private Boolean notificationsEnabled;
}