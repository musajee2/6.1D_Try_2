package com.example.a61d_try_2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {
    @SerializedName("question")
    private String question;

    @SerializedName("correct_answer")
    private String correctAnswer;

    @SerializedName("options")
    private List<String> options;

    public Question(String question, String correctAnswer, List<String> options) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }
}
