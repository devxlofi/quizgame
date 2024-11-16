class Question {
    private String question;
    private String answer;

    /**
     * @param question 문제 내용
     * @param answer 문제의 정답
     */
    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
} 