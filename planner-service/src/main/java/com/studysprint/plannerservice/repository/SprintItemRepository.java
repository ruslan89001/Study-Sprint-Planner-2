package com.studysprint.plannerservice.repository;

import com.studysprint.plannerservice.entity.SprintItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SprintItemRepository extends JpaRepository<SprintItem, Long> {

    List<SprintItem> findBySprintIdOrderByPlannedDateAscOrderIndexAsc(Long sprintId);

    List<SprintItem> findBySprintIdInAndPlannedDateOrderByPlannedDateAscOrderIndexAsc(List<Long> sprintIds, LocalDate plannedDate);

    void deleteBySprintId(Long sprintId);
}