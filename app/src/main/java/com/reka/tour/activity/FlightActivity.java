package com.reka.tour.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reka.tour.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
    RelativeLayout dpDeparture;
    @Bind(R.id.date_berangkat)
    TextView tvDeparture;
    @Bind(R.id.choose_pulang)
    RelativeLayout dpAperture;
    @Bind(R.id.date_pulang)
    TextView tvAperture;

    @Bind(R.id.dari_airport_code)
    TextView dariAirportCode;
    @Bind(R.id.dari_airport_name)
    TextView dariAirportName;
    @Bind(R.id.menuju_airport_code)
    TextView menujuAirportCode;
    @Bind(R.id.menuju_airport_name)
    TextView menujuAirportName;

    @Bind(R.id.tv_adult_total)
    TextView tvAdultTotal;
    @Bind(R.id.btn_adult_minus)
    Button btnAdultMinus;
    @Bind(R.id.btn_adult_plus)
    Button btnAdultPlus;

    @Bind(R.id.tv_child_total)
    TextView tvChildTotal;
    @Bind(R.id.btn_child_minus)
    Button btnChildMinus;
    @Bind(R.id.btn_child_plus)
    Button btnChildPlus;

    @Bind(R.id.tv_baby_total)
    TextView tvBabyTotal;
    @Bind(R.id.btn_baby_minus)
    Button btnBabyMinus;
    @Bind(R.id.btn_baby_plus)
    Button btnBabyPlus;

    @Bind(R.id.cari_pesawat)
    TextView cariPesawat;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;


    private int DARI_AIRPORT = 100;
    private int MENUJU_AIRPORT = 200;
    private int totalAdult = 0;
    private int totalChild = 0;
    private int totalBaby = 0;
    private SimpleDateFormat dateFormatter;

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
                    dpAperture.setVisibility(View.GONE);
                } else if (position == 1) {
                    dpAperture.setVisibility(View.VISIBLE);
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
        dpDeparture.setOnClickListener(this);
        dpAperture.setOnClickListener(this);
        cariPesawat.setOnClickListener(this);

        btnAdultMinus.setOnClickListener(this);
        btnAdultPlus.setOnClickListener(this);
        btnChildMinus.setOnClickListener(this);
        btnChildPlus.setOnClickListener(this);
        btnBabyMinus.setOnClickListener(this);
        btnBabyPlus.setOnClickListener(this);
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

        final Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("EEEE , dd MMMM yyyy", new Locale("ind", "IDN"));

        totalAdult = Integer.parseInt(tvAdultTotal.getText().toString());
        totalChild = Integer.parseInt(tvChildTotal.getText().toString());
        totalBaby = Integer.parseInt(tvBabyTotal.getText().toString());

        switch (v.getId()) {
            case R.id.dari_airport_code:
                pickIntent = new Intent(FlightActivity.this,
                        AirportChooserActivity.class);
                pickIntent.putExtra("title", getString(R.string.pilih_kota_keberangkatan));
                startActivityForResult(pickIntent, DARI_AIRPORT);
                break;
            case R.id.menuju_airport_code:
                pickIntent = new Intent(FlightActivity.this,
                        AirportChooserActivity.class);
                pickIntent.putExtra("title", getString(R.string.pilih_kota_tujuan));
                startActivityForResult(pickIntent, MENUJU_AIRPORT);
                break;

            case R.id.choose_berangkat:
                new DatePickerDialog(FlightActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tvDeparture.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.choose_pulang:
                new DatePickerDialog(FlightActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tvAperture.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.btn_adult_minus:
                if (totalAdult > 1) {
                    totalAdult--;
                    tvAdultTotal.setText(totalAdult + "");
                }
                break;
            case R.id.btn_adult_plus:
                if (totalAdult > 0 && totalAdult < 6) {
                    totalAdult++;
                    tvAdultTotal.setText(totalAdult + "");
                }
                break;
            case R.id.btn_child_minus:
                if (totalChild > 0) {
                    totalChild--;
                    tvChildTotal.setText(totalChild + "");
                }
                break;
            case R.id.btn_child_plus:
                if (totalChild >= 0 && totalChild < 6) {
                    totalChild++;
                    tvChildTotal.setText(totalChild + "");
                }
                break;
            case R.id.btn_baby_minus:
                if (totalBaby > 0) {
                    totalBaby--;
                    tvBabyTotal.setText(totalBaby + "");
                }
                break;
            case R.id.btn_baby_plus:
                if (totalBaby >= 0 && totalBaby < 6) {
                    totalBaby++;
                    tvBabyTotal.setText(totalBaby + "");
                }
                break;
            case R.id.cari_pesawat:
                startActivity(new Intent(FlightActivity.this, DepartureActivity.class));
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
