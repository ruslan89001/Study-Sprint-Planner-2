package com.studysprint.plannerservice.repository;

import com.studysprint.plannerservice.entity.Sprint;
import enums.SprintStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    List<Sprint> findByUserIdOrderByIdDesc(Long userId);

    Optional<Sprint> findFirstByUserIdAndPeriodStartLessThanEqualAndPeriodEndGreaterThanEqualOrderByIdDesc(
            Long userId,
            LocalDate date1,
            LocalDate date2
    );

    Optional<Sprint> findFirstByUserIdAndStatusInOrderByIdDesc(Long userId, List<SprintStatus> statuses);
}