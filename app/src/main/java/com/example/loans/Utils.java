package com.example.loans;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.internal.common.CommonUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.my.tracker.MyTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

public class Utils {
    public static final String[] googleAdvertisingId = {null};
    public static final String[] token = {null};
    public static String appMetricaAPIKey = "77704704188920986930";

    public static String getSimCountryIso(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimCountryIso();
    }

    public static String getColor(Activity activity) {
        final String[] color = {null};
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(activity, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        try {
                            JSONObject jColor = new JSONObject(firebaseRemoteConfig.getString("color"));
                            color[0] = jColor.getString("color");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        return color[0];
    }

    public static String getRootState(Context context) {
        if (CommonUtils.isRooted(context))
            return "granted";
        else
            return null;
    }

    public static String getLocale() {
        return Locale.getDefault().getCountry();
    }

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void getToken(final Context context) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @SuppressLint("StringFormatInvalid")
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        token[0] = task.getResult();
                    }
                });
    }

    public static void getGoogleAdvertisingId(final Context context) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                    googleAdvertisingId[0] = adInfo != null ? adInfo.getId() : null;
                } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException exception) {
                    exception.printStackTrace();
                }
            }
        };

        thread.start();
    }

    public static String getInstanceId(Context context) {
        return MyTracker.getInstanceId(context);
    }
}
