package com.sk.syncboard.controller;

import com.sk.syncboard.dto.TaskRequest;
import com.sk.syncboard.dto.TaskResponse;
import com.sk.syncboard.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor // This injects your Service automatically
public class TaskController {

    private final TaskService taskService;

    // 1. Get all tasks for the logged-in user's organization
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasksByOrganization();
        return ResponseEntity.ok(tasks);
    }

    // 2. Create a new task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        TaskResponse createdTask = taskService.createTask(request);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // 3. Update task status or details
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest request) throws AccessDeniedException {
        TaskResponse updated = taskService.updateTask(id, request);
        return ResponseEntity.ok(updated);
    }

    // 4. Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build(); // Returns a 204 No Content status
    }
}