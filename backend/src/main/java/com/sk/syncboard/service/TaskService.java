package com.sk.syncboard.service;

import com.sk.syncboard.dto.DTOConverter;
import com.sk.syncboard.dto.TaskRequest;
import com.sk.syncboard.dto.TaskResponse;
import com.sk.syncboard.model.Task;
import com.sk.syncboard.model.User;
import com.sk.syncboard.repository.TaskRepository;
import com.sk.syncboard.repository.UserRepository;
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

    // Get all tasks filter by organization
    public List<TaskResponse> getAllTasksByOrganization() {
        return null;
    }

    //  Create task
    public TaskResponse createTask(TaskRequest request) {

        User user = userRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = dtoConverter.requestToTask(request, user);

        Task savedTask = taskRepository.save(task);

        return dtoConverter.taskToResponse(savedTask);
    }

    //  Update task
    public TaskResponse updateTask(Long id, TaskRequest request) {

     return null;
    }

    //  Delete task
    public void deleteTask(Long id) {

    }
}