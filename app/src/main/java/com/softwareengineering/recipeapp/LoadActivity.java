package com.softwareengineering.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class LoadActivity extends AppCompatActivity {
    private static final int LOAD_SCREEN_TIME = 3000;
    private static final int LOAD_SCREEN_INTERVAL = 100;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);


        progressBar = findViewById(R.id.progressBar_load);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(LOAD_SCREEN_TIME / LOAD_SCREEN_INTERVAL);


        new CountDownTimer(LOAD_SCREEN_TIME, LOAD_SCREEN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

                int progress = (int) ((LOAD_SCREEN_TIME - millisUntilFinished) / LOAD_SCREEN_INTERVAL);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                // Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                Intent intent = new Intent(LoadActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
