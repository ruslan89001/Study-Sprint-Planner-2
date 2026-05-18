package com.studysprint.taskservice.service;

import com.studysprint.taskservice.dto.*;
import enums.GoalStatus;
import enums.TaskStatus;
import com.studysprint.taskservice.entity.*;
import com.studysprint.common.exception.NotFoundException;
import com.studysprint.taskservice.repository.GoalRepository;
import com.studysprint.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.studysprint.common.event.TaskCreatedEvent;
import com.studysprint.taskservice.kafka.TaskEventProducer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;
    private final TaskEventProducer taskEventProducer;
    private final SnapshotStorageService snapshotStorageService;

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

    @Cacheable(value = "goalsByUserId", key = "#userId")
    @Override
    public List<GoalResponse> getGoalsByUserId(Long userId) {
        return goalRepository.findByUserId(userId)
                .stream()
                .map(this::mapGoal)
                .toList();
    }

    @Cacheable(value = "goalById", key = "#goalId")
    @Override
    public GoalResponse getGoalById(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        return mapGoal(goal);
    }

    @CacheEvict(value = "goalById", key = "#goalId")
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

    @CacheEvict(value = "goalById", key = "#goalId")
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

        taskEventProducer.sendTaskCreated(
                new TaskCreatedEvent(
                        saved.getId(),
                        saved.getUserId(),
                        saved.getTitle(),
                        saved.getPriority().name()
                )
        );

        return mapTask(saved);
    }

    @Cacheable(value = "tasksByGoalId", key = "#goalId")
    @Override
    public List<TaskResponse> getTasksByGoalId(Long goalId) {
        return taskRepository.findByGoalId(goalId)
                .stream()
                .map(this::mapTask)
                .toList();
    }

    @Cacheable(value = "tasksByUserId", key = "#userId")
    @Override
    public List<TaskResponse> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::mapTask)
                .toList();
    }

    @Cacheable(value = "taskById", key = "#taskId")
    @Override
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        return mapTask(task);
    }

    @CacheEvict(value = "taskById", key = "#taskId")
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

    @CacheEvict(value = "taskById", key = "#taskId")
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

    @Cacheable(value = "goalProgress", key = "#goalId")
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

    @Cacheable(value = "planningTasks", key = "#userId")
    @Override
    public List<PlanningTaskResponse> getPlanningTasks(Long userId) {
        return taskRepository.findByUserIdAndStatusIn(userId, List.of(TaskStatus.NEW, TaskStatus.IN_PROGRESS))
                .stream()
                .map(this::mapPlanningTask)
                .toList();
    }

    @Cacheable(value = "planningTasksByGoal", key = "#userId + '-' + #goalId")
    @Override
    public List<PlanningTaskResponse> getPlanningTasksByGoal(Long userId, Long goalId) {
        return taskRepository.findByUserIdAndGoalIdAndStatusIn(userId, goalId, List.of(TaskStatus.NEW, TaskStatus.IN_PROGRESS))
                .stream()
                .map(this::mapPlanningTask)
                .toList();
    }

    @Cacheable(value = "deadlineSoonTasks", key = "#days")
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

    @Override
    public String exportUserTasksSnapshot(Long userId) {
        List<TaskResponse> tasks = getTasksByUserId(userId);

        Map<String, Object> snapshot = Map.of(
                "userId", userId,
                "exportedAt", java.time.OffsetDateTime.now().toString(),
                "taskCount", tasks.size(),
                "tasks", tasks
        );

        String objectName = "users/%d/tasks-%d.json".formatted(userId, System.currentTimeMillis());
        return snapshotStorageService.uploadJsonSnapshot(objectName, snapshot);
    }
}
