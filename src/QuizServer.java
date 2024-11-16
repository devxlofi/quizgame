import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class QuizServer {
    private static final int DEFAULT_PORT = 1234;
    private static final int THREAD_POOL_SIZE = 10;
    private List<Question> questions;
    private ExecutorService executorService;

    public QuizServer() {
        questions = initializeQuestions();
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    private List<Question> initializeQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("대한민국의 수도는?", "서울"));
        questions.add(new Question("1 + 1 = ?", "2"));
        questions.add(new Question("지구의 위성은?", "달"));
        // 더 많은 질문 추가 가능
        return questions;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            System.out.println("서버가 포트 " + DEFAULT_PORT + "에서 시작되었습니다.");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ClientHandler(clientSocket, questions));
            }
        } catch (IOException e) {
            System.err.println("서버 에러: " + e.getMessage());
        }
    }
} 