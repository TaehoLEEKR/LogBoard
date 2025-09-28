
package com.open.logboard_grpc.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 중앙 저장소에 보관할 로그 엔트리 모델
 * gRPC로 받은 LogMessage를 이 형태로 변환해서 메모리에 저장
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // equals/hashCode에서 특정 필드만 포함
public class LogEntry {

    @EqualsAndHashCode.Include
    private String id;                    // 유니크 ID (UUID)

    private String serverName;            // 로그를 보낸 서버명 (A, B, C)
    private String level;                 // 로그 레벨 (INFO, WARN, ERROR, DEBUG)
    private String message;               // 로그 메시지 내용
    private LocalDateTime timestamp;      // 로그 발생 시간
    private String logger;                // 로거 이름
    private String thread;                // 스레드 이름

    @Builder.Default                      // 빌더에서 기본값 설정
    private LocalDateTime receivedAt = LocalDateTime.now();  // 중앙서버에서 받은 시간

    /**
     * 로그의 간단한 문자열 표현
     * @return 서버명, 레벨, 로거, 메시지를 포함한 문자열
     */
    @Override
    public String toString() {
        return String.format("[%s] %s - %s: %s", serverName, level, logger, message);
    }
}