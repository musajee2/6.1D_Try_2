package com.example.a61d_try_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UpgradeActivity extends AppCompatActivity {
    private Button starterButton, intermediateButton, advancedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        starterButton = findViewById(R.id.starterButton);
        intermediateButton = findViewById(R.id.intermediateButton);
        advancedButton = findViewById(R.id.advancedButton);

        starterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePurchase("starter_plan");
            }
        });

        intermediateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePurchase("intermediate_plan");
            }
        });

        advancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePurchase("advanced_plan");
            }
        });
    }

    private void initiatePurchase(String plan) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.google.android.apps.nbu.paisa.user"));
        startActivity(intent);
    }
}
