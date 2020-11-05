package com.example.loans;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private String TAG = "onComplete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                    }
                });


//        Tracker t = ((AnalyticsApplication) this.getApplication()).getDefaultTracker(
//                TrackerName.APP_TRACKER);
//
//        t.setScreenName(screenName);
//        String campaignData = "http://examplepetstore.com/index.html?" +
//                "utm_source=email&utm_medium=email_marketing&utm_campaign=summer" +
//                "&utm_content=email_variation_1";
//
//        t.send(new HitBuilders.ScreenViewBuilder()
//                .setCampaignParamsFromUrl(campaignData)
//                .build()
//        );
    }
}