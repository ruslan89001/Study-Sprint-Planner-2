package com.studysprint.plannerservice.service;

import com.studysprint.plannerservice.dto.*;

import java.util.List;

public interface PlannerService {

    GenerateSprintResponse generateSprint(GenerateSprintRequest request);

    SprintResponse getSprintById(Long sprintId);

    SprintResponse getActiveSprint(Long userId);

    List<SprintResponse> getSprintsByUserId(Long userId);

    ReplanSprintResponse replanSprint(Long sprintId);

    List<SprintItemResponse> getSprintItems(Long sprintId);

    TodaySprintResponse getTodayPlan(Long userId);

    HealthResponse getHealth();
}