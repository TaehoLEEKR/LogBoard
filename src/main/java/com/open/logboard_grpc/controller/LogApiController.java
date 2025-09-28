package com.open.logboard_grpc.controller;

import com.open.logboard_grpc.model.LogEntry;
import com.open.logboard_grpc.service.LogStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LogApiController {

    private final LogStorageService logStorageService;

    @GetMapping("/logs")
    public List<LogEntry> getAllLogs() {
        return logStorageService.getAllLogs();
    }

    @GetMapping("/logs/server/{serverName}")
    public List<LogEntry> getLogsByServer(@PathVariable String serverName) {
        return logStorageService.getLogsByServer(serverName);
    }

    @DeleteMapping("/logs")
    public String clearLogs() {
        logStorageService.clearLogs();
        return "cleared";
    }
}