package com.example.a61d_try_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {
    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button submitButton;
    private TextView progressTextView;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private DatabaseHelper databaseHelper;
    private int userId;  // Retrieve this from the logged-in user session
    private long attemptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        String interest = intent.getStringExtra("interest");

        questionTextView = findViewById(R.id.questionTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        submitButton = findViewById(R.id.submitButton);
        progressTextView = findViewById(R.id.progressTextView);

        databaseHelper = new DatabaseHelper(this);

        // Assume userId is retrieved from the logged-in user session
        userId = getIntent().getIntExtra("userId", -1);

        fetchQuestions(interest);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    private void fetchQuestions(String interest) {
        RetrofitClient.getInstance().fetchQuestions(interest, new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questions = response.body().getQuestions();
                    if (questions != null && !questions.isEmpty()) {
                        Collections.shuffle(questions);
                        displayQuestion(currentQuestionIndex);

                        // Start a new quiz attempt
                        attemptId = databaseHelper.addQuizAttempt(userId, interest, questions.size(), 0, false);
                    } else {
                        Toast.makeText(QuizActivity.this, "No questions available", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(QuizActivity.this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayQuestion(int index) {
        int progress = currentQuestionIndex + 1;
        progressTextView.setText("Progress: " + progress + "/" + questions.size());

        Question currentQuestion = questions.get(index);
        questionTextView.setText(currentQuestion.getQuestion());
        optionsRadioGroup.clearCheck();
        optionsRadioGroup.removeAllViews();

        List<String> options = currentQuestion.getOptions();
        Collections.shuffle(options);

        for (String option : options) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            optionsRadioGroup.addView(radioButton);
        }
    }

    private void checkAnswer() {
        int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();
        if (selectedOptionId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedOptionId);
            if (selectedRadioButton != null) {
                String selectedAnswer = selectedRadioButton.getText().toString();
                if (true/*selectedAnswer.equals(questions.get(currentQuestionIndex).getCorrectAnswer())*/) {
                    correctAnswers++;
                }
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    displayQuestion(currentQuestionIndex);
                } else {
                    showFinalScore();
                }
            } else {
                Toast.makeText(this, "Error: Selected radio button is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFinalScore() {
        databaseHelper.updateQuizAttempt(attemptId, correctAnswers, true);
        Intent intent = new Intent(QuizActivity.this, FinalScoreActivity.class);
        intent.putExtra("final_score", correctAnswers);
        startActivity(intent);
        finish();
    }
}
