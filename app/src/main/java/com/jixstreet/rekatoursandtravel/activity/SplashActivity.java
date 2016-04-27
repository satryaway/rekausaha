package com.jixstreet.rekatoursandtravel.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity {

    private String token;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        startAnActivity();
        /*sharedPreferences = this.getSharedPreferences(CommonConstants.REKA_USAHA, MODE_PRIVATE);
        String url = CommonConstants.BASE_URL + "apiv1/payexpress";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.METHOD, "getToken");
        requestParams.put(CommonConstants.SECRET_KEY, getString(R.string.secret_key));
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
        client.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String token = response.getString(CommonConstants.TOKEN);
                    sharedPreferences.edit().putString(CommonConstants.TOKEN, token).apply();
                    Log.d("token", token);

                    startAnActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SplashActivity.this, "Request Timed Out", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });*/
    }

    private void startAnActivity() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2000);
    }
}
