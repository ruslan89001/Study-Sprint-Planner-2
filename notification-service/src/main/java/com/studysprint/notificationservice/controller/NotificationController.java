package com.studysprint.notificationservice.controller;

import com.studysprint.notificationservice.dto.*;
import com.studysprint.notificationservice.dto.internal.*;
import com.studysprint.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/api/notifications")
    public NotificationResponse createNotification(@Valid @RequestBody CreateNotificationRequest request) {
        return notificationService.createNotification(request);
    }

    @GetMapping("/api/notifications/user/{userId}")
    public List<NotificationResponse> getNotificationsByUserId(@PathVariable Long userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    @GetMapping("/api/notifications/user/{userId}/unread")
    public List<NotificationResponse> getUnreadNotificationsByUserId(@PathVariable Long userId) {
        return notificationService.getUnreadNotificationsByUserId(userId);
    }

    @PatchMapping("/api/notifications/{notificationId}/read")
    public MarkAsReadResponse markAsRead(@PathVariable Long notificationId) {
        return notificationService.markAsRead(notificationId);
    }

    @PostMapping("/api/internal/notifications/sprint-created")
    public NotificationCreatedResponse handleSprintCreated(@RequestBody SprintNotificationRequest request) {
        return notificationService.handleSprintCreated(request);
    }

    @PostMapping("/api/internal/notifications/sprint-replanned")
    public NotificationCreatedResponse handleSprintReplanned(@RequestBody SprintNotificationRequest request) {
        return notificationService.handleSprintReplanned(request);
    }

    @PostMapping("/api/internal/notifications/deadline-reminder")
    public NotificationCreatedResponse handleDeadlineReminder(@RequestBody DeadlineReminderRequest request) {
        return notificationService.handleDeadlineReminder(request);
    }

    @PostMapping("/api/internal/notifications/daily-plan")
    public NotificationCreatedResponse handleDailyPlan(@RequestBody DailyPlanRequest request) {
        return notificationService.handleDailyPlan(request);
    }
}