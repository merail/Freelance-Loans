package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private AppConfig appConfig;
    private ArrayList<Loan> loansArrayList;
    private ArrayList<Cards> cardsArrayList;
    private ArrayList<Loan> creditsArrayList;

    private String user_term_html;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        final boolean hasConnection = Utils.isNetworkAvailable(getApplicationContext());

        if (hasConnection) {
            Utils.getFirebaseMessagingToken(getApplicationContext());
            Utils.getGoogleAdvertisingId(getApplicationContext());
            Utils.getColor(SplashActivity.this);
            Utils.getInstanceId(getApplicationContext());

            Handler h = new Handler();
            h.postDelayed(this::sendRequest, 2000);
        } else {
            Handler h = new Handler();
            h.postDelayed(() -> setData(false), 2000);
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
                    sendDateRequest(actualBackendJson.actualbackend);
                } else {
                    setData(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ActualBackendJson> call, @NonNull Throwable t) {
                t.printStackTrace();

                setData(false);
            }
        });
    }

    private void sendDateRequest(String actualBackend) {
        DateService dateService = DateBuilder.build(actualBackend);

        dateService.getDate().enqueue(new Callback<DateJson>() {
            @Override
            public void onResponse(@NonNull Call<DateJson> call, @NonNull Response<DateJson> response) {
                DateJson dateJson = response.body();
                if (dateJson != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    if (Objects.equals(sharedPreferences.getString("date", ""), dateJson.date)) {
                        setData(false);
                    } else {
                        sharedPreferences.edit().putString("date", dateJson.date).apply();

                        sendDatabaseRequest(actualBackend);
                    }
                } else
                    setData(false);
            }

            @Override
            public void onFailure(@NonNull Call<DateJson> call, @NonNull Throwable t) {
                t.printStackTrace();

                setData(false);
            }
        });
    }

    private void sendDatabaseRequest(String actualBackend) {
        DatabaseService databaseService = DatabaseBuilder.build(actualBackend);

        databaseService.getDatabase().enqueue(new Callback<DatabaseJson>() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onResponse(@NonNull Call<DatabaseJson> call, @NonNull Response<DatabaseJson> response) {
                DatabaseJson databaseJson = response.body();
                if (databaseJson != null) {
                    appConfig = databaseJson.app_config;
                    Loan[] loans = databaseJson.loans;
                    Cards[] cards = databaseJson.cards;
                    Loan[] credits = databaseJson.credits;

                    user_term_html = appConfig.user_term_html;

                    Gson gson = new Gson();
                    String jsonAppConfig = gson.toJson(appConfig);
                    sharedPreferences.edit().putString("app_config", jsonAppConfig).apply();
                    gson = new Gson();
                    loansArrayList = new ArrayList<>();
                    Collections.addAll(loansArrayList, loans);
                    String jsonLoans = gson.toJson(loansArrayList);
                    sharedPreferences.edit().putString("loans", jsonLoans).apply();
                    gson = new Gson();
                    cardsArrayList = new ArrayList<>();
                    Collections.addAll(cardsArrayList, cards);
                    String jsonCards = gson.toJson(cardsArrayList);
                    sharedPreferences.edit().putString("cards", jsonCards).apply();
                    gson = new Gson();
                    creditsArrayList = new ArrayList<>();
                    Collections.addAll(creditsArrayList, credits);
                    String jsonCredits = gson.toJson(creditsArrayList);
                    sharedPreferences.edit().putString("credits", jsonCredits).apply();

                    setData(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DatabaseJson> call, @NonNull Throwable t) {
                t.printStackTrace();

                setData(false);
            }
        });
    }

    private void setData(boolean isNewData) {
        if (!isNewData) {
            Gson gson = new Gson();

            String jsonAppConfig = sharedPreferences.getString("app_config", "");
            appConfig = gson.fromJson(jsonAppConfig, AppConfig.class);

        }
        if (appConfig != null) {
            if (appConfig.show_privacy_police == 1) {
                if (sharedPreferences.getBoolean("ua_accept", false))
                    startActivity(new Intent(SplashActivity.this, OfferActivity.class));
                else
                    startActivity(UserAgreementActivity.newIntent(getApplicationContext(), appConfig.privacy_policy_html));
            } else {
                startActivity(MainActivity.newIntent(getApplicationContext(), user_term_html));
            }
        } else {
            startActivity(MainActivity.newIntent(getApplicationContext(), null));
        }

    }
}