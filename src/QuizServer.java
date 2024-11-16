import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * 퀴즈 서버 클래스
 * 다중 클라이언트의 동시 접속을 처리하는 TCP 서버
 */
public class QuizServer {
    // 서버의 기본 포트 번호
    private static final int DEFAULT_PORT = 1234;
    // 동시에 처리할 수 있는 최대 클라이언트 수
    private static final int THREAD_POOL_SIZE = 10;
    // 퀴즈 문제 목록을 저장하는 리스트
    private List<Question> questions;
    // 클라이언트 요청을 동시에 처리하기 위한 스레드 풀
    private ExecutorService executorService;

    /**
     * QuizServer 생성자
     * 퀴즈 문제를 초기화하고 스레드 풀을 생성
     */
    public QuizServer() {
        questions = initializeQuestions();
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    /**
     * 퀴즈 문제를 초기화하는 메소드
     * @return 초기화된 문제 리스트
     */
    private List<Question> initializeQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("대한민국의 수도는?", "서울"));
        questions.add(new Question("1 + 1 = ?", "2"));
        questions.add(new Question("지구의 위성은?", "달"));
        // 더 많은 질문 추가 가능
        return questions;
    }

    /**
     * 서버를 시작하고 클라이언트의 연결을 대기하는 메소드
     * 각 클라이언트 연결에 대해 새로운 ClientHandler를 생성하여 처리
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            System.out.println("서버가 포트 " + DEFAULT_PORT + "에서 시작되었습니다.");
            
            while (true) {
                // 클라이언트의 연결을 수락
                Socket clientSocket = serverSocket.accept();
                // 클라이언트 요청을 스레드 풀에서 처리
                executorService.execute(new ClientHandler(clientSocket, questions));
            }
        } catch (IOException e) {
            System.err.println("서버 에러: " + e.getMessage());
        }
    }
} 