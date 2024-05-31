package com.example.a61d_try_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView usernameTextView, emailTextView, totalQuestionsTextView, correctAnswersTextView, incorrectAnswersTextView;
    private Button generateQuizButton, upgradeButton, shareButton;
    private DatabaseHelper databaseHelper;
    private User user;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        totalQuestionsTextView = findViewById(R.id.totalQuestionsTextView);
        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        incorrectAnswersTextView = findViewById(R.id.incorrectAnswersTextView);
        generateQuizButton = findViewById(R.id.generateQuizButton);
        upgradeButton = findViewById(R.id.upgradeButton);
        shareButton = findViewById(R.id.shareButton);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        generateQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra("userId", user.getId());
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpgradeActivity.class);
                startActivity(intent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareProfile();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        user = databaseHelper.getUser(email);

        if (user != null) {
            usernameTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());

            List<QuizAttempt> attempts = databaseHelper.getQuizAttempts(user.getId());
            int totalQuestions = 0;
            int correctAnswers = 0;
            for (QuizAttempt attempt : attempts) {
                totalQuestions += attempt.getTotalQuestions();
                correctAnswers += attempt.getCorrectAnswers();
            }
            totalQuestionsTextView.setText("Total Questions: " + totalQuestions);
            correctAnswersTextView.setText("Correct Answers: " + correctAnswers);
            incorrectAnswersTextView.setText("Incorrect Answers: " + (totalQuestions - correctAnswers));
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareProfile() {
        String shareText = "Username: " + user.getUsername() + "\nEmail: " + user.getEmail() +
                "\nTotal Questions: " + totalQuestionsTextView.getText().toString() +
                "\nCorrect Answers: " + correctAnswersTextView.getText().toString() +
                "\nIncorrect Answers: " + incorrectAnswersTextView.getText().toString();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share profile via"));
    }
}
