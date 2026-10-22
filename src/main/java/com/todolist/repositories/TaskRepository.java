package com.todolist.repositories;

import com.todolist.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("""
    SELECT t FROM Task t
    WHERE t.user.id = :userId
    AND (:completed IS NULL OR t.completed = :completed)
    AND (
        :title IS NULL
        OR LOWER(t.title) LIKE
           CONCAT('%', LOWER(CAST(:title AS string)), '%')
    )
""")
    Page<Task> findAllFiltered(
            @Param("completed") Boolean completed,
            @Param("title") String title,
            @Param("userId") String userId,
            Pageable pageable
    );

    Optional<Task> findByIdAndUserId(Long tasksId, String userId);
}
