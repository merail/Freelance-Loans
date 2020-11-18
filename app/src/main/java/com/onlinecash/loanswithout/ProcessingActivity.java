package com.onlinecash.loanswithout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class ProcessingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        ProgressBar processingProgressBar = findViewById(R.id.processingProgressBar);

        new Thread(() -> {
            int currentPosition= 0;
            while (currentPosition <= 100) {
                try {
                    processingProgressBar.setProgress(currentPosition);
                    Thread.sleep(3000);
                    currentPosition++;
                } catch (Exception e) {
                    return;
                }
            }

            startActivity(new Intent(ProcessingActivity.this, ResultActivity.class));
        }).start();
    }
}