package com.onlinecash.loanswithout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final boolean hasConnection = Utils.isNetworkAvailable(getApplicationContext());

        if (hasConnection) {
            Utils.getFirebaseMessagingToken(getApplicationContext());
            Utils.getGoogleAdvertisingId(getApplicationContext());
            Utils.getColor(SplashActivity.this);
            Utils.getInstanceId(getApplicationContext());

            Handler h = new Handler();
            h.postDelayed(this::sendRequest, 2000);
        } else {
            startActivity(MainActivity.newIntent(getApplicationContext(), false, ""));
        }
    }

    private void sendRequest() {
        String simCountryIso = "ru";
        String color = "#00a86b";
        String rootState = "null";
        String locale = "ru_RU";
        //String simCountryIso = Utils.getSimCountryIso(getApplicationContext());
        //String color = Utils.color[0];
        //String rootState = Utils.getRootState(getApplicationContext());
        //String locale = Utils.getLocale();
        String appMetricaAPIKey = Utils.appMetricaAPIKey;
        String androidId = Utils.getAndroidId(getApplicationContext());
        String token = Utils.firebaseMessagingToken[0];
        String googleAdvertisingId = Utils.googleAdvertisingId[0];
        String instanceId = Utils.instanceId[0];

        ActualBackendService actualBackendService = ActualBackendBuilder.build();

        actualBackendService.getActualBackend(simCountryIso, color, rootState, locale, appMetricaAPIKey,
                androidId, token, googleAdvertisingId, instanceId).enqueue(new Callback<ActualBackendJson>() {
            @Override
            public void onResponse(@NonNull Call<ActualBackendJson> call, @NonNull Response<ActualBackendJson> response) {
                ActualBackendJson actualBackendJson = response.body();
                if (actualBackendJson != null) {
                    if (actualBackendJson.actualbackend != null) {
                        startActivity(MainActivity.newIntent(getApplicationContext(), true, actualBackendJson.actualbackend));
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        if (sharedPreferences.getBoolean("ua_accept", false))
                            startActivity(new Intent(SplashActivity.this, OfferActivity.class));
                        else
                            startActivity(new Intent(SplashActivity.this, UserAgreementActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ActualBackendJson> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}