package com.sk.syncboard.service;

import com.sk.syncboard.dto.DTOConverter;
import com.sk.syncboard.dto.TaskRequest;
import com.sk.syncboard.dto.TaskResponse;
import com.sk.syncboard.model.Task;
import com.sk.syncboard.model.User;
import com.sk.syncboard.repository.TaskRepository;
import com.sk.syncboard.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final DTOConverter dtoConverter;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       DTOConverter dtoConverter) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.dtoConverter = dtoConverter;
    }

    // Helper to find the user currently logged in via JWT
    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));
    }

    // 1. Get all tasks filtered by the user's organization
    public List<TaskResponse> getAllTasksByOrganization() {
        User user = getLoggedInUser();
        Long orgId = user.getOrganization().getId();

        return taskRepository.findAllByOrganizationId(orgId)
                .stream()
                .map(dtoConverter::taskToResponse)
                .collect(Collectors.toList());
    }

    // 2. Create task and link it to the organization
    public TaskResponse createTask(TaskRequest request) {
        User currentUser = getLoggedInUser();

        // Find who the task is assigned to
        User assignee = userRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assignee user not found"));

        Task task = dtoConverter.requestToTask(request, assignee);

        // CRITICAL: Set the organization from the logged-in user
        task.setOrganization(currentUser.getOrganization());

        Task savedTask = taskRepository.save(task);
        return dtoConverter.taskToResponse(savedTask);
    }

    // 3. Update task
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        // Update fields
        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setStatus(request.getStatus());
        existingTask.setPriority(request.getPriority());

        if (request.getAssignedToId() != null) {
            User newAssignee = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingTask.setAssignedTo(newAssignee);
        }

        Task updatedTask = taskRepository.save(existingTask);
        return dtoConverter.taskToResponse(updatedTask);
    }

    // 4. Delete task
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}