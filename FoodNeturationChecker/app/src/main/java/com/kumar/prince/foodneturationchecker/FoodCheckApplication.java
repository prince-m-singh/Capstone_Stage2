package com.kumar.prince.foodneturationchecker;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by prince on 26/8/17.
 */

public class FoodCheckApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            // DebugTree has all usual logging functionality
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        }
    }
}
