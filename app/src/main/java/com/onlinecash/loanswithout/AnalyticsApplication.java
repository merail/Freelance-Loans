package com.onlinecash.loanswithout;

import android.app.Application;

import com.my.tracker.MyTracker;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class AnalyticsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("3698e262-68c3-4352-8926-8d61610c9db8").build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking(this);

        MyTracker.initTracker("77704704188920986930", this);
    }
}