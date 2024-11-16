import java.io.*;
import java.net.*;

public class QuizClient {
    private String serverIP;
    private int serverPort;

    public QuizClient() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            File configFile = new File("server_info.dat");
            if (configFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                serverIP = reader.readLine();
                serverPort = Integer.parseInt(reader.readLine());
                reader.close();
            } else {
                serverIP = "localhost";
                serverPort = 1234;
            }
        } catch (IOException e) {
            System.err.println("설정 파일 로드 실패. 기본값 사용: localhost:1234");
            serverIP = "localhost";
            serverPort = 1234;
        }
    }

    public void start() {
        try (Socket socket = new Socket(serverIP, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                String[] parts = serverMessage.split(":", 2);
                String messageType = parts[0];
                String content = parts[1];

                switch (messageType) {
                    case "QUESTION":
                        System.out.println("질문: " + content);
                        System.out.print("답변: ");
                        String answer = userInput.readLine();
                        out.println(answer);
                        break;
                    case "CORRECT":
                    case "INCORRECT":
                        System.out.println(content);
                        break;
                    case "FINAL_SCORE":
                        System.out.println("최종 점수: " + content);
                        return;
                }
            }

        } catch (IOException e) {
            System.err.println("클라이언트 에러: " + e.getMessage());
        }
    }
} 