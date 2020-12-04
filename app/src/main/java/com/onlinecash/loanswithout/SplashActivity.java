package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;
import com.google.gson.Gson;
import com.my.tracker.MyTracker;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

        // Get user consent
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        AppLinkData.fetchDeferredAppLinkData(this,
                appLinkData -> {
                    if (appLinkData != null) {
                        if (appLinkData.getTargetUri() != null)
                            sharedPreferences.edit().putString("facebook_deep_link", appLinkData.getTargetUri().toString()).apply();
                        else
                            sharedPreferences.edit().putString("facebook_deep_link", "not_available").apply();
                    } else
                        sharedPreferences.edit().putString("facebook_deep_link", "not_available").apply();
                }
        );

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
        String simCountryIso = Utils.getSimCountryIso(getApplicationContext());
        String color = Utils.color[0];
        String rootState = Utils.getRootState(getApplicationContext());
        String locale = Utils.getLocale();
        locale = locale.replace("-", "_");
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
                    if (actualBackendJson.actualbackend != null)
                        sendDateRequest(actualBackendJson.actualbackend);
                    else {
                        String eventParameters = "{\"googleAdvertisingId\":\"" + Utils.googleAdvertisingId[0] + "\"}";
                        YandexMetrica.reportEvent("actualbackend_null", eventParameters);

                        Map<String, String> eventParams = new HashMap<>();
                        eventParams.put("googleAdvertisingId", Utils.googleAdvertisingId[0]);
                        MyTracker.trackEvent("actualbackend_null", eventParams);

                        if (sharedPreferences.getBoolean("ua_accept", false))
                            startActivity(new Intent(SplashActivity.this, OfferActivity.class));
                        else
                            startActivity(UserAgreementActivity.newIntent(getApplicationContext(), null));
                    }
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
        DateService dateService = DateBuilder.build();

        dateService.getDate(Utils.BASE_URL + actualBackend + Utils.DATE_JSON).enqueue(new Callback<DateJson>() {
            @Override
            public void onResponse(@NonNull Call<DateJson> call, @NonNull Response<DateJson> response) {
                YandexMetrica.reportEvent("requestdate");
                MyTracker.trackEvent("requestdate");

                DateJson dateJson = response.body();
                if (dateJson != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    if (Objects.equals(sharedPreferences.getString("date", ""), dateJson.date)) {
                        setData(false);
                    } else {
                        sharedPreferences.edit().putString("date", dateJson.date).apply();

                        sendDatabaseRequest(actualBackend);
                    }
                } else {
                    YandexMetrica.reportEvent("backend_unavailable");
                    MyTracker.trackEvent("backend_unavailable");

                    setData(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DateJson> call, @NonNull Throwable t) {
                t.printStackTrace();

                setData(false);
            }
        });
    }

    private void sendDatabaseRequest(String actualBackend) {
        DatabaseService databaseService = DatabaseBuilder.build();

        databaseService.getDatabase(Utils.BASE_URL + actualBackend + Utils.DATABASE_JSON).enqueue(new Callback<DatabaseJson>() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onResponse(@NonNull Call<DatabaseJson> call, @NonNull Response<DatabaseJson> response) {
                YandexMetrica.reportEvent("requestdb");
                MyTracker.trackEvent("requestdb");

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
                    if (loans != null) {
                        loansArrayList = new ArrayList<>();
                        Collections.addAll(loansArrayList, loans);
                    }
                    String jsonLoans = gson.toJson(loansArrayList);
                    sharedPreferences.edit().putString("loans", jsonLoans).apply();
                    gson = new Gson();
                    if (cards != null) {
                        cardsArrayList = new ArrayList<>();
                        Collections.addAll(cardsArrayList, cards);
                    }
                    String jsonCards = gson.toJson(cardsArrayList);
                    sharedPreferences.edit().putString("cards", jsonCards).apply();
                    gson = new Gson();
                    if (credits != null) {
                        creditsArrayList = new ArrayList<>();
                        Collections.addAll(creditsArrayList, credits);
                    }
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
            user_term_html = appConfig.user_term_html;
        }
        if (appConfig != null) {
            if (appConfig.show_privacy_police == 1) {
                if (sharedPreferences.getBoolean("ua_accept", false))
                    startActivity(new Intent(SplashActivity.this, OfferActivity.class));
                else
                    startActivity(UserAgreementActivity.newIntent(getApplicationContext(), appConfig.privacy_policy_html));
            } else {
                startActivity(MainActivity.newIntent(getApplicationContext(), user_term_html, 0, 0, 0));
            }
        } else {
            startActivity(MainActivity.newIntent(getApplicationContext(), null, 0, 0, 0));
        }
    }
}