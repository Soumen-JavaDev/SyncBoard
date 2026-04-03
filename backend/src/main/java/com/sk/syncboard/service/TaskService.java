package com.sk.syncboard.service;

import com.sk.syncboard.dto.TaskRequest;
import com.sk.syncboard.dto.TaskResponse;
import com.sk.syncboard.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    public List<TaskResponse> getAllTasksByOrganization() {
        return null;
    }

    public TaskResponse createTask(TaskRequest request) {
        return null;
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        return null;
    }

    public void deleteTask(Long id) {
    }
}
