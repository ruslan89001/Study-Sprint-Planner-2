package com.studysprint.taskservice.repository;

import com.studysprint.taskservice.entity.Task;
import enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByGoalId(Long goalId);

    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndStatusIn(Long userId, List<TaskStatus> statuses);

    List<Task> findByUserIdAndGoalIdAndStatusIn(Long userId, Long goalId, List<TaskStatus> statuses);

    List<Task> findByDeadlineLessThanEqualAndStatusIn(LocalDate deadline, List<TaskStatus> statuses);

    List<Task> findByDeadlineBeforeAndStatusIn(LocalDate deadline, List<TaskStatus> statuses);

    int countByGoalId(Long goalId);

    int countByGoalIdAndStatus(Long goalId, TaskStatus status);
}
