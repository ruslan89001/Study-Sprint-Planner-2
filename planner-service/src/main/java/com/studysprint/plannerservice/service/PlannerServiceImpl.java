package com.studysprint.plannerservice.service;

import com.studysprint.plannerservice.client.NotificationClient;
import com.studysprint.plannerservice.client.TaskClient;
import com.studysprint.plannerservice.client.UserClient;
import com.studysprint.plannerservice.dto.*;
import com.studysprint.plannerservice.dto.client.NotificationResponse;
import com.studysprint.plannerservice.dto.client.PlanningTaskResponse;
import com.studysprint.plannerservice.dto.client.SprintNotificationRequest;
import com.studysprint.plannerservice.dto.client.UserPreferencesResponse;
import com.studysprint.plannerservice.entity.Sprint;
import com.studysprint.plannerservice.entity.SprintItem;
import enums.SprintItemStatus;
import enums.SprintStatus;
import com.studysprint.common.exception.BadRequestException;
import com.studysprint.common.exception.NotFoundException;
import com.studysprint.plannerservice.repository.SprintItemRepository;
import com.studysprint.plannerservice.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerServiceImpl implements PlannerService {

    private final SprintRepository sprintRepository;
    private final SprintItemRepository sprintItemRepository;
    private final UserClient userClient;
    private final TaskClient taskClient;
    private final NotificationClient notificationClient;

    @Override
    @Transactional
    public GenerateSprintResponse generateSprint(GenerateSprintRequest request) {
        validatePeriod(request.getPeriodStart(), request.getPeriodEnd());

        UserPreferencesResponse preferences = userClient.getPreferences(request.getUserId());
        Integer dailyStudyHours = preferences.getDailyStudyHours();

        if (dailyStudyHours == null || dailyStudyHours <= 0) {
            throw new BadRequestException("dailyStudyHours must be greater than 0");
        }

        List<PlanningTaskResponse> tasks = request.getGoalId() == null
                ? taskClient.getPlanningTasks(request.getUserId())
                : taskClient.getPlanningTasksByGoal(request.getUserId(), request.getGoalId());

        Sprint sprint = Sprint.builder()
                .userId(request.getUserId())
                .goalId(request.getGoalId())
                .periodStart(request.getPeriodStart())
                .periodEnd(request.getPeriodEnd())
                .status(SprintStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        Sprint savedSprint = sprintRepository.save(sprint);
        List<SprintItem> items = buildSprintItems(savedSprint.getId(), tasks, request.getPeriodStart(), request.getPeriodEnd(), dailyStudyHours);
        sprintItemRepository.saveAll(items);

        savedSprint.setStatus(SprintStatus.ACTIVE);
        sprintRepository.save(savedSprint);

        notificationClient.notifySprintCreated(
                SprintNotificationRequest.builder()
                        .userId(savedSprint.getUserId())
                        .sprintId(savedSprint.getId())
                        .message("Учебный спринт успешно сформирован")
                        .build()
        );

        return GenerateSprintResponse.builder()
                .sprintId(savedSprint.getId())
                .status(SprintStatus.CREATED.name())
                .itemsCount(items.size())
                .build();
    }

    @Override
    public SprintResponse getSprintById(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new NotFoundException("Sprint not found"));
        return mapSprint(sprint);
    }

    @Override
    public SprintResponse getActiveSprint(Long userId) {
        Sprint sprint = sprintRepository.findFirstByUserIdAndPeriodStartLessThanEqualAndPeriodEndGreaterThanEqualOrderByIdDesc(
                        userId, LocalDate.now(), LocalDate.now())
                .orElseThrow(() -> new NotFoundException("Active sprint not found"));
        return mapSprint(sprint);
    }

    @Override
    public List<SprintResponse> getSprintsByUserId(Long userId) {
        return sprintRepository.findByUserIdOrderByIdDesc(userId)
                .stream()
                .map(this::mapSprint)
                .toList();
    }

    @Override
    @Transactional
    public ReplanSprintResponse replanSprint(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new NotFoundException("Sprint not found"));

        UserPreferencesResponse preferences = userClient.getPreferences(sprint.getUserId());
        Integer dailyStudyHours = preferences.getDailyStudyHours();

        if (dailyStudyHours == null || dailyStudyHours <= 0) {
            throw new BadRequestException("dailyStudyHours must be greater than 0");
        }

        List<PlanningTaskResponse> tasks = sprint.getGoalId() == null
                ? taskClient.getPlanningTasks(sprint.getUserId())
                : taskClient.getPlanningTasksByGoal(sprint.getUserId(), sprint.getGoalId());

        sprintItemRepository.deleteBySprintId(sprintId);
        List<SprintItem> items = buildSprintItems(sprintId, tasks, sprint.getPeriodStart(), sprint.getPeriodEnd(), dailyStudyHours);
        sprintItemRepository.saveAll(items);

        sprint.setStatus(SprintStatus.REPLANNED);
        sprintRepository.save(sprint);

        notificationClient.notifySprintReplanned(
                SprintNotificationRequest.builder()
                        .userId(sprint.getUserId())
                        .sprintId(sprint.getId())
                        .message("Учебный спринт был обновлен")
                        .build()
        );

        return ReplanSprintResponse.builder()
                .sprintId(sprint.getId())
                .status(SprintStatus.REPLANNED.name())
                .itemsCount(items.size())
                .build();
    }

    @Override
    public List<SprintItemResponse> getSprintItems(Long sprintId) {
        sprintRepository.findById(sprintId)
                .orElseThrow(() -> new NotFoundException("Sprint not found"));

        return sprintItemRepository.findBySprintIdOrderByPlannedDateAscOrderIndexAsc(sprintId)
                .stream()
                .map(this::mapSprintItem)
                .toList();
    }

    @Override
    public TodaySprintResponse getTodayPlan(Long userId) {
        Sprint sprint = sprintRepository.findFirstByUserIdAndPeriodStartLessThanEqualAndPeriodEndGreaterThanEqualOrderByIdDesc(
                        userId, LocalDate.now(), LocalDate.now())
                .orElseThrow(() -> new NotFoundException("Active sprint not found"));

        List<TodaySprintItemResponse> items = sprintItemRepository
                .findBySprintIdInAndPlannedDateOrderByPlannedDateAscOrderIndexAsc(List.of(sprint.getId()), LocalDate.now())
                .stream()
                .map(item -> TodaySprintItemResponse.builder()
                        .taskId(item.getTaskId())
                        .taskTitle(item.getTaskTitle())
                        .plannedHours(item.getPlannedHours())
                        .build())
                .toList();

        return TodaySprintResponse.builder()
                .date(LocalDate.now())
                .items(items)
                .build();
    }

    @Override
    public HealthResponse getHealth() {
        return HealthResponse.builder()
                .status("OK")
                .build();
    }

    private List<SprintItem> buildSprintItems(Long sprintId,
                                              List<PlanningTaskResponse> tasks,
                                              LocalDate periodStart,
                                              LocalDate periodEnd,
                                              Integer dailyStudyHours) {
        List<PlanningTaskResponse> sortedTasks = tasks.stream()
                .sorted(Comparator
                        .comparing(PlanningTaskResponse::getDeadline)
                        .thenComparing((a, b) -> Integer.compare(priorityWeight(b.getPriority()), priorityWeight(a.getPriority())))
                        .thenComparing(PlanningTaskResponse::getId))
                .toList();

        List<SprintItem> result = new ArrayList<>();
        LocalDate currentDate = periodStart;
        int remainingHoursInDay = dailyStudyHours;
        int orderIndex = 1;

        for (PlanningTaskResponse task : sortedTasks) {
            int remainingTaskHours = task.getEstimatedHours();

            while (remainingTaskHours > 0 && !currentDate.isAfter(periodEnd)) {
                if (remainingHoursInDay == 0) {
                    currentDate = currentDate.plusDays(1);
                    remainingHoursInDay = dailyStudyHours;
                    continue;
                }

                int plannedHours = Math.min(remainingTaskHours, remainingHoursInDay);

                result.add(SprintItem.builder()
                        .sprintId(sprintId)
                        .taskId(task.getId())
                        .taskTitle(task.getTitle())
                        .plannedDate(currentDate)
                        .plannedHours(plannedHours)
                        .orderIndex(orderIndex++)
                        .status(SprintItemStatus.PLANNED)
                        .build());

                remainingTaskHours -= plannedHours;
                remainingHoursInDay -= plannedHours;
            }

            if (currentDate.isAfter(periodEnd)) {
                break;
            }
        }

        return result;
    }

    private int priorityWeight(String priority) {
        if ("HIGH".equals(priority)) {
            return 3;
        }
        if ("MEDIUM".equals(priority)) {
            return 2;
        }
        return 1;
    }

    private void validatePeriod(LocalDate periodStart, LocalDate periodEnd) {
        if (periodStart.isAfter(periodEnd)) {
            throw new BadRequestException("periodStart must be before or equal to periodEnd");
        }
    }

    private SprintResponse mapSprint(Sprint sprint) {
        return SprintResponse.builder()
                .id(sprint.getId())
                .userId(sprint.getUserId())
                .goalId(sprint.getGoalId())
                .periodStart(sprint.getPeriodStart())
                .periodEnd(sprint.getPeriodEnd())
                .status(sprint.getStatus().name())
                .build();
    }

    private SprintItemResponse mapSprintItem(SprintItem item) {
        return SprintItemResponse.builder()
                .id(item.getId())
                .sprintId(item.getSprintId())
                .taskId(item.getTaskId())
                .taskTitle(item.getTaskTitle())
                .plannedDate(item.getPlannedDate())
                .plannedHours(item.getPlannedHours())
                .status(item.getStatus().name())
                .build();
    }
}