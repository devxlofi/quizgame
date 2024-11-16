import javax.swing.*;
import java.io.*;
import java.net.*;

public class QuizClient {
    private String serverIP;
    private int serverPort;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private QuizGameGUI gui;

    public QuizClient() {
        loadConfig();
        gui = new QuizGameGUI(this);
    }

    public void startGame() {
        start();
    }

    public void restartGame() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        start();
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
        try {
            socket = new Socket(serverIP, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            gui.showQuizScreen();

            // 서버로부터 메시지를 받는 스레드
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        String[] parts = serverMessage.split(":", 2);
                        String messageType = parts[0];
                        String content = parts[1];

                        SwingUtilities.invokeLater(() -> {
                            switch (messageType) {
                                case "QUESTION":
                                    gui.setQuestion(content);
                                    break;
                                case "CORRECT":
                                    gui.showAnswer(true);
                                    break;
                                case "INCORRECT":
                                    gui.showAnswer(false);
                                    break;
                                case "FINAL_SCORE":
                                    gui.showResultScreen(content);
                                    break;
                            }
                        });
                    }
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> 
                        JOptionPane.showMessageDialog(null, 
                            "서버와의 연결이 끊어졌습니다.",
                            "연결 오류",
                            JOptionPane.ERROR_MESSAGE));
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "서버 연결 실패: " + e.getMessage(), 
                "연결 오류", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendAnswer(String answer) {
        if (out != null) {
            out.println("ANSWER:" + answer);
        }
    }
} 