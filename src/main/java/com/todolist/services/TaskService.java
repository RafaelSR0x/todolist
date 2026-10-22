package com.todolist.services;

import com.todolist.dtos.TaskRequestDTO;
import com.todolist.dtos.TasksDTO;
import com.todolist.dtos.TaskResponseDTO;
import com.todolist.entities.Tasks;
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
    private Tasks findTaskByIdAndUser(Long taskId, User user) {
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

        Tasks task = new Tasks(
                request.title(),
                request.description(),
                false,
                user
        );

        Tasks tasksSaved = taskRepository.save(task);

        return new TaskResponseDTO(
                tasksSaved.getId(),
                tasksSaved.getTitle(),
                tasksSaved.getDescription(),
                tasksSaved.isCompleted(),
                tasksSaved.getCreatedAt(),
                tasksSaved.getUpdatedAt()
        );
    }

    //Metodo para listar todos as tarefas com paginação
    @Transactional(readOnly = true)
    public Page<TasksDTO> findAllPaged(Boolean completed, String title,Pageable pageable){
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Page<Tasks> tasksPage = taskRepository.findAllFiltered(
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

        Tasks tasks = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        return new TasksDTO(tasks);
    }

    //Metodo para atualizar uma tarefa pelo ID requisitado
    public TaskResponseDTO updateTask(TaskRequestDTO request, Long taskId) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Tasks tasks = findTaskByIdAndUser(taskId, user);

        tasks.setTitle(request.title());
        tasks.setDescription(request.description());

        Tasks updatedTask = taskRepository.save(tasks);

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

        Tasks tasks = findTaskByIdAndUser(taskId, user);

        taskRepository.delete(tasks);
    }

    //metodo para marcar como concluido a tarefa
    public Tasks markAsCompleted(Long taskId) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Tasks tasks = findTaskByIdAndUser(taskId, user);

        if (tasks.isCompleted()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A tarefa já está concluída"
            );
        }

        tasks.setCompleted(true);

        return taskRepository.save(tasks);

    }

    //metodo para reabrir uma tarefa
    public Tasks markAsIncomplete(Long taskId) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Tasks tasks = findTaskByIdAndUser(taskId, user);

        if (!tasks.isCompleted()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A tarefa já está pendente"
            );
        }

        tasks.setCompleted(false);

        return taskRepository.save(tasks);

    }
}
