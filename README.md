# 🎮 네트워크 퀴즈 게임 (Network Quiz Game)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Socket](https://img.shields.io/badge/Socket-010101?style=for-the-badge&logo=socket.io&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-43B02A?style=for-the-badge&logo=java&logoColor=white)

Java Swing과 소켓 프로그래밍을 활용한 실시간 멀티플레이어 퀴즈 게임입니다.

## ✨ 주요 기능

- 직관적인 GUI 인터페이스 (Java Swing)
- 실시간 클라이언트-서버 통신
- 멀티클라이언트 지원 (ExecutorService ThreadPool)
- 즉각적인 정답 피드백 시스템
- 게임 진행 상황 실시간 표시
- 최종 점수 집계 및 리플레이 기능

## 🏗️ 시스템 아키텍처

```
src/
├── App.java              # 메인 애플리케이션 진입점
├── QuizServer.java       # 서버 로직 및 문제 관리
├── ClientHandler.java    # 클라이언트 세션 관리
├── QuizClient.java       # 클라이언트 네트워크 로직
├── QuizGameGUI.java      # 사용자 인터페이스
└── Question.java         # 문제 데이터 모델
```

## 🎯 기능 상세

### 서버 (QuizServer)
- 동시 접속 클라이언트: 최대 10명
- 기본 포트: 1234
- 문제 풀 관리 및 배포
- 클라이언트 응답 처리 및 채점

### 클라이언트 (QuizClient + QuizGameGUI)
- 스플래시 화면
- 실시간 퀴즈 인터페이스
- 즉각적인 정답 피드백
- 최종 결과 화면
- 게임 재시작 기능

## 🚀 실행 방법

### 컴파일
```bash
javac -d bin src/*.java
```

### 서버 실행
```bash
java -cp src App server
```

### 클라이언트 실행
```bash
java -cp src App
```

## 🔍 통신 프로토콜

### 클라이언트 → 서버
- `START`: 게임 시작 요청
- `ANSWER:{답변}`: 사용자 답변 제출

### 서버 → 클라이언트
- `QUESTION:{문제}`: 새로운 문제 전송
- `CORRECT:{정답}`: 정답 피드백
- `INCORRECT:{정답}`: 오답 피드백
- `FINAL_SCORE:{점수}/{총문제수}`: 최종 결과

## 🛠️ 기술 스택

- Java 8+
- Java Swing (GUI)
- Socket Programming
- ExecutorService (Thread Pool)
- Event-Driven Architecture

## 🎨 UI 구성

- **스플래시 화면**: 게임 시작 인터페이스
- **퀴즈 화면**: 문제 표시 및 답변 입력
- **결과 화면**: 최종 점수 및 재시작 옵션

## 📝 라이선스

MIT License