package com.example.a61d_try_2;

public class QuizAttempt {
    private int id;
    private int userId;
    private String quizTopic;
    private int totalQuestions;
    private int correctAnswers;
    private boolean completed;

    public QuizAttempt(int id, int userId, String quizTopic, int totalQuestions, int correctAnswers, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.quizTopic = quizTopic;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getQuizTopic() {
        return quizTopic;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
