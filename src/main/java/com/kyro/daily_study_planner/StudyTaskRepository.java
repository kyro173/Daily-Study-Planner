package com.kyro.daily_study_planner;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {

    List<StudyTask> findByPlannedDateOrderByStartTimeAsc(LocalDate plannedDate);
}
