package com.example.a61d_try_2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button signInButton, signUpButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        databaseHelper = new DatabaseHelper(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (isValid(email, password)) {
                    login(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private boolean isValid(String email, String password) {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);
    }

    private void login(String email, String password) {
        if (databaseHelper.checkUser(email)) {
            User user = databaseHelper.getUser(email);
            if (user != null && user.getPassword().equals(password)) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }
}
