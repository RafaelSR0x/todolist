package com.todolist.dtos;

import com.todolist.entities.Tasks;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "completed",
        "createdAt",
        "updatedAt"
})

public class TasksDTO {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TasksDTO() {
    }

    public TasksDTO(Tasks tasks) {
        id = tasks.getId();
        title = tasks.getTitle();
        description = tasks.getDescription();
        completed = tasks.isCompleted();
        createdAt = tasks.getCreatedAt();
        updatedAt = tasks.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return updatedAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
