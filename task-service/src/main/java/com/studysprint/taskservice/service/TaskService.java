package com.studysprint.taskservice.service;

import com.studysprint.taskservice.dto.*;

import java.util.List;

public interface TaskService {

    GoalResponse createGoal(CreateGoalRequest request);

    List<GoalResponse> getGoalsByUserId(Long userId);

    GoalResponse getGoalById(Long goalId);

    GoalResponse updateGoal(Long goalId, UpdateGoalRequest request);

    void deleteGoal(Long goalId);

    TaskResponse createTask(CreateTaskRequest request);

    List<TaskResponse> getTasksByGoalId(Long goalId);

    List<TaskResponse> getTasksByUserId(Long userId);

    TaskResponse getTaskById(Long taskId);

    TaskResponse updateTask(Long taskId, UpdateTaskRequest request);

    TaskStatusResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request);

    GoalProgressResponse getGoalProgress(Long goalId);

    List<PlanningTaskResponse> getPlanningTasks(Long userId);

    List<PlanningTaskResponse> getPlanningTasksByGoal(Long userId, Long goalId);

    List<DeadlineTaskResponse> getDeadlineSoonTasks(int days);

    List<DeadlineTaskResponse> getOverdueTasks();

    String exportUserTasksSnapshot(Long userId);
}
