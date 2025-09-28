package com.open.logboard_grpc.controller;

import com.open.logboard_grpc.model.LogEntry;
import com.open.logboard_grpc.service.LogStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    /**
     * B, C 서버에서 HTTP로 로그 전송할 수 있는 간단한 엔드포인트
     * proto 파일 필요 없음!
     */
    @PostMapping("/logs/simple")
    public String receiveSimpleLog(@RequestBody Map<String, String> logData) {
        try {
            LogEntry logEntry = LogEntry.builder()
                    .id(UUID.randomUUID().toString())
                    .serverName(logData.get("serverName"))
                    .level(logData.get("level"))
                    .message(logData.get("message"))
                    .logger(logData.get("logger"))
                    .thread(logData.get("thread"))
                    .timestamp(LocalDateTime.parse(logData.get("timestamp"), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            logStorageService.addLog(logEntry);
            return "OK";

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}