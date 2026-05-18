package com.studysprint.notificationservice.kafka;

import com.studysprint.common.event.TaskCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TaskEventConsumer {

    @KafkaListener(topics = "task-events", groupId = "notification-group")
    public void handleTaskCreated(TaskCreatedEvent event) {
        System.out.println("Новое задание: " + event.getTitle() + " для user " + event.getUserId());
        // здесь логика отправки email/push
    }
}