package com.kyro.daily_study_planner;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class StudyTask {

    public enum TaskType {
        READING, PRACTICE, REVISION, MOCK_TEST
    }

    public enum TaskStatus {
        PLANNED, DONE, SKIPPED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Subject is required")
    private String subject;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskType type;

    @NotNull(message = "Planned date is required")
    private LocalDate plannedDate;

    @Positive(message = "Duration must be greater than zero")
    @Max(value = 720, message = "Duration cannot exceed 720 minutes")
    private int durationMinutes;

    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PLANNED;

    public StudyTask() {
    }

    // Add getters and setters for each field below.
    // Ask Copilot: "generate getters and setters for this class"
    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return TaskType return the type
     */
    public TaskType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TaskType type) {
        this.type = type;
    }

    /**
     * @return LocalDate return the plannedDate
     */
    public LocalDate getPlannedDate() {
        return plannedDate;
    }

    /**
     * @param plannedDate the plannedDate to set
     */
    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }

    /**
     * @return int return the durationMinutes
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * @param durationMinutes the durationMinutes to set
     */
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    /**
     * @return LocalTime return the startTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return TaskStatus return the status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

}
