package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class OfferActivity extends AppCompatActivity {

    private int money = 17500;
    private int time = 77;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        TextView sumToReturnTextView = findViewById(R.id.sumToReturnTextView);

        TextView moneyProgressTextView = findViewById(R.id.moneyProgressTextView);
        CircularSeekBar moneySeekBar = findViewById(R.id.moneySeekBar);
        moneySeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                money = (int) (5000 + (double) progress / 100 * 25000);
                moneyProgressTextView.setText(money + " руб.");
                sumToReturnTextView.setText(String.valueOf((int)(time * 0.001 * money + money)));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        TextView timeProgressTextView = findViewById(R.id.timeProgressTextView);
        CircularSeekBar timeSeekBar = findViewById(R.id.timeSeekBar);
        timeSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                time = (int) (61 + (double) progress / 100 * 32);
                timeProgressTextView.setText(time + " дней");
                sumToReturnTextView.setText(String.valueOf((int)(time * 0.001 * money + money)));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        ImageButton getImageButton = findViewById(R.id.getImageButton);
        getImageButton.setOnClickListener(view -> startActivity(new Intent(OfferActivity.this, FormActivity.class)));
    }
}