package com.onlinecash.loanswithout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

public class UserAgreementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);

        RecyclerView userAgreementRecyclerView = findViewById(R.id.userAgreementRecyclerView);
        userAgreementRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        userAgreementRecyclerView.setAdapter(new UserAgreementAdapter(this));

        ImageButton denyImageButton = findViewById(R.id.denyImageButton);
        denyImageButton.setOnClickListener(view -> finishAffinity());

        ImageButton acceptImageButton = findViewById(R.id.acceptImageButton);
        acceptImageButton.setOnClickListener(view -> {
            startActivity(new Intent(this, OfferActivity.class));
        });

        CheckBox userAgreementCheckBox = findViewById(R.id.userAgreementCheckbox);
        userAgreementCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                acceptImageButton.setVisibility(View.VISIBLE);
            else
                acceptImageButton.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }
}