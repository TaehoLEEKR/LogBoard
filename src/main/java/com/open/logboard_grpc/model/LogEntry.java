package com.open.logboard_grpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry {
    private String id;
    private String serverName;
    private String level;
    private String message;
    private LocalDateTime timestamp;
    private String logger;
    private String thread;

    @Builder.Default
    private LocalDateTime receivedAt = LocalDateTime.now();
}