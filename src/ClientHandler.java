import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.net.Socket;
import java.io.IOException;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<Question> questions;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, List<Question> questions) {
        this.clientSocket = socket;
        this.questions = questions;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String command;
            int currentQuestion = 0;
            int score = 0;

            while ((command = in.readLine()) != null) {
                if (command.equals("START")) {
                    currentQuestion = 0;
                    score = 0;
                    // 첫 문제 전송
                    out.println("QUESTION:" + questions.get(currentQuestion).getQuestion());
                } else if (command.startsWith("ANSWER:")) {
                    String answer = command.substring(7).trim();
                    Question question = questions.get(currentQuestion);
                    
                    if (answer.equalsIgnoreCase(question.getAnswer())) {
                        score++;
                        out.println("CORRECT:" + question.getAnswer());
                    } else {
                        out.println("INCORRECT:" + question.getAnswer());
                    }
                    
                    currentQuestion++;
                    
                    // 모든 문제를 다 풀었는지 확인
                    if (currentQuestion >= questions.size()) {
                        out.println("FINAL_SCORE:" + score + "/" + questions.size());
                    } else {
                        // 잠시 대기 후 다음 문제 전송
                        Thread.sleep(2000);
                        out.println("QUESTION:" + questions.get(currentQuestion).getQuestion());
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("클라이언트 처리 중 에러: " + e.getMessage());
        }
    }
} 