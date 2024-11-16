import java.io.*;
import java.net.*;
import javax.swing.SwingUtilities;

// 퀴즈 게임의 클라이언트 측 구현을 담당하는 클래스
public class QuizClient {
    // 서버 연결 정보
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;
    
    // 네트워크 통신을 위한 필드
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private QuizGameGUI gui;

    public QuizClient() {
        gui = new QuizGameGUI(this);
    }

    // 클라이언트 시작 메소드: 서버와 연결하고 메시지 수신 스레드를 시작
    public void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // 서버로부터 메시지를 받는 스레드 시작
            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            System.err.println("서버 연결 실패: " + e.getMessage());
        }
    }

    // 서버로부터 메시지를 지속적으로 수신하는 메소드
    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                processMessage(message);
            }
        } catch (IOException e) {
            System.err.println("서버로부터 메시지 수신 실패: " + e.getMessage());
        }
    }

    // 서버로부터 받은 메시지를 처리하는 메소드
    private void processMessage(String message) {
        // 문제 수신 처리
        if (message.startsWith("QUESTION:")) {
            String question = message.substring(9);
            SwingUtilities.invokeLater(() -> gui.setQuestion(question));
        } 
        // 정답/오답 처리
        else if (message.startsWith("CORRECT:") || message.startsWith("INCORRECT:")) {
            boolean isCorrect = message.startsWith("CORRECT:");
            String correctAnswer = message.substring(message.indexOf(":") + 1);
            SwingUtilities.invokeLater(() -> gui.showAnswer(isCorrect, correctAnswer));
        } 
        // 최종 점수 처리
        else if (message.startsWith("FINAL_SCORE:")) {
            String score = message.substring(12);
            SwingUtilities.invokeLater(() -> gui.showResultScreen(score));
        }
    }

    // 게임 시작 요청을 서버로 전송
    public void startGame() {
        out.println("START");
        gui.showQuizScreen();
    }

    // 사용자의 답변을 서버로 전송
    public void sendAnswer(String answer) {
        out.println("ANSWER:" + answer);
    }

    // 다음 문제 요청을 서버로 전송
    public void requestNextQuestion() {
        out.println("NEXT");
    }

    // 게임 재시작 처리
    public void restartGame() {
        startGame();
    }
} 