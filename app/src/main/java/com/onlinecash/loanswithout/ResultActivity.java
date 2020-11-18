package com.onlinecash.loanswithout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageButton okImageButton = findViewById(R.id.okImageButton);
        okImageButton.setOnClickListener(view -> startActivity(new Intent(ResultActivity.this, OfferActivity.class)));
    }
}