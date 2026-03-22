package com.studysprint.taskservice.service;

import com.studysprint.taskservice.dto.*;
import enums.GoalStatus;
import enums.TaskStatus;
import com.studysprint.taskservice.entity.*;
import com.studysprint.taskservice.exception.NotFoundException;
import com.studysprint.taskservice.repository.GoalRepository;
import com.studysprint.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;

    @Override
    public GoalResponse createGoal(CreateGoalRequest request) {
        Goal goal = Goal.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .status(GoalStatus.ACTIVE)
                .build();

        Goal saved = goalRepository.save(goal);
        return mapGoal(saved);
    }

    @Override
    public List<GoalResponse> getGoalsByUserId(Long userId) {
        return goalRepository.findByUserId(userId)
                .stream()
                .map(this::mapGoal)
                .toList();
    }

    @Override
    public GoalResponse getGoalById(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        return mapGoal(goal);
    }

    @Override
    public GoalResponse updateGoal(Long goalId, UpdateGoalRequest request) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("Goal not found"));

        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setDeadline(request.getDeadline());
        goal.setStatus(request.getStatus());

        Goal updated = goalRepository.save(goal);
        return mapGoal(updated);
    }

    @Override
    public void deleteGoal(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        goalRepository.delete(goal);
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        Goal goal = goalRepository.findById(request.getGoalId())
                .orElseThrow(() -> new NotFoundException("Goal not found"));

        Task task = Task.builder()
                .goalId(goal.getId())
                .userId(request.getUserId())
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .deadline(request.getDeadline())
                .estimatedHours(request.getEstimatedHours())
                .status(request.getStatus())
                .build();

        Task saved = taskRepository.save(task);
        return mapTask(saved);
    }

    @Override
    public List<TaskResponse> getTasksByGoalId(Long goalId) {
        return taskRepository.findByGoalId(goalId)
                .stream()
                .map(this::mapTask)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::mapTask)
                .toList();
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        return mapTask(task);
    }

    @Override
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDeadline(request.getDeadline());
        task.setEstimatedHours(request.getEstimatedHours());
        task.setStatus(request.getStatus());

        Task updated = taskRepository.save(task);
        return mapTask(updated);
    }

    @Override
    public TaskStatusResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        task.setStatus(request.getStatus());
        Task updated = taskRepository.save(task);

        return TaskStatusResponse.builder()
                .id(updated.getId())
                .status(updated.getStatus().name())
                .build();
    }

    @Override
    public GoalProgressResponse getGoalProgress(Long goalId) {
        goalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("Goal not found"));

        int total = taskRepository.countByGoalId(goalId);
        int done = taskRepository.countByGoalIdAndStatus(goalId, TaskStatus.DONE);
        double progress = total == 0 ? 0.0 : ((double) done / total) * 100.0;

        return GoalProgressResponse.builder()
                .goalId(goalId)
                .totalTasks(total)
                .doneTasks(done)
                .progressPercent(progress)
                .build();
    }

    @Override
    public List<PlanningTaskResponse> getPlanningTasks(Long userId) {
        return taskRepository.findByUserIdAndStatusIn(userId, List.of(TaskStatus.NEW, TaskStatus.IN_PROGRESS))
                .stream()
                .map(this::mapPlanningTask)
                .toList();
    }

    @Override
    public List<PlanningTaskResponse> getPlanningTasksByGoal(Long userId, Long goalId) {
        return taskRepository.findByUserIdAndGoalIdAndStatusIn(userId, goalId, List.of(TaskStatus.NEW, TaskStatus.IN_PROGRESS))
                .stream()
                .map(this::mapPlanningTask)
                .toList();
    }

    @Override
    public List<DeadlineTaskResponse> getDeadlineSoonTasks(int days) {
        LocalDate targetDate = LocalDate.now().plusDays(days);
        return taskRepository.findByDeadlineLessThanEqualAndStatusIn(targetDate, List.of(TaskStatus.NEW, TaskStatus.IN_PROGRESS))
                .stream()
                .map(this::mapDeadlineTask)
                .toList();
    }

    @Override
    public List<DeadlineTaskResponse> getOverdueTasks() {
        LocalDate today = LocalDate.now();
        return taskRepository.findByDeadlineBeforeAndStatusIn(today, List.of(TaskStatus.NEW, TaskStatus.IN_PROGRESS))
                .stream()
                .map(this::mapDeadlineTask)
                .toList();
    }

    private GoalResponse mapGoal(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .userId(goal.getUserId())
                .title(goal.getTitle())
                .description(goal.getDescription())
                .deadline(goal.getDeadline())
                .status(goal.getStatus().name())
                .build();
    }

    private TaskResponse mapTask(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .goalId(task.getGoalId())
                .userId(task.getUserId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().name())
                .deadline(task.getDeadline())
                .estimatedHours(task.getEstimatedHours())
                .status(task.getStatus().name())
                .build();
    }

    private PlanningTaskResponse mapPlanningTask(Task task) {
        return PlanningTaskResponse.builder()
                .id(task.getId())
                .goalId(task.getGoalId())
                .userId(task.getUserId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().name())
                .deadline(task.getDeadline())
                .estimatedHours(task.getEstimatedHours())
                .status(task.getStatus().name())
                .build();
    }

    private DeadlineTaskResponse mapDeadlineTask(Task task) {
        return DeadlineTaskResponse.builder()
                .id(task.getId())
                .userId(task.getUserId())
                .title(task.getTitle())
                .deadline(task.getDeadline())
                .status(task.getStatus().name())
                .build();
    }
}
