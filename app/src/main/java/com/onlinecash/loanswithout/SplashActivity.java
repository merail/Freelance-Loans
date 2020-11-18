package com.onlinecash.loanswithout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

        final boolean hasConnection = isNetworkAvailable();

        if (hasConnection) {
            Utils.getToken(getApplicationContext());
            Utils.getGoogleAdvertisingId(getApplicationContext());
        }

        Handler h = new Handler();
        h.postDelayed(() -> {
            String simCountryIso = Utils.getSimCountryIso(getApplicationContext());
            String color = Utils.getColor(SplashActivity.this);
            String rootState = Utils.getRootState(getApplicationContext());
            String locale = Utils.getLocale();
            String appMetricaAPIKey = Utils.appMetricaAPIKey;
            String androidId = Utils.getAndroidId(getApplicationContext());
            String token = Utils.token[0];
            String googleAdvertisingId = Utils.googleAdvertisingId[0];
            String instanceId = Utils.getInstanceId(getApplicationContext());

            Service service = ServiceBuilder.build();

            service.getJson(simCountryIso, color, rootState, locale, appMetricaAPIKey,
                    androidId, token, googleAdvertisingId, instanceId).enqueue(new Callback<Json>() {
                @Override
                public void onResponse(@NonNull Call<Json> call, @NonNull Response<Json> response) {
                    Json json = response.body();
                    if (json != null) {
                        if(json.actualBackend != null)
                        {
                            startActivity(MainActivity.newIntent(getApplicationContext(), hasConnection));
                        }
                        else
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                            if(sharedPreferences.getBoolean("ua_accept", false))
                                startActivity(new Intent(SplashActivity.this, OfferActivity.class));
                            else
                                startActivity(new Intent(SplashActivity.this, UserAgreementActivity.class));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Json> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }, 2000);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}