package com.reka.tour.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.reka.tour.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by satryaway on 12/20/2015.
 * splash screen for 3 seconds
 */
public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        startActivity();

    }

    private void startActivity() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                SplashScreenActivity.this.finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2000);
    }
}
