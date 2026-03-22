package com.studysprint.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "daily_study_hours", nullable = false)
    private Integer dailyStudyHours;

    @Column(name = "preferred_study_time", nullable = false)
    private String preferredStudyTime;

    @Column(name = "notifications_enabled", nullable = false)
    private Boolean notificationsEnabled;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}