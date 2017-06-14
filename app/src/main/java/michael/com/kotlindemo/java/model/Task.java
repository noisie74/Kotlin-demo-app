package michael.com.kotlindemo.java.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Mikhail on 5/31/17.
 */

public class Task {

    @SerializedName("_id")
    private String id;
    @SerializedName("text")
    private String taskTitle;
    @SerializedName("completed")
    private Boolean isCompleted;

    public Task(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public Task(String taskTitle, Boolean completed) {
        this.taskTitle = taskTitle;
        this.isCompleted = completed;
    }

    public Task(String id, String taskTitle, Boolean isCompleted) {
        this.id = id;
        this.taskTitle = taskTitle;
        this.isCompleted = isCompleted;
    }

    public String getId() {
        return id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public Boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isEmpty() {
        return (taskTitle == null || "".equals(taskTitle));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        return taskTitle != null ? taskTitle.equals(task.taskTitle) : task.taskTitle == null && (isCompleted != null ?
                isCompleted.equals(task.isCompleted) :
                task.isCompleted == null);

    }

    @Override
    public int hashCode() {
        int result = taskTitle != null ? taskTitle.hashCode() : 0;
        result = 31 * result + (isCompleted != null ? isCompleted.hashCode() : 0);
        return result;
    }
}
