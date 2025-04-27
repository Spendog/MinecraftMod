package com.example.educationmod.content;

public class QuizContent {
    private String question;
    private String[] answers;
    private int correctAnswerIndex;

    public QuizContent(String question, String[] answers, int correctAnswerIndex) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

public String getAnswer() {
    if (answers != null && correctAnswerIndex >= 0 && correctAnswerIndex < answers.length) {
        return answers[correctAnswerIndex];
    }
    return "";
}
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
