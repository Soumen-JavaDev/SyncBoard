package com.sk.syncboard.dto;

import com.sk.syncboard.model.Priority;
import com.sk.syncboard.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private String title;
    private String description;
    private Status status;     // e.g., TODO, IN_PROGRESS
    private Priority priority; // e.g., LOW, MEDIUM, HIGH
    private Long assignedToId; // ID of the user assigned to this task
}