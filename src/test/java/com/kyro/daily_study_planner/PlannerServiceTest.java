package com.kyro.daily_study_planner;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlannerServiceTest {

    private final PlannerService plannerService = new PlannerService();

    @Test
    void schedulesTasksWithTenMinuteBreaks() {
        StudyTask first = task(45);
        StudyTask second = task(30);

        plannerService.scheduleTasks(List.of(first, second), LocalTime.of(6, 0));

        assertEquals(LocalTime.of(6, 0), first.getStartTime());
        assertEquals(LocalTime.of(6, 55), second.getStartTime());
    }

    @Test
    void schedulesEmptyDayWithoutFailure() {
        plannerService.scheduleTasks(List.of(), LocalTime.of(6, 0));
    }

    private StudyTask task(int durationMinutes) {
        StudyTask task = new StudyTask();
        task.setDurationMinutes(durationMinutes);
        return task;
    }
}
