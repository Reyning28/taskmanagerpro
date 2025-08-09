package com.example.taskmanagerpro.model;

import java.io.Serializable;

public class Task implements Serializable {
    private long id;
    private String title;
    private String description;
    private long dueDateMillis; // 0 = none
    private int priority; // 0 low, 1 medium, 2 high
    private boolean done;

    public Task() {}

    public Task(long id, String title, String description, long dueDateMillis, int priority, boolean done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDateMillis = dueDateMillis;
        this.priority = priority;
        this.done = done;
    }

    // getters y setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getDueDateMillis() { return dueDateMillis; }
    public void setDueDateMillis(long dueDateMillis) { this.dueDateMillis = dueDateMillis; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
}