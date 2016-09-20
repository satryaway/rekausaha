package com.jixstreet.rekatoursandtravel.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.Session.CountrySessionManager;
import com.jixstreet.rekatoursandtravel.fragment.AboutFragment;
import com.jixstreet.rekatoursandtravel.fragment.FeedbackFragment;
import com.jixstreet.rekatoursandtravel.fragment.HomeFragment;
import com.jixstreet.rekatoursandtravel.fragment.NewsFragment;
import com.jixstreet.rekatoursandtravel.model.Country;
import com.jixstreet.rekatoursandtravel.utils.Builder;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar_icon)
    ImageView toolbarIcon;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private Fragment fragment;
    private CountrySessionManager countrySessionManager;
    private SharedPreferences sharedPreferences;
    private boolean isRefreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        isRefreshToken = intent.getBooleanExtra(CommonConstants.REFRESH_TOKEN, false);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences(CommonConstants.REKA_USAHA, MODE_PRIVATE);
        String token = sharedPreferences.getString(CommonConstants.TOKEN, "");

        if (token.isEmpty() || isRefreshToken) {
            getAccessToken();
        }

        countrySessionManager = new CountrySessionManager(this);
        if (countrySessionManager.getCountry(this) == null) {
            getCountry();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = HomeFragment.newInstance(this);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }
    }

    private void getCountry() {
        Builder.getInstance(this).getCountry(new Builder.CallBackCountry() {
            @Override
            public void onSucces(ArrayList<Country> countries) {
                Log.e("countries", countries.size() + "");
                countrySessionManager.saveCountry(MainActivity.this, countries);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_beranda) {
            toolbarIcon.setVisibility(View.VISIBLE);
            fragment = HomeFragment.newInstance(this);
        } else if (id == R.id.nav_kabar_berita) {
            toolbarIcon.setVisibility(View.VISIBLE);
            fragment = NewsFragment.newInstance();
        } else if (id == R.id.nav_kritik_saran) {
            toolbarIcon.setVisibility(View.VISIBLE);
            fragment = FeedbackFragment.newInstance();
        } else if (id == R.id.nav_tentang) {
            toolbarIcon.setVisibility(View.GONE);
            fragment = AboutFragment.newInstance();
        } else if (id == R.id.nav_booking) {
            Intent intent = new Intent(MainActivity.this, MyBookingActivity.class);
            startActivity(intent);
            finish();
        }

        if (fragment != null) {
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getAccessToken() {
        String url = CommonConstants.BASE_URL + "apiv1/payexpress";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.METHOD, "getToken");
        requestParams.put(CommonConstants.SECRET_KEY, getString(R.string.secret_key));
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.addHeader("user-agent", "twh:[22490422]:[Reka Tours dan Travel]");
        client.setTimeout(100000);
        client.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String token = response.getString(CommonConstants.TOKEN);
                    sharedPreferences.edit().putString(CommonConstants.TOKEN, token).apply();
                    Log.d("token", token);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MainActivity.this, "Request Timed Out", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
