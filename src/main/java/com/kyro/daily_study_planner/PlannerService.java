package com.kyro.daily_study_planner;

import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;

@Service
public class PlannerService {

    private static final int BREAK_MINUTES = 10;

    public void scheduleTasks(List<StudyTask> tasks, LocalTime dayStart) {
        LocalTime current = dayStart;

        for (StudyTask task : tasks) {
            task.setStartTime(current);
            current = current.plusMinutes(task.getDurationMinutes() + BREAK_MINUTES);
        }
    }
}
