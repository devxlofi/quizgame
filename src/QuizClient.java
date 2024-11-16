import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class QuizClient extends JFrame {
    private String serverIP;
    private int serverPort;
    private JTextArea chatArea;
    private JTextField answerField;
    private JButton submitButton;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    public QuizClient() {
        loadConfig();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("퀴즈 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);

        // 메인 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 채팅 영역
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 입력 패널
        JPanel inputPanel = new JPanel(new BorderLayout());
        answerField = new JTextField();
        answerField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        submitButton = new JButton("제출");
        submitButton.setEnabled(false);

        inputPanel.add(answerField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // 이벤트 리스너
        submitButton.addActionListener(e -> sendAnswer());
        answerField.addActionListener(e -> sendAnswer());

        add(mainPanel);
        setVisible(true);
    }

    private void sendAnswer() {
        if (out != null && answerField.getText().trim().length() > 0) {
            out.println(answerField.getText().trim());
            answerField.setText("");
            submitButton.setEnabled(false);
            answerField.setEnabled(false);
        }
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
                                    chatArea.append("\n질문: " + content + "\n");
                                    answerField.setEnabled(true);
                                    submitButton.setEnabled(true);
                                    answerField.requestFocus();
                                    break;
                                case "CORRECT":
                                    chatArea.append("✅ " + content + "\n");
                                    break;
                                case "INCORRECT":
                                    chatArea.append("❌ " + content + "\n");
                                    break;
                                case "FINAL_SCORE":
                                    chatArea.append("\n🏆 최종 점수: " + content + "\n");
                                    answerField.setEnabled(false);
                                    submitButton.setEnabled(false);
                                    break;
                            }
                            // 자동 스크롤
                            chatArea.setCaretPosition(chatArea.getDocument().getLength());
                        });
                    }
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> 
                        chatArea.append("\n서버와의 연결이 끊어졌습니다.\n"));
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "서버 연결 실패: " + e.getMessage(), 
                "연결 오류", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 