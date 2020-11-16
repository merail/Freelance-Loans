package com.onlinecash.loanswithout;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        TextView moneyProgressTextView = findViewById(R.id.moneyProgressTextView);

        CircularSeekBar moneySeekBar = findViewById(R.id.moneySeekBar);
        moneySeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                moneyProgressTextView.setText(String.valueOf((int) (5000 + (double) progress / 100 * 25000)));
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
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                timeProgressTextView.setText(String.valueOf((int)(3 + (double) progress / 100 * 27)));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
    }
}