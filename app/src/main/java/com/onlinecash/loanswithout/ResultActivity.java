package com.onlinecash.loanswithout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageButton okImageButton = findViewById(R.id.okImageButton);
        okImageButton.setOnClickListener(view -> startActivity(new Intent(ResultActivity.this, OfferActivity.class)));
    }
}