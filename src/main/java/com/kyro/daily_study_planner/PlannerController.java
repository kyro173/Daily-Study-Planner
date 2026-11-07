package com.kyro.daily_study_planner;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/planner")
public class PlannerController {

    private static final LocalTime DAY_START = LocalTime.of(6, 0);

    private final StudyTaskRepository repository;
    private final PlannerService plannerService;

    public PlannerController(StudyTaskRepository repository, PlannerService plannerService) {
        this.repository = repository;
        this.plannerService = plannerService;
    }

    @GetMapping
    public String showPlanner(@RequestParam(name = "date", required = false) LocalDate date, Model model) {
        LocalDate day = (date != null) ? date : LocalDate.now().plusDays(1);
        loadPage(model, day);
        StudyTask task = new StudyTask();
        task.setPlannedDate(day);
        model.addAttribute("task", task);
        return "planner";
    }

    @PostMapping("/add")
    public String addTask(@Valid @ModelAttribute("task") StudyTask task, BindingResult result, Model model) {
        if (result.hasErrors()) {
            LocalDate day = (task.getPlannedDate() != null) ? task.getPlannedDate() : LocalDate.now().plusDays(1);
            loadPage(model, day);
            return "planner";
        }

        List<StudyTask> tasks = new ArrayList<>(repository.findByPlannedDateOrderByStartTimeAsc(task.getPlannedDate()));
        tasks.add(task);
        plannerService.scheduleTasks(tasks, DAY_START);
        repository.saveAll(tasks);

        return "redirect:/planner?date=" + task.getPlannedDate();
    }

    @PostMapping("/{id}/status")
    public String updateTaskStatus(@PathVariable Long id, @RequestParam StudyTask.TaskStatus status) {
        StudyTask task = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        task.setStatus(status);
        repository.save(task);
        return "redirect:/planner?date=" + task.getPlannedDate();
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        StudyTask task = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        LocalDate day = task.getPlannedDate();
        repository.delete(task);
        List<StudyTask> remainingTasks = repository.findByPlannedDateOrderByStartTimeAsc(day);
        plannerService.scheduleTasks(remainingTasks, DAY_START);
        repository.saveAll(remainingTasks);
        return "redirect:/planner?date=" + day;
    }

    private void loadPage(Model model, LocalDate day) {
        List<StudyTask> tasks = repository.findByPlannedDateOrderByStartTimeAsc(day);
        long completedCount = tasks.stream()
                .filter(task -> task.getStatus() == StudyTask.TaskStatus.DONE)
                .count();
        int plannedMinutes = tasks.stream()
                .filter(task -> task.getStatus() == StudyTask.TaskStatus.PLANNED)
                .mapToInt(StudyTask::getDurationMinutes)
                .sum();
        int completedMinutes = tasks.stream()
                .filter(task -> task.getStatus() == StudyTask.TaskStatus.DONE)
                .mapToInt(StudyTask::getDurationMinutes)
                .sum();

        model.addAttribute("tasks", tasks);
        model.addAttribute("day", day);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("plannedMinutes", plannedMinutes);
        model.addAttribute("completedMinutes", completedMinutes);
        model.addAttribute("completionPercent", tasks.isEmpty() ? 0 : completedCount * 100 / tasks.size());
        model.addAttribute("taskTypes", StudyTask.TaskType.values());
    }
}
