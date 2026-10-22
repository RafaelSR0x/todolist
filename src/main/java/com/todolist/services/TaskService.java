package com.todolist.services;

import com.todolist.dtos.TaskRequestDTO;
import com.todolist.dtos.TasksDTO;
import com.todolist.dtos.TaskResponseDTO;
import com.todolist.entities.Task;
import com.todolist.entities.User;
import com.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //metodo para centralizar a busca de uma tarefa por ID
    private Task findTaskByIdAndUser(Long taskId, User user) {
        return taskRepository.findByIdAndUserId(taskId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    //Metodo para criar nova tarefa
    public TaskResponseDTO create(TaskRequestDTO request) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = new Task(
                request.title(),
                request.description(),
                false,
                user
        );

        Task taskSaved = taskRepository.save(task);

        return new TaskResponseDTO(
                taskSaved.getId(),
                taskSaved.getTitle(),
                taskSaved.getDescription(),
                taskSaved.isCompleted(),
                taskSaved.getCreatedAt(),
                taskSaved.getUpdatedAt()
        );
    }

    //Metodo para listar todos as tarefas com paginação
    @Transactional(readOnly = true)
    public Page<TasksDTO> findAllPaged(Boolean completed, String title,Pageable pageable){
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Page<Task> tasksPage = taskRepository.findAllFiltered(
                completed,
                title,
                user.getId(),
                pageable);

        return tasksPage.map(TasksDTO::new);
    }

    //Metodo para retornar apenas uma tarefa pelo ID requisitado
    public TasksDTO findById(Long taskId) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = findTaskByIdAndUser(taskId, user);

        return new TasksDTO(task);
    }

    //Metodo para atualizar uma tarefa pelo ID requisitado
    public TaskResponseDTO updateTask(TaskRequestDTO request, Long taskId) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = findTaskByIdAndUser(taskId, user);

        task.setTitle(request.title());
        task.setDescription(request.description());

        Task updatedTask = taskRepository.save(task);

        return new TaskResponseDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.isCompleted(),
                updatedTask.getCreatedAt(),
                updatedTask.getUpdatedAt()
        );
    }

    //Metodo para deletar uma tarefa pelo Id requisitado
    public void deleteTask(Long taskId){
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = findTaskByIdAndUser(taskId, user);

        taskRepository.delete(task);
    }

    //metodo para marcar como concluido a tarefa
    public Task markAsCompleted(Long taskId) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = findTaskByIdAndUser(taskId, user);

        if (task.isCompleted()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A tarefa já está concluída"
            );
        }

        task.setCompleted(true);

        return taskRepository.save(task);

    }

    //metodo para reabrir uma tarefa
    public Task markAsIncomplete(Long taskId) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = findTaskByIdAndUser(taskId, user);

        if (!task.isCompleted()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A tarefa já está pendente"
            );
        }

        task.setCompleted(false);

        return taskRepository.save(task);

    }
}
