package com.onlinecash.loanswithout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserAgreementActivity extends AppCompatActivity {
    private static final String EXTRA_UA = "ua";

    public static Intent newIntent(Context packageContext, String ua) {
        Intent intent = new Intent(packageContext, UserAgreementActivity.class);

        intent.putExtra(EXTRA_UA, ua);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);

        String ua = getIntent().getStringExtra(EXTRA_UA);

        RecyclerView userAgreementRecyclerView = findViewById(R.id.userAgreementRecyclerView);
        userAgreementRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        userAgreementRecyclerView.setAdapter(new UserAgreementAdapter(this, ua));

        ImageButton denyImageButton = findViewById(R.id.denyImageButton);
        denyImageButton.setOnClickListener(view -> finishAffinity());

        ImageButton acceptImageButton = findViewById(R.id.acceptImageButton);
        acceptImageButton.setOnClickListener(view -> {

            SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("ua_accept", true).apply();

            startActivity(new Intent(this, OfferActivity.class));
        });

        CheckBox userAgreementCheckBox = findViewById(R.id.userAgreementCheckbox);
        userAgreementCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
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