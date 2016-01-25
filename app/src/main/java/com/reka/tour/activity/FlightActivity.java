package com.reka.tour.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reka.tour.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FlightActivity extends AppCompatActivity implements View.OnClickListener {
    private static String AIRPORT_CODE = "";
    private static String AIRPORT_NAME = "";
    private static String AIRPORT_LOCATION = "";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.choose_berangkat)
    RelativeLayout chooseBerangkat;
    @Bind(R.id.choose_pulang)
    RelativeLayout choosePulang;
    @Bind(R.id.dari_airport_code)
    TextView dariAirportCode;
    @Bind(R.id.dari_airport_name)
    TextView dariAirportName;
    @Bind(R.id.menuju_airport_code)
    TextView menujuAirportCode;
    @Bind(R.id.menuju_airport_name)
    TextView menujuAirportName;
    private int DARI_AIRPORT = 100;
    private int MENUJU_AIRPORT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);

        initUI();
        setCallBack();


    }

    private void initUI() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.sekali_jalan));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.pulang_pergi));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    choosePulang.setVisibility(View.GONE);
                } else if (position == 1) {
                    choosePulang.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setCallBack() {
        dariAirportCode.setOnClickListener(this);
        menujuAirportCode.setOnClickListener(this);
        choosePulang.setOnClickListener(this);
        choosePulang.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent pickIntent;
        switch (v.getId()) {
            case R.id.dari_airport_code:
                pickIntent = new Intent(FlightActivity.this,
                        AirportChooserActivity.class);
                startActivityForResult(pickIntent, DARI_AIRPORT);
                break;
            case R.id.menuju_airport_code:
                pickIntent = new Intent(FlightActivity.this,
                        AirportChooserActivity.class);
                startActivityForResult(pickIntent, MENUJU_AIRPORT);
                break;

            case R.id.choose_berangkat:

                break;
            case R.id.choose_pulang:
                break;

            default:
                return;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == DARI_AIRPORT) {
                AIRPORT_CODE = data.getStringExtra("AIRPORT_CODE");
                AIRPORT_NAME = data.getStringExtra("AIRPORT_NAME");
                AIRPORT_LOCATION = data.getStringExtra("AIRPORT_LOCATION");
                dariAirportCode.setText(AIRPORT_CODE);
                dariAirportName.setText(AIRPORT_NAME + "\n" + AIRPORT_LOCATION);
            }

            if (requestCode == MENUJU_AIRPORT) {
                AIRPORT_CODE = data.getStringExtra("AIRPORT_CODE");
                AIRPORT_NAME = data.getStringExtra("AIRPORT_NAME");
                AIRPORT_LOCATION = data.getStringExtra("AIRPORT_LOCATION");
                menujuAirportCode.setText(AIRPORT_CODE);
                menujuAirportName.setText(AIRPORT_NAME + "\n" + AIRPORT_LOCATION);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
