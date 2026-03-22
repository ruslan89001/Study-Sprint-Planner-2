package com.studysprint.taskservice.controller;

import com.studysprint.taskservice.dto.*;
import com.studysprint.taskservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/api/goals")
    public GoalResponse createGoal(@Valid @RequestBody CreateGoalRequest request) {
        return taskService.createGoal(request);
    }

    @GetMapping("/api/goals/user/{userId}")
    public List<GoalResponse> getGoalsByUserId(@PathVariable Long userId) {
        return taskService.getGoalsByUserId(userId);
    }

    @GetMapping("/api/goals/{goalId}")
    public GoalResponse getGoalById(@PathVariable Long goalId) {
        return taskService.getGoalById(goalId);
    }

    @PutMapping("/api/goals/{goalId}")
    public GoalResponse updateGoal(@PathVariable Long goalId,
                                   @Valid @RequestBody UpdateGoalRequest request) {
        return taskService.updateGoal(goalId, request);
    }

    @DeleteMapping("/api/goals/{goalId}")
    public void deleteGoal(@PathVariable Long goalId) {
        taskService.deleteGoal(goalId);
    }

    @PostMapping("/api/tasks")
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/api/tasks/goal/{goalId}")
    public List<TaskResponse> getTasksByGoalId(@PathVariable Long goalId) {
        return taskService.getTasksByGoalId(goalId);
    }

    @GetMapping("/api/tasks/user/{userId}")
    public List<TaskResponse> getTasksByUserId(@PathVariable Long userId) {
        return taskService.getTasksByUserId(userId);
    }

    @GetMapping("/api/tasks/{taskId}")
    public TaskResponse getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @PutMapping("/api/tasks/{taskId}")
    public TaskResponse updateTask(@PathVariable Long taskId,
                                   @Valid @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(taskId, request);
    }

    @PatchMapping("/api/tasks/{taskId}/status")
    public TaskStatusResponse updateTaskStatus(@PathVariable Long taskId,
                                               @Valid @RequestBody UpdateTaskStatusRequest request) {
        return taskService.updateTaskStatus(taskId, request);
    }

    @GetMapping("/api/goals/{goalId}/progress")
    public GoalProgressResponse getGoalProgress(@PathVariable Long goalId) {
        return taskService.getGoalProgress(goalId);
    }

    @GetMapping("/api/internal/tasks/planning/{userId}")
    public List<PlanningTaskResponse> getPlanningTasks(@PathVariable Long userId) {
        return taskService.getPlanningTasks(userId);
    }

    @GetMapping("/api/internal/tasks/planning/{userId}/goal/{goalId}")
    public List<PlanningTaskResponse> getPlanningTasksByGoal(@PathVariable Long userId,
                                                             @PathVariable Long goalId) {
        return taskService.getPlanningTasksByGoal(userId, goalId);
    }

    @GetMapping("/api/internal/tasks/deadline-soon")
    public List<DeadlineTaskResponse> getDeadlineSoonTasks(@RequestParam int days) {
        return taskService.getDeadlineSoonTasks(days);
    }

    @GetMapping("/api/internal/tasks/overdue")
    public List<DeadlineTaskResponse> getOverdueTasks() {
        return taskService.getOverdueTasks();
    }
}
