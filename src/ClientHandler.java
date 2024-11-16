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

            int score = 0;
            for (Question question : questions) {
                out.println("QUESTION:" + question.getQuestion());
                String response = in.readLine();
                
                System.out.println("받은 응답: " + response);
                
                String answer = "";
                if (response != null && response.startsWith("ANSWER:")) {
                    answer = response.substring(7).trim();
                    System.out.println("사용자 답변: " + answer);
                    System.out.println("정답: " + question.getAnswer());
                    
                    if (answer.equalsIgnoreCase(question.getAnswer())) {
                        score++;
                        System.out.println("정답 처리됨!");
                        out.println("CORRECT:정답입니다!");
                    } else {
                        System.out.println("오답 처리됨!");
                        out.println("INCORRECT:틀렸습니다. 정답은 " + question.getAnswer() + "입니다.");
                    }
                } else {
                    System.out.println("잘못된 응답 형식: " + response);
                    out.println("INCORRECT:잘못된 응답 형식입니다.");
                }
            }

            out.println("FINAL_SCORE:" + score + "/" + questions.size());
            
        } catch (IOException e) {
            System.err.println("클라이언트 처리 중 에러: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("소켓 닫기 실패: " + e.getMessage());
            }
        }
    }
} 