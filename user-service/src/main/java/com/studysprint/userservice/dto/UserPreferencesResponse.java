package com.studysprint.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPreferencesResponse {
    private Long userId;
    private Integer dailyStudyHours;
    private String preferredStudyTime;
    private Boolean notificationsEnabled;
}