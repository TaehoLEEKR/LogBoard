package com.open.logboard_grpc.service;

import com.open.logboard_grpc.model.LogEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class LogStorageService {

    private final List<LogEntry> logs = new CopyOnWriteArrayList<>();
    private static final int MAX_LOGS = 1000; // 프로토타입용으로 적게

    public void addLog(LogEntry logEntry) {
        logs.add(0, logEntry); // 최신이 맨 앞에

        if (logs.size() > MAX_LOGS) {
            logs.remove(logs.size() - 1);
        }
    }

    public List<LogEntry> getAllLogs() {
        return new ArrayList<>(logs);
    }

    public List<LogEntry> getLogsByServer(String serverName) {
        return logs.stream()
                .filter(log -> serverName.equals(log.getServerName()))
                .collect(Collectors.toList());
    }

    public void clearLogs() {
        logs.clear();
    }
}