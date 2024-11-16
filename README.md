# 🎮 네트워크 퀴즈 게임

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Socket](https://img.shields.io/badge/Socket-010101?style=for-the-badge&logo=socket.io&logoColor=white)

멀티스레드 소켓 프로그래밍을 활용한 실시간 퀴즈 게임 애플리케이션입니다.

## ✨ 주요 기능

- 실시간 클라이언트-서버 통신
- 멀티클라이언트 지원 (ThreadPool 활용)
- 설정 파일 기반의 서버 연결
- 실시간 점수 계산 및 피드백

## 🏗️ 아키텍처

├── 서버 (QuizServer)
│ ├── 문제 관리
│ ├── 클라이언트 연결 관리
│ └── 멀티스레드 처리
│
└── 클라이언트 (QuizClient)
├── 서버 연결
├── 사용자 입력 처리
└── 설정 파일 관리


## 🚀 시작하기

### 서버 실행
```bash
java App server
```

### 클라이언트 실행
```bash
java App
```


## 📝 설정

서버 연결 정보는 `server_info.dat` 파일에서 관리됩니다:
`localhost:1234`


## 🔍 프로토콜

### 메시지 형식
- `QUESTION:{질문내용}`
- `CORRECT:{피드백메시지}`
- `INCORRECT:{피드백메시지}`
- `FINAL_SCORE:{점수}/{총문제수}`

## 🛠️ 기술 스택

- Java Socket Programming
- Java Thread Pool
- File I/O
- Protocol Design

## 🎯 구현 세부사항

- **동시성 처리**: ExecutorService를 활용한 ThreadPool 구현
- **설정 관리**: 파일 기반 서버 설정 관리
- **프로토콜**: ASCII 기반 커스텀 프로토콜 구현
- **에러 처리**: 네트워크 및 파일 I/O 예외 처리

## 📊 성능

- 동시 접속 가능 클라이언트: 10명
- 기본 포트: 1234

## 📜 라이선스

MIT License