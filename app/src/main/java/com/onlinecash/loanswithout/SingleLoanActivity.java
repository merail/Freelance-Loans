package com.onlinecash.loanswithout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class SingleLoanActivity extends AppCompatActivity {

    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_POSITION = "position";
    private TextView bankNameTextView;
    private TextView markTextView;
    private TextView rateLabelTextView;
    private TextView rateTextView;
    private TextView sumTextView;
    private TextView timeLabelTextView;
    private TextView termTextView;
    private ImageView visaImageView;
    private ImageView mastercardImageView;
    private ImageView mirImageView;
    private ImageView yandexImageView;
    private ImageView qiwiImageView;
    private ImageView bankLogoImageView;
    private TextView documentsTextView;
    private ImageButton registrationImageButton;
    private ImageButton favouriteImageButton;

    public static Intent newIntent(Context packageContext, String type, String position) {
        Intent intent = new Intent(packageContext, SingleLoanActivity.class);

        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_POSITION, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_loan);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        if (getIntent() != null) {
            String type = getIntent().getStringExtra(EXTRA_TYPE);
            int position = getIntent().getIntExtra(EXTRA_POSITION, 0);

            Loan[] loansArray;

            Gson gson = new Gson();
            if (type.equals("loans")) {
                String jsonLoans = sharedPreferences.getString("loans", "");
                loansArray = gson.fromJson(jsonLoans, Loan[].class);
            } else if (type.equals("cards_credit")) {
                gson = new Gson();
                String jsonCards = sharedPreferences.getString("cards", "");
                Cards[] cards = gson.fromJson(jsonCards, Cards[].class);
                loansArray = cards[0].cards_credit;
            } else if (type.equals("cards_debit")) {
                gson = new Gson();
                String jsonCards = sharedPreferences.getString("cards", "");
                Cards[] cards = gson.fromJson(jsonCards, Cards[].class);
                loansArray = cards[1].cards_debit;
            } else if (type.equals("cards_installment")) {
                gson = new Gson();
                String jsonCards = sharedPreferences.getString("cards", "");
                Cards[] cards = gson.fromJson(jsonCards, Cards[].class);
                loansArray = cards[2].cards_installment;
            } else {
                gson = new Gson();
                String jsonCredits = sharedPreferences.getString("credits", "");
                loansArray = gson.fromJson(jsonCredits, Loan[].class);
            }

            ArrayList<Loan> loans = new ArrayList<>();
            Collections.addAll(loans, loansArray);

            bankNameTextView = findViewById(R.id.bankNameTextView);
            markTextView = findViewById(R.id.markTextView);
            rateLabelTextView = findViewById(R.id.rateLabelTextView);
            rateTextView = findViewById(R.id.rateTextView);
            sumTextView = findViewById(R.id.sumTextView);
            timeLabelTextView = findViewById(R.id.timeLabelTextView);
            termTextView = findViewById(R.id.termTextView);
            visaImageView = findViewById(R.id.visaImageView);
            mastercardImageView = findViewById(R.id.mastercardImageView);
            mirImageView = findViewById(R.id.mirImageView);
            yandexImageView = findViewById(R.id.yandexImageView);
            qiwiImageView = findViewById(R.id.qiwiImageView);
            bankLogoImageView = findViewById(R.id.bankLogoImageView);
            documentsTextView = findViewById(R.id.documentsTextView);
            registrationImageButton = findViewById(R.id.registrationImageButton);
            favouriteImageButton = findViewById(R.id.favouriteImageButton);

            bankNameTextView.setText(loans.get(position).name);

            markTextView.setText(String.valueOf(loans.get(position).score));

            if (loans.get(position).hide_PercentFields == 0) {
                rateLabelTextView.setVisibility(View.VISIBLE);
                String rate = loans.get(position).percentPrefix + " " + loans.get(position).percent + loans.get(position).percentPostfix;
                rateTextView.setText(rate);
            } else {
                rateLabelTextView.setVisibility(View.INVISIBLE);
            }

            String summ = loans.get(position).summPrefix + " " + loans.get(position).summMin + " " + loans.get(position).summMid
                    + " " + loans.get(position).summMax + " " + loans.get(position).summPostfix;
            sumTextView.setText(summ);

            if (loans.get(position).hide_TermFields == 0) {
                timeLabelTextView.setVisibility(View.VISIBLE);

                String term = loans.get(position).termPrefix + " " + loans.get(position).termMin + " " + loans.get(position).termMid
                        + " " + loans.get(position).termMax + " " + loans.get(position).termPostfix;
                termTextView.setText(term);
            } else {
                timeLabelTextView.setVisibility(View.INVISIBLE);
            }

            if (loans.get(position).show_visa == 0)
                visaImageView.setVisibility(View.GONE);
            if (loans.get(position).show_mastercard == 0)
                mastercardImageView.setVisibility(View.GONE);
            if (loans.get(position).show_mir == 0)
                mirImageView.setVisibility(View.GONE);
            if (loans.get(position).show_yandex == 0)
                yandexImageView.setVisibility(View.GONE);
            if (loans.get(position).show_qiwi == 0)
                qiwiImageView.setVisibility(View.GONE);

            Glide.with(getApplicationContext())
                    .load(loans.get(position).screen)
                    .into(bankLogoImageView);

            documentsTextView.setText(Html.fromHtml(loans.get(position).description));

            registrationImageButton.setOnClickListener(view -> getApplicationContext().startActivity(RegistrationActivity
                    .newIntent(getApplicationContext(), loans.get(position).order, loans.get(position).itemId)));

            if (sharedPreferences.contains("favourite:" + loans.get(position).name)) {
                favouriteImageButton.setBackgroundResource(R.drawable.favourite_selected);
            }
            favouriteImageButton.setOnClickListener(view -> {
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

                if (sharedPreferences.contains("favourite:" + loans.get(position).name)) {
                    prefsEditor.remove("favourite:" + loans.get(position).name).apply();

                    favouriteImageButton.setBackgroundResource(R.drawable.favourite);
                } else {
                    favouriteImageButton.setBackgroundResource(R.drawable.favourite_selected);
                    Gson gson1 = new Gson();
                    String json = gson1.toJson(loans.get(position));
                    prefsEditor.putString("favourite:" + loans.get(position).name, json);
                    prefsEditor.apply();
                }
            });
        }
    }
}