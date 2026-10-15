package com.todolist.controllers;

import com.todolist.dtos.TasksDTO;
import com.todolist.entities.Tasks;
import com.todolist.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping()
    public ResponseEntity<Tasks> createTask(@Valid @RequestBody Tasks request) {
        Tasks tasks = taskService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(tasks);
    }

    @GetMapping()
    public ResponseEntity<List<TasksDTO>> findAll() {
        List<TasksDTO> list = taskService.findAllTask();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TasksDTO> findById(@PathVariable Long taskId) {
        TasksDTO task = taskService.findById(taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Tasks> updateTask(@Valid @RequestBody Tasks request, @PathVariable Long taskId){
        Tasks tasks = taskService.updateTask(request, taskId);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/{taskId}/completed")
    public ResponseEntity<TasksDTO> markAsCompleted(@PathVariable Long taskId) {
        Tasks tasks = taskService.markAsCompleted(taskId);
        TasksDTO dto = new TasksDTO(tasks);

        return ResponseEntity.ok(dto);
    }
}
