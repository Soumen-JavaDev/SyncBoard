package com.sk.syncboard.dto;

import com.sk.syncboard.model.Priority;
import com.sk.syncboard.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String assignedToEmail; // Easier for the frontend than just an ID
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}