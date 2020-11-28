package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.crashlytics.internal.common.CommonUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.my.tracker.MyTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class Utils {
    public static final String LOANS_FRAGMENT_TAG = "onComplete";
    public static final String CARDS_FRAGMENT_TAG = "onComplete";
    public static final String CREDITS_FRAGMENT_TAG = "onComplete";
    public static final String FAVOURITES_FRAGMENT_TAG = "onComplete";

    public static final String[] color = {null};
    public static final String[] googleAdvertisingId = {null};
    public static final String[] firebaseMessagingToken = {null};
    public static final String[] instanceId = {null};
    public static String appMetricaAPIKey = "3698e262-68c3-4352-8926-8d61610c9db8";

    public static String getSimCountryIso(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimCountryIso();
    }

    public static void getColor(Activity activity) {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(activity, task -> {
                    try {
                        JSONObject jColor = new JSONObject(firebaseRemoteConfig.getString("cl"));
                        color[0] = jColor.get("color").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static String getRootState(Context context) {
        if (CommonUtils.isRooted(context))
            return "granted";
        else
            return "null";
    }

    public static String getLocale() {
        return Locale.getDefault().toLanguageTag();
    }

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void getFirebaseMessagingToken(final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        firebaseMessagingToken[0] = sharedPreferences.getString("firebaseMessagingToken", "");
        if (Objects.requireNonNull(firebaseMessagingToken[0]).isEmpty()) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        firebaseMessagingToken[0] = task.getResult();
                        if (firebaseMessagingToken[0] == null || firebaseMessagingToken[0].isEmpty())
                            firebaseMessagingToken[0] = "not_available";
                        sharedPreferences.edit().putString("firebaseMessagingToken", googleAdvertisingId[0]).apply();
                    });
        }
    }

    public static void getGoogleAdvertisingId(final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        googleAdvertisingId[0] = sharedPreferences.getString("googleAdvertisingId", "");
        if (Objects.requireNonNull(googleAdvertisingId[0]).isEmpty()) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                        googleAdvertisingId[0] = adInfo.getId();
                        if (googleAdvertisingId[0] == null || googleAdvertisingId[0].isEmpty())
                            googleAdvertisingId[0] = "not_available";
                        sharedPreferences.edit().putString("googleAdvertisingId", googleAdvertisingId[0]).apply();
                    } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException exception) {
                        exception.printStackTrace();
                        sharedPreferences.edit().putString("googleAdvertisingId", googleAdvertisingId[0]).apply();
                    }
                }
            };

            thread.start();
        }
    }

    public static void getInstanceId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        instanceId[0] = sharedPreferences.getString("instanceId", "");
        if (Objects.requireNonNull(instanceId[0]).isEmpty()) {
            Thread thread = new Thread(() -> {
                instanceId[0] = MyTracker.getInstanceId(context);
                if (instanceId[0].isEmpty())
                    instanceId[0] = "not_available";
                sharedPreferences.edit().putString("instanceId", instanceId[0]).apply();
            });

            thread.start();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
