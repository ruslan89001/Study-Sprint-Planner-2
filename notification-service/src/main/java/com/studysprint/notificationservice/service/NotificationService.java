package com.studysprint.notificationservice.service;

import com.studysprint.notificationservice.dto.*;
import com.studysprint.notificationservice.dto.internal.*;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(CreateNotificationRequest request);

    List<NotificationResponse> getNotificationsByUserId(Long userId);

    List<NotificationResponse> getUnreadNotificationsByUserId(Long userId);

    MarkAsReadResponse markAsRead(Long notificationId);

    NotificationCreatedResponse handleSprintCreated(SprintNotificationRequest request);

    NotificationCreatedResponse handleSprintReplanned(SprintNotificationRequest request);

    NotificationCreatedResponse handleDeadlineReminder(DeadlineReminderRequest request);

    NotificationCreatedResponse handleDailyPlan(DailyPlanRequest request);

    void createDeadlineSoonNotifications(int days);

    void createOverdueNotifications();
}