package com.mad.cipelist.activity;

import android.content.res.Configuration;

import com.orm.SugarApp;
import com.orm.SugarContext;

public class CipelistApplication extends SugarApp {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(getApplicationContext());
        LocalRecipe.findById(LocalRecipe.class, (long) 1);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}