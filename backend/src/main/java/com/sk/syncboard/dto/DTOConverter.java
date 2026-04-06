package com.sk.syncboard.dto;


import com.sk.syncboard.model.Task;
import com.sk.syncboard.model.User;
import org.springframework.stereotype.Component;

@Component
public class DTOConverter {
    public TaskResponse taskToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assignedToName(
                        task.getAssignedTo() != null
                                ? task.getAssignedTo().getFirst_name()
                                : null
                )
                .createdByName(
                        task.getCreatedBy() != null
                                ? task.getCreatedBy().getFirst_name()
                                : null
                )
                .createdAt(task.getCreatedAt())
                .build();
    }

    public Task requestToTask(TaskRequest taskRequest, User user) {
        return Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus())
                .priority(taskRequest.getPriority())
                .assignedTo(user)
                .build();
    }
}