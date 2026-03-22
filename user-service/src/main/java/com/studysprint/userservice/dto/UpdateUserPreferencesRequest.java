package com.studysprint.userservice.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserPreferencesRequest {

    @NotNull(message = "Daily study hours must not be null")
    @Min(value = 1, message = "Daily study hours must be at least 1")
    @Max(value = 24, message = "Daily study hours must be at most 24")
    private Integer dailyStudyHours;

    @NotBlank(message = "Preferred study time must not be blank")
    private String preferredStudyTime;

    @NotNull(message = "Notifications enabled must not be null")
    private Boolean notificationsEnabled;
}
