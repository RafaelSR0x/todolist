package com.todolist.dtos;

import com.todolist.entities.Tasks;

public class TasksDTO {

    private String title;
    private String description;

    public TasksDTO() {
    }

    public TasksDTO(Tasks tasks) {
        title = tasks.getTitle();
        description = tasks.getDescription();
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
}
