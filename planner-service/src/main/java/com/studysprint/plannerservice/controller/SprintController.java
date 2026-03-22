package com.studysprint.plannerservice.controller;

import com.studysprint.plannerservice.dto.*;
import com.studysprint.plannerservice.service.PlannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SprintController {

    private final PlannerService plannerService;

    @PostMapping("/api/sprints/generate")
    public GenerateSprintResponse generateSprint(@Valid @RequestBody GenerateSprintRequest request) {
        return plannerService.generateSprint(request);
    }

    @GetMapping("/api/sprints/{sprintId}")
    public SprintResponse getSprintById(@PathVariable Long sprintId) {
        return plannerService.getSprintById(sprintId);
    }

    @GetMapping("/api/sprints/user/{userId}/active")
    public SprintResponse getActiveSprint(@PathVariable Long userId) {
        return plannerService.getActiveSprint(userId);
    }

    @GetMapping("/api/sprints/user/{userId}")
    public List<SprintResponse> getSprintsByUserId(@PathVariable Long userId) {
        return plannerService.getSprintsByUserId(userId);
    }

    @PostMapping("/api/sprints/{sprintId}/replan")
    public ReplanSprintResponse replanSprint(@PathVariable Long sprintId) {
        return plannerService.replanSprint(sprintId);
    }

    @GetMapping("/api/sprints/{sprintId}/items")
    public List<SprintItemResponse> getSprintItems(@PathVariable Long sprintId) {
        return plannerService.getSprintItems(sprintId);
    }

    @GetMapping("/api/sprints/user/{userId}/today")
    public TodaySprintResponse getTodayPlan(@PathVariable Long userId) {
        return plannerService.getTodayPlan(userId);
    }

    @GetMapping("/api/internal/planner/health")
    public HealthResponse getHealth() {
        return plannerService.getHealth();
    }
}