package com.jixstreet.rekatoursandtravel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.jixstreet.rekatoursandtravel.utils.CommonConstants;

/**
 * Created by satryaway on 4/11/2016.
 */
public class RekaApplication extends Application {
    private static RekaApplication ourInstance;
    private SharedPreferences preferences;

    public synchronized static RekaApplication getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        preferences = getSharedPreferences(CommonConstants.REKA_USAHA, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return preferences;
    }

    public String getToken() {
        return preferences.getString(CommonConstants.TOKEN, "");
    }
}
