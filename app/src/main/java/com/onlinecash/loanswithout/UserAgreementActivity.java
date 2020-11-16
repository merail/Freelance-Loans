package com.onlinecash.loanswithout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class UserAgreementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);

        ImageButton denyImageButton = findViewById(R.id.denyImageButton);
        denyImageButton.setOnClickListener(view -> finishAffinity());

        ImageButton acceptImageButton = findViewById(R.id.acceptImageButton);
        acceptImageButton.setOnClickListener(view -> {
            startActivity(new Intent(this, OfferActivity.class));
        });
    }
}