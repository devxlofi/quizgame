import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QuizGameGUI {
    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel splashPanel;
    private JPanel quizPanel;
    private JPanel resultPanel;
    
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel resultLabel;
    private JLabel scoreLabel;
    private QuizClient client;

    public QuizGameGUI(QuizClient client) {
        this.client = client;
        initializeGUI();
    }

    private void initializeGUI() {
        mainFrame = new JFrame("네트워크 퀴즈 게임");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createSplashScreen();
        createQuizPanel();
        createResultPanel();

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
        quizPanel = new JPanel(new BorderLayout(20, 20));
        quizPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));

        answerField = new JTextField();
        answerField.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        
        submitButton = new JButton("제출");
        submitButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        
        submitButton.addActionListener(e -> {
            String answer = answerField.getText().trim();
            if (!answer.isEmpty()) {
                client.sendAnswer(answer);
            }
        });

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

    public void showAnswer(boolean correct) {
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        
        if (correct) {
            questionLabel.setText("정답입니다! 👍");
        } else {
            questionLabel.setText("틀렸습니다! 😢");
        }
        
        // 정답 여부를 2초간 보여준 후 다음 문제로 넘어가도록 수정
        Timer timer = new Timer(2000, e -> {
            answerField.setEnabled(true);
            submitButton.setEnabled(true);
        });
        timer.setRepeats(false);
        timer.start();
    }

    public JTextField getAnswerField() {
        return answerField;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }
} 