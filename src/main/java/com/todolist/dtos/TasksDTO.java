package com.todolist.dtos;

import com.todolist.entities.Tasks;

import java.time.LocalDateTime;

public class TasksDTO {

    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAd;

    public TasksDTO() {
    }

    public TasksDTO(Tasks tasks) {
        title = tasks.getTitle();
        description = tasks.getDescription();
        createdAt = tasks.getCreatedAt();
        updatedAd = tasks.getUpdatedAt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAd() {
        return updatedAd;
    }
}
