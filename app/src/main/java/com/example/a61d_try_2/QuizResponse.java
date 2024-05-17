package com.example.a61d_try_2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizResponse {

    @SerializedName("quiz")
    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
