package com.studysprint.notificationservice.service;

import com.studysprint.notificationservice.client.PlannerClient;
import com.studysprint.notificationservice.client.TaskClient;
import com.studysprint.notificationservice.client.UserClient;
import com.studysprint.notificationservice.dto.*;
import com.studysprint.notificationservice.dto.client.DeadlineTaskResponse;
import com.studysprint.notificationservice.dto.client.UserPreferencesResponse;
import com.studysprint.notificationservice.dto.internal.*;
import com.studysprint.notificationservice.entity.Notification;
import com.studysprint.common.exception.NotFoundException;
import com.studysprint.notificationservice.repository.NotificationRepository;
import enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserClient userClient;
    private final TaskClient taskClient;
    private final PlannerClient plannerClient;

    @Override
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(NotificationType.valueOf(request.getType()))
                .title(request.getTitle())
                .message(request.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);
        return mapNotification(saved);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapNotification)
                .toList();
    }

    @Override
    public List<NotificationResponse> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapNotification)
                .toList();
    }

    @Override
    public MarkAsReadResponse markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        notification.setIsRead(true);
        notificationRepository.save(notification);

        return MarkAsReadResponse.builder()
                .notificationId(notificationId)
                .status("READ")
                .build();
    }

    @Override
    public NotificationCreatedResponse handleSprintCreated(SprintNotificationRequest request) {
        Notification saved = saveInternalNotification(
                request.getUserId(),
                NotificationType.SPRINT_CREATED,
                "Спринт создан",
                request.getMessage() == null || request.getMessage().isBlank()
                        ? "Учебный спринт успешно сформирован"
                        : request.getMessage()
        );

        return buildCreatedResponse(saved);
    }

    @Override
    public NotificationCreatedResponse handleSprintReplanned(SprintNotificationRequest request) {
        Notification saved = saveInternalNotification(
                request.getUserId(),
                NotificationType.SPRINT_REPLANNED,
                "Спринт перепланирован",
                request.getMessage() == null || request.getMessage().isBlank()
                        ? "Учебный спринт был обновлен"
                        : request.getMessage()
        );

        return buildCreatedResponse(saved);
    }

    @Override
    public NotificationCreatedResponse handleDeadlineReminder(DeadlineReminderRequest request) {
        Notification saved = saveInternalNotification(
                request.getUserId(),
                NotificationType.DEADLINE_REMINDER,
                "Скоро дедлайн",
                request.getMessage() == null || request.getMessage().isBlank()
                        ? "Задача \"" + request.getTaskTitle() + "\" требует внимания"
                        : request.getMessage()
        );

        return buildCreatedResponse(saved);
    }

    @Override
    public NotificationCreatedResponse handleDailyPlan(DailyPlanRequest request) {
        int totalHours = request.getItems() == null ? 0 :
                request.getItems().stream().mapToInt(item -> item.getPlannedHours() == null ? 0 : item.getPlannedHours()).sum();

        Notification saved = saveInternalNotification(
                request.getUserId(),
                NotificationType.DAILY_PLAN,
                "План на сегодня",
                "На " + request.getDate() + " запланировано задач: "
                        + (request.getItems() == null ? 0 : request.getItems().size())
                        + ", часов: " + totalHours
        );

        return buildCreatedResponse(saved);
    }

    @Override
    public void createDeadlineSoonNotifications(int days) {
        List<DeadlineTaskResponse> tasks = taskClient.getDeadlineSoonTasks(days);

        for (DeadlineTaskResponse task : tasks) {
            UserPreferencesResponse preferences = userClient.getPreferences(task.getUserId());
            if (preferences.getNotificationsEnabled() == null || !preferences.getNotificationsEnabled()) {
                continue;
            }

            saveInternalNotification(
                    task.getUserId(),
                    NotificationType.DEADLINE_REMINDER,
                    "Скоро дедлайн",
                    "Задача \"" + task.getTitle() + "\" должна быть выполнена до " + task.getDeadline()
            );
        }
    }

    @Override
    public void createOverdueNotifications() {
        List<DeadlineTaskResponse> tasks = taskClient.getOverdueTasks();

        for (DeadlineTaskResponse task : tasks) {
            UserPreferencesResponse preferences = userClient.getPreferences(task.getUserId());
            if (preferences.getNotificationsEnabled() == null || !preferences.getNotificationsEnabled()) {
                continue;
            }

            saveInternalNotification(
                    task.getUserId(),
                    NotificationType.OVERDUE_ALERT,
                    "Задача просрочена",
                    "Задача \"" + task.getTitle() + "\" просрочена с " + task.getDeadline()
            );
        }
    }

    private Notification saveInternalNotification(Long userId,
                                                  NotificationType type,
                                                  String title,
                                                  String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        return notificationRepository.save(notification);
    }

    private NotificationCreatedResponse buildCreatedResponse(Notification notification) {
        return NotificationCreatedResponse.builder()
                .notificationId(notification.getId())
                .status("CREATED")
                .build();
    }

    private NotificationResponse mapNotification(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .type(notification.getType().name())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}