package com.studysprint.plannerservice.kafka;

import com.studysprint.common.event.TaskCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TaskEventConsumer {

    @KafkaListener(topics = "task-events", groupId = "planner-group")
    public void handleTaskCreated(TaskCreatedEvent event) {
        System.out.println("Planner received task: " + event.getTitle());
    }
}