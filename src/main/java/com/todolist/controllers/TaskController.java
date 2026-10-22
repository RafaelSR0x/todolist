package com.todolist.controllers;

import com.todolist.dtos.PageResponseDTO;
import com.todolist.dtos.TaskRequestDTO;
import com.todolist.dtos.TasksDTO;
import com.todolist.dtos.TaskResponseDTO;
import com.todolist.entities.Task;
import com.todolist.services.TaskService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tarefas", description = "Gerenciamento das tarefas do usuário autenticado")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping()
    @Operation(summary = "Criar tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da tarefa inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO request) {
        TaskResponseDTO tasks = taskService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(tasks);
    }

    @GetMapping()
    @Operation(summary = "Listar tarefas", description = "Lista as tarefas do usuário com filtros e paginação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefas encontradas"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public ResponseEntity<PageResponseDTO<TasksDTO>> findAllPaged(
            @Parameter(description = "Filtra pelo status de conclusão")
            @RequestParam(required = false) Boolean completed,
            @Parameter(description = "Filtra pelo título")
            @RequestParam(required = false) String title,
            @PageableDefault(
                    page = 0,
                    size = 3,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<TasksDTO> tasksPage = taskService.findAllPaged(completed, title, pageable);

        PageResponseDTO<TasksDTO> response = new PageResponseDTO<>(
                tasksPage.getContent(),
                tasksPage.getNumber(),
                tasksPage.getSize(),
                tasksPage.getTotalPages(),
                tasksPage.getTotalElements(),
                tasksPage.isFirst(),
                tasksPage.isLast()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Buscar tarefa por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TasksDTO> findById(@PathVariable Long taskId) {
        TasksDTO task = taskService.findById(taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Atualizar tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da tarefa inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TaskResponseDTO> updateTask(
            @Valid @RequestBody TaskRequestDTO request,
            @PathVariable Long taskId){
        TaskResponseDTO task = taskService.updateTask(request, taskId);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Excluir tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/{taskId}/completed")
    @Operation(summary = "Marcar tarefa como concluída")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa concluída"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "409", description = "Tarefa já concluída")
    })
    public ResponseEntity<TasksDTO> markAsCompleted(@PathVariable Long taskId) {
        Task task = taskService.markAsCompleted(taskId);
        TasksDTO dto = new TasksDTO(task);

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{taskId}/incomplete")
    @Operation(summary = "Reabrir tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa reaberta"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "409", description = "Tarefa já pendente")
    })
    public ResponseEntity<TasksDTO> markAsIncomplete(@PathVariable Long taskId) {
        Task task = taskService.markAsIncomplete(taskId);
        TasksDTO dto = new TasksDTO(task);

        return ResponseEntity.ok(dto);
    }
}
