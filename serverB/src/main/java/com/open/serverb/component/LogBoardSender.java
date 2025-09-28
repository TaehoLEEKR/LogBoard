package com.open.serverb.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

/**
 * HTTP로 A서버에 로그 전송 (proto 파일 필요 없음)
 */
@Component
public class LogBoardSender {

    @Value("${logboard.central.url:http://localhost:9090}")
    private String centralUrl;

    @Value("${logboard.server.name:ServerB}")
    private String serverName;

    private final WebClient webClient = WebClient.create();
    private final Random random = new Random();

    private final String[] levels = {"INFO", "WARN", "ERROR", "DEBUG"};
    private final String[] messages = {
            "서버B에서 사용자 로그인 처리",
            "서버B에서 데이터 처리 완료",
            "서버B에서 오류 발생",
            "서버B에서 배치 작업 시작"
    };

    /**
     * 5초마다 테스트 로그 전송
     */
    @Scheduled(fixedRate = 5000)
    public void sendTestLog() {
        String level = levels[random.nextInt(levels.length)];
        String message = messages[random.nextInt(messages.length)];

        Map<String, String> logData = Map.of(
                "serverName", serverName,
                "level", level,
                "message", message,
                "timestamp", LocalDateTime.now().toString(),
                "logger", "com.example.serverb.TestLogger",
                "thread", "main"
        );

        webClient.post()
                .uri(centralUrl + "/api/logs/simple")  // A서버에 간단한 엔드포인트 추가
                .bodyValue(logData)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        result -> System.out.println("로그 전송 성공: " + message),
                        error -> System.err.println("로그 전송 실패: " + error.getMessage())
                );
    }
}