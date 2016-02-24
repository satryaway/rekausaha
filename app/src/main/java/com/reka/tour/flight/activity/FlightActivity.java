package com.reka.tour.flight.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.reka.tour.R;
import com.reka.tour.utils.CommonConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FlightActivity extends AppCompatActivity implements View.OnClickListener {
    private static String AIRPORT_CODE_D = "";
    private static String AIRPORT_NAME_D = "";
    private static String AIRPORT_LOCATION_D = "";
    private static String AIRPORT_CODE_A = "";
    private static String AIRPORT_NAME_A = "";
    private static String AIRPORT_LOCATION_A = "";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.choose_berangkat)
    RelativeLayout dpDeparture;
    @Bind(R.id.date_berangkat)
    TextView tvDeparture;
    @Bind(R.id.choose_pulang)
    RelativeLayout dpArrival;
    @Bind(R.id.date_pulang)
    TextView tvArrival;

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

    private int DARI_AIRPORT = 100;
    private int MENUJU_AIRPORT = 200;
    private int totalAdult = 0;
    private int totalChild = 0;
    private int totalBaby = 0;
    private String dateDeparture;
    private String dateArrival;
    private SimpleDateFormat dateDayFormatter;
    private SimpleDateFormat dateFormatter;
    private Calendar newCalendar;

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

        dariAirportName.setSelected(true);
        menujuAirportName.setSelected(true);

        newCalendar = Calendar.getInstance();
        dateDayFormatter = new SimpleDateFormat("EEEE , dd MMMM yyyy", new Locale("ind", "IDN"));
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        //date now
        tvDeparture.setText(dateDayFormatter.format(newCalendar.getTime()));
        dateDeparture = dateFormatter.format(newCalendar.getTime());

        //date next week
        newCalendar.add(Calendar.DATE, 6);
        tvArrival.setText(dateDayFormatter.format(newCalendar.getTime()));

        tabLayout.addTab(tabLayout.newTab().setText(R.string.sekali_jalan));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.pulang_pergi));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    dpArrival.setVisibility(View.GONE);
                    dateArrival = "";
                } else if (position == 1) {
                    dpArrival.setVisibility(View.VISIBLE);
                    dateArrival = dateFormatter.format(newCalendar.getTime());
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
        dpArrival.setOnClickListener(this);
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


        totalAdult = Integer.parseInt(tvAdultTotal.getText().toString());
        totalChild = Integer.parseInt(tvChildTotal.getText().toString());
        totalBaby = Integer.parseInt(tvBabyTotal.getText().toString());

        switch (v.getId()) {
            case R.id.dari_airport_code:
                pickIntent = new Intent(FlightActivity.this,
                        AirportChooserActivity.class);
                pickIntent.putExtra("title", getString(R.string.pilih_kota_keberangkatan));
                pickIntent.putExtra(CommonConstants.FLIGHT, CommonConstants.DEPARTURE);
                startActivityForResult(pickIntent, DARI_AIRPORT);
                break;
            case R.id.menuju_airport_code:
                pickIntent = new Intent(FlightActivity.this,
                        AirportChooserActivity.class);
                pickIntent.putExtra("title", getString(R.string.pilih_kota_tujuan));
                pickIntent.putExtra(CommonConstants.FLIGHT, CommonConstants.APERTURE);
                startActivityForResult(pickIntent, MENUJU_AIRPORT);
                break;

            case R.id.choose_berangkat:
                new CalendarDatePickerDialogFragment().setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tvDeparture.setText(dateDayFormatter.format(newDate.getTime()));
                        dateDeparture = dateFormatter.format(newDate.getTime());
                    }
                }).show(getSupportFragmentManager(), "DATEPICKER");
                break;
            case R.id.choose_pulang:
                new CalendarDatePickerDialogFragment().setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tvArrival.setText(dateDayFormatter.format(newDate.getTime()));
                        dateArrival = dateFormatter.format(newDate.getTime());
                    }
                }).show(getSupportFragmentManager(), "DATEPICKER");
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
                if (dariAirportCode.getText().toString().isEmpty() || menujuAirportCode.getText().toString().isEmpty()) {
                    Toast.makeText(FlightActivity.this, "Isi yang masih kosong", Toast.LENGTH_LONG).show();
                } else {
                    Intent findFlightIntent = new Intent(FlightActivity.this, DepartureActivity.class);
                    findFlightIntent.putExtra(CommonConstants.AIRPORT_CODE_D, AIRPORT_CODE_D);
                    findFlightIntent.putExtra(CommonConstants.AIRPORT_CODE_A, AIRPORT_CODE_A);
                    findFlightIntent.putExtra(CommonConstants.AIRPORT_LOCATION_D, AIRPORT_NAME_D + "\n" + AIRPORT_LOCATION_D);
                    findFlightIntent.putExtra(CommonConstants.AIRPORT_LOCATION_A, AIRPORT_NAME_A + "\n" + AIRPORT_LOCATION_A);
                    findFlightIntent.putExtra(CommonConstants.ADULT, String.valueOf(totalAdult));
                    findFlightIntent.putExtra(CommonConstants.CHILD, String.valueOf(totalChild));
                    findFlightIntent.putExtra(CommonConstants.INFRANT, String.valueOf(totalBaby));
                    findFlightIntent.putExtra(CommonConstants.DATE, dateDeparture);
                    findFlightIntent.putExtra(CommonConstants.RET_DATE, dateArrival);
                    startActivity(findFlightIntent);
                }


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
                AIRPORT_CODE_D = data.getStringExtra(CommonConstants.AIRPORT_CODE_D);
                AIRPORT_NAME_D = data.getStringExtra(CommonConstants.AIRPORT_NAME_D);
                AIRPORT_LOCATION_D = data.getStringExtra(CommonConstants.AIRPORT_LOCATION_D);
                dariAirportCode.setText(AIRPORT_CODE_D);
                dariAirportName.setText(AIRPORT_NAME_D + "\n" + AIRPORT_LOCATION_D);
            }

            if (requestCode == MENUJU_AIRPORT) {
                AIRPORT_CODE_A = data.getStringExtra(CommonConstants.AIRPORT_CODE_A);
                AIRPORT_NAME_A = data.getStringExtra(CommonConstants.AIRPORT_NAME_A);
                AIRPORT_LOCATION_A = data.getStringExtra(CommonConstants.AIRPORT_LOCATION_A);
                menujuAirportCode.setText(AIRPORT_CODE_A);
                menujuAirportName.setText(AIRPORT_NAME_A + "\n" + AIRPORT_LOCATION_A);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
