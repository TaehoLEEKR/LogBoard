package com.open.logboard_grpc.client;

import com.open.logboard_grpc.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * LogBoard 중앙 서버로 로그를 전송하는 클라이언트
 * B, C 서버에서 사용할 수 있도록 설계
 */
@Component
@ConditionalOnProperty(name = "logboard.client.enabled", havingValue = "true")
public class LogBoardClient {

    private ManagedChannel channel;
    private LogCollectorServiceGrpc.LogCollectorServiceBlockingStub blockingStub;

    private String centralHost;
    private int centralPort;
    private String serverName;

    @PostConstruct
    public void init() {
        // 설정값들을 환경변수나 프로퍼티에서 가져오기
        this.centralHost = System.getProperty("logboard.central.host", "localhost");
        this.centralPort = Integer.parseInt(System.getProperty("logboard.central.port", "9091"));
        this.serverName = System.getProperty("logboard.server.name", "Unknown");

        // gRPC 채널 생성
        this.channel = ManagedChannelBuilder.forAddress(centralHost, centralPort)
                .usePlaintext()
                .build();

        this.blockingStub = LogCollectorServiceGrpc.newBlockingStub(channel);

        System.out.println("LogBoard 클라이언트 초기화: " + serverName + " -> " + centralHost + ":" + centralPort);
    }

    /**
     * 로그 전송 메서드
     */
    public void sendLog(String level, String message, String logger, String thread) {
        try {
            LogMessage logMessage = LogMessage.newBuilder()
                    .setServerName(serverName)
                    .setLevel(level)
                    .setMessage(message)
                    .setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .setLogger(logger)
                    .setThread(thread)
                    .build();

            LogResponse response = blockingStub.collectLog(logMessage);

            if (!response.getSuccess()) {
                System.err.println("로그 전송 실패: " + response.getMessage());
            }

        } catch (Exception e) {
            System.err.println("로그 전송 중 오류: " + e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        if (channel != null) {
            try {
                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}