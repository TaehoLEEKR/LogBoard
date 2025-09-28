# LogBoard - 중앙 로그 수집 시스템 🚀 (프로토 타입 버전)

gRPC 기반의 분산 시스템 로그 중앙 집중 관리 솔루션

## 📋 개요

LogBoard는 여러 서버(A, B, C)의 로그를 중앙에서 수집하고 실시간으로 모니터링할 수 있는 시스템입니다.

프로토 타입 버전으로 오픈소스가 아닌 로컬에서 테스트 하는 버전이다.

현재 로컬에서 HTTP gRpc 통신으로 로그를 수집하고 중앙A서버에서 9090포트로 확인 할 수 있는 구조.

### 🏗️ 시스템 구조

[ServerB:8081] -> [ServerA:9090] <- [ServerC:8082]

bash
# 터미널 1: A서버 (중앙 서버) 실행
cd LogBoard_gRpc ./gradlew bootRun
# 터미널 2: B서버 실행
cd ServerB ./gradlew bootRun
# 터미널 3: C서버 실행
cd ServerC ./gradlew bootRun

---
[툴 사용시 ]

중앙서버 logboard_grpc 실행 (9090) <br>
serverB 실행 (8081) <br>
serverC 실행 (8082)


### 2. 웹 UI 접속

브라우저에서 [http://localhost:9090](http://localhost:9090) 접속

---
## TODO 
1. 테스트 용 Http 스케쥴러 삭제 
   2. 특정 B,C 서버 키워드로그 수집 HTTP 통신 고려 (gRpc 포트)
3. 중앙 서버 특정 호스트 하드코딩 삭제 
   4. 동적 호스팅 및 로그 수집.
5. 오픈소스 배포 후 전체코드 수정
