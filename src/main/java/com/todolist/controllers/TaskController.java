package com.todolist.controllers;

import com.todolist.entities.Tasks;
import com.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("task")
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks request) {
        Tasks tasks = taskService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(tasks);
    }
}
