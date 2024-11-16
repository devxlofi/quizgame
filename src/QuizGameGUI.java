import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QuizGameGUI {
    // GUI 컴포넌트들을 위한 필드 선언
    private JFrame mainFrame;          // 메인 프레임
    private CardLayout cardLayout;     // 화면 전환을 위한 카드 레이아웃
    private JPanel cardPanel;          // 카드 레이아웃을 포함할 패널
    private JPanel splashPanel;        // 시작 화면 패널
    private JPanel quizPanel;          // 퀴즈 화면 패널
    private JPanel resultPanel;        // 결과 화면 패널
    
    // 퀴즈 관련 컴포넌트들
    private JLabel questionLabel;      // 질문을 표시할 레이블
    private JTextField answerField;    // 답변 입력 필드
    private JButton submitButton;      // 제출 버튼
    private JLabel scoreLabel;         // 점수 표시 레이블
    private QuizClient client;         // 퀴즈 클라이언트 객체

    public QuizGameGUI(QuizClient client) {
        this.client = client;
        initializeGUI();
    }

    private void initializeGUI() {
        // 메인 프레임 설정
        mainFrame = new JFrame("네트워크 퀴즈 게임");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);  // 화면 중앙에 위치

        // 카드 레이아웃 설정
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 각 화면 생성 및 추가
        createSplashScreen();
        createQuizPanel();
        createResultPanel();

        // 카드 패널에 각 화면 추가
        cardPanel.add(splashPanel, "SPLASH");
        cardPanel.add(quizPanel, "QUIZ");
        cardPanel.add(resultPanel, "RESULT");

        mainFrame.add(cardPanel);
        showSplashScreen();
    }

    private void createSplashScreen() {
        splashPanel = new JPanel(new BorderLayout());
        splashPanel.setBackground(new Color(41, 128, 185));

        JLabel titleLabel = new JLabel("네트워크 퀴즈 게임", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JButton startButton = new JButton("게임 시작");
        startButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        startButton.addActionListener(e -> client.startGame());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);

        splashPanel.add(titleLabel, BorderLayout.CENTER);
        splashPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createQuizPanel() {
        // 퀴즈 패널 기본 설정
        quizPanel = new JPanel(new BorderLayout(20, 20));
        quizPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 질문 레이블 설정
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));

        // 답변 입력 필드 설정
        answerField = new JTextField();
        answerField.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        
        // 제출 버튼 설정
        submitButton = new JButton("제출");
        submitButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        
        // 제출 버튼 클릭 이벤트 처리
        submitButton.addActionListener(e -> {
            String answer = answerField.getText().trim();
            if (!answer.isEmpty()) {
                client.sendAnswer(answer);
            }
        });

        // Enter 키 입력 이벤트 처리
        answerField.addActionListener(e -> {
            String answer = answerField.getText().trim();
            if (!answer.isEmpty()) {
                client.sendAnswer(answer);
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.add(new JLabel("A. "), BorderLayout.WEST);
        inputPanel.add(answerField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);

        quizPanel.add(questionLabel, BorderLayout.CENTER);
        quizPanel.add(inputPanel, BorderLayout.SOUTH);
    }

    private void createResultPanel() {
        resultPanel = new JPanel(new BorderLayout(20, 20));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));

        JButton restartButton = new JButton("다시 시작");
        restartButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        restartButton.addActionListener(e -> client.restartGame());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restartButton);

        resultPanel.add(scoreLabel, BorderLayout.CENTER);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showSplashScreen() {
        cardLayout.show(cardPanel, "SPLASH");
        mainFrame.setVisible(true);
    }

    public void showQuizScreen() {
        cardLayout.show(cardPanel, "QUIZ");
    }

    public void showResultScreen(String score) {
        scoreLabel.setText("최종 점수: " + score);
        cardLayout.show(cardPanel, "RESULT");
    }

    public void setQuestion(String question) {
        questionLabel.setText("Q. " + question);
        answerField.setText("");
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
        answerField.requestFocus();
    }

    // 정답 확인 후 결과 표시
    public void showAnswer(boolean isCorrect, String correctAnswer) {
        // 입력 필드와 제출 버튼 비활성화
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        
        // 정답/오답 메시지 표시
        if (isCorrect) {
            questionLabel.setText("정답입니다! 👍\n정답: " + correctAnswer);
        } else {
            questionLabel.setText("틀렸습니다! 😢\n정답: " + correctAnswer);
        }
    }

    public JTextField getAnswerField() {
        return answerField;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }
} 