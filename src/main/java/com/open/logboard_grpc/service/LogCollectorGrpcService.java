package com.open.logboard_grpc.service;

import com.open.logboard_grpc.model.LogEntry;
import com.open.logboard_grpc.proto.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class LogCollectorGrpcService extends LogCollectorServiceGrpc.LogCollectorServiceImplBase {


    private final LogStorageService logStorageService;

    @Override
    public void collectLog(LogMessage request, StreamObserver<LogResponse> responseObserver) {
        try {
            LogEntry logEntry = LogEntry.builder()
                    .id(UUID.randomUUID().toString())
                    .serverName(request.getServerName())
                    .level(request.getLevel())
                    .message(request.getMessage())
                    .logger(request.getLogger())
                    .thread(request.getThread())
                    .timestamp(LocalDateTime.now()) // 간단하게 현재 시간 사용
                    .build();

            logStorageService.addLog(logEntry);

            LogResponse response = LogResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("OK")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            LogResponse response = LogResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(e.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}