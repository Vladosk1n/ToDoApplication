package todoapp.com.todoapplication.model;

import todoapp.com.todoapplication.model.enums.TaskState;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Task implements Serializable {

//    private static final long serialVersionUID = -7817224776021728682L;

    private Long taskId;
    private Long userId;
    private String description;
    private LocalDate deadline;
    private TaskState taskState;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId) && Objects.equals(userId, task.userId) && Objects.equals(description, task.description) && Objects.equals(deadline, task.deadline) && taskState == task.taskState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, userId, description, deadline, taskState);
    }
}
