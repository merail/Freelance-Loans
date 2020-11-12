package com.example.loans;

import android.app.Application;

import com.my.tracker.MyTracker;

public class AnalyticsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MyTracker.initTracker("77704704188920986930", this);
    }
}