import java.io.*;
import java.net.*;
import javax.swing.SwingUtilities;

public class QuizClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private QuizGameGUI gui;

    public QuizClient() {
        gui = new QuizGameGUI(this);
    }

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

    private void processMessage(String message) {
        if (message.startsWith("QUESTION:")) {
            String question = message.substring(9);
            SwingUtilities.invokeLater(() -> gui.setQuestion(question));
        } else if (message.startsWith("CORRECT:") || message.startsWith("INCORRECT:")) {
            boolean isCorrect = message.startsWith("CORRECT:");
            String correctAnswer = message.substring(message.indexOf(":") + 1);
            SwingUtilities.invokeLater(() -> gui.showAnswer(isCorrect, correctAnswer));
        } else if (message.startsWith("FINAL_SCORE:")) {
            String score = message.substring(12);
            SwingUtilities.invokeLater(() -> gui.showResultScreen(score));
        }
    }

    public void startGame() {
        out.println("START");
        gui.showQuizScreen();
    }

    public void sendAnswer(String answer) {
        out.println("ANSWER:" + answer);
    }

    public void requestNextQuestion() {
        out.println("NEXT");
    }

    public void restartGame() {
        startGame();
    }
} 