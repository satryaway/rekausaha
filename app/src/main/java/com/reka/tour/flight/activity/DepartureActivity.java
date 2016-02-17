package com.reka.tour.flight.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.flight.adapter.FlightAdapter;
import com.reka.tour.flight.adapter.ScheduleAdapter;
import com.reka.tour.flight.model.Departures;
import com.reka.tour.flight.model.NearbyGoDate;
import com.reka.tour.utils.CommonConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DepartureActivity extends AppCompatActivity {
    private final int FILTER_FLIGHT = 100;
    @Bind(R.id.list_schedule)
    TwoWayView listSchedule;
    @Bind(R.id.list_flight)
    ListView listFlight;
    @Bind(R.id.dari_airport_code)
    TextView dariAirportCode;
    @Bind(R.id.dari_airport_name)
    TextView dariAirportName;
    @Bind(R.id.menuju_airport_code)
    TextView menujuAirportCode;
    @Bind(R.id.menuju_airport_name)
    TextView menujuAirportName;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<NearbyGoDate> nearbyGoDateArrayList = new ArrayList<>();
    private ArrayList<Departures> depAirportArrayList = new ArrayList<>();
    private ArrayList<Departures> depAirportFilterArrayList = new ArrayList<>();
    private ArrayList<Departures> departures;
    private FlightAdapter flightAdapter;
    private Bundle bundle;
    private String dateValue, retDateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setValue();
        getData();
        setCallback();

    }

    private void setValue() {
        bundle = getIntent().getExtras();
        dariAirportCode.setText(bundle.getString(CommonConstants.AIRPORT_CODE_D));
        dariAirportName.setText(bundle.getString(CommonConstants.AIRPORT_LOCATION_D));
        menujuAirportCode.setText(bundle.getString(CommonConstants.AIRPORT_CODE_A));
        menujuAirportName.setText(bundle.getString(CommonConstants.AIRPORT_LOCATION_A));

        dateValue = "2014-05-25";
//        dateValue = bundle.getString(CommonConstants.DATE);
        retDateValue = bundle.getString(CommonConstants.RET_DATE);
    }

    private void setCallback() {
        listSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                dateValue = nearbyGoDateArrayList.get(position).date;
                //                retDate = nearbyGoDateArrayList.get(position).date;
                getData();

            }
        });

        listFlight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentDeparture = new Intent(DepartureActivity.this, DetailOrderActivity.class);

                if(departures!=null){
                    intentDeparture.putExtra(CommonConstants.FLIGHT_ID, departures.get(position).flightId);
                    intentDeparture.putExtra(CommonConstants.HAS_FOOD, departures.get(position).hasFood);
                    intentDeparture.putExtra(CommonConstants.AIRPORT_TAX, departures.get(position).airportTax);
                    intentDeparture.putExtra(CommonConstants.BAGGAGE, departures.get(position).checkInBaggage);
                    intentDeparture.putExtra(CommonConstants.NEED_BAGGAGE, departures.get(position).needBaggage);
                }else {
                    intentDeparture.putExtra(CommonConstants.FLIGHT_ID, depAirportArrayList.get(position).flightId);
                    intentDeparture.putExtra(CommonConstants.HAS_FOOD, depAirportArrayList.get(position).hasFood);
                    intentDeparture.putExtra(CommonConstants.AIRPORT_TAX, depAirportArrayList.get(position).airportTax);
                    intentDeparture.putExtra(CommonConstants.BAGGAGE, depAirportArrayList.get(position).checkInBaggage);
                    intentDeparture.putExtra(CommonConstants.NEED_BAGGAGE, depAirportArrayList.get(position).needBaggage);
                }

                intentDeparture.putExtra(CommonConstants.DATE, dateValue);
                startActivity(intentDeparture);
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;

            case R.id.action_filter:
                Intent intentDeparture = new Intent(DepartureActivity.this, FilterActivity.class);
                intentDeparture.putExtra(CommonConstants.FLIGHT, depAirportFilterArrayList);
//                startActivity(intentDeparture);
                startActivityForResult(intentDeparture, FILTER_FLIGHT);
//                flightAdapter.filter("3:9,","murah","transit","citi,garuda");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == FILTER_FLIGHT) {
                flightAdapter.notifyDataSetChanged();

                departures = (ArrayList<Departures>) data.getSerializableExtra(CommonConstants.DEPARTURES);
                String FILTER_MASKAPAI = data.getStringExtra(CommonConstants.FILTER_MASKAPAI);
                String FILTER_JAM = data.getStringExtra(CommonConstants.FILTER_JAM);
                String FILTER_OPSI = data.getStringExtra(CommonConstants.FILTER_OPSI);
                String FILTER_HARGA = data.getStringExtra(CommonConstants.FILTER_HARGA);

                Log.e("FILTER_MASKAPAI", FILTER_MASKAPAI.toLowerCase(Locale.getDefault()) + " " +
                                FILTER_JAM.toLowerCase(Locale.getDefault()) + " " +
                                FILTER_OPSI.toLowerCase(Locale.getDefault()) + " " +
                                FILTER_HARGA.toLowerCase(Locale.getDefault())
                );

                flightAdapter = new FlightAdapter(DepartureActivity.this, departures);
                listFlight.setAdapter(flightAdapter);

                flightAdapter.filter(FILTER_JAM, FILTER_HARGA, FILTER_OPSI, FILTER_MASKAPAI.toLowerCase(Locale.getDefault()));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getData() {
//        String url = CommonConstants.BASE_URL + "search/flight?V=3";
        String url = "http://api-sandbox.tiket.com/search/flight?d=CGK&a=DPS&date=" + dateValue +
                "&ret_date=2014-05-30&adult=" + bundle.getString(CommonConstants.ADULT) +
                "&child=" + bundle.getString(CommonConstants.CHILD) +
                "&infant=" + bundle.getString(CommonConstants.INFRANT) +
                "&token=19d0ceaca45f9ee27e3c51df52786f1d904280f9&v=3&output=json";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.D, bundle.getString(CommonConstants.AIRPORT_CODE_D));
        requestParams.put(CommonConstants.A, bundle.getString(CommonConstants.AIRPORT_CODE_A));
        requestParams.put(CommonConstants.ADULT, bundle.getString(CommonConstants.ADULT));
        requestParams.put(CommonConstants.CHILD, bundle.getString(CommonConstants.CHILD));
        requestParams.put(CommonConstants.INFRANT, bundle.getString(CommonConstants.INFRANT));
        requestParams.put(CommonConstants.DATE, dateValue);
        requestParams.put(CommonConstants.RET_DATE, bundle.getString(CommonConstants.RET_DATE));
        requestParams.put(CommonConstants.TOKEN, "19d0ceaca45f9ee27e3c51df52786f1d904280f9");
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                progressDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("JSON FLIGHT", response.toString() + "");
                try {
                    Gson gson = new Gson();

                    JSONArray nearbyDateArrayList = response.getJSONObject(CommonConstants.NEARBY_GO_DATE).getJSONArray(CommonConstants.NEARBY);
                    for (int i = 0; i < nearbyDateArrayList.length(); i++) {
                        nearbyGoDateArrayList.add(gson.fromJson(nearbyDateArrayList.getJSONObject(i).toString(), NearbyGoDate.class));
                    }

                    scheduleAdapter = new ScheduleAdapter(DepartureActivity.this, nearbyGoDateArrayList);
                    listSchedule.setAdapter(scheduleAdapter);

                    JSONArray airportArrayList = response.getJSONObject(CommonConstants.DEPARTURES).getJSONArray(CommonConstants.RESULT);
                    for (int i = 0; i < airportArrayList.length(); i++) {
                        depAirportArrayList.add(gson.fromJson(airportArrayList.getJSONObject(i).toString(), Departures.class));
                    }

                    // Sorting
                    Collections.sort(depAirportArrayList, new Comparator<Departures>() {
                        @Override
                        public int compare(Departures fruit2, Departures fruit1) {
                            return Double.compare(Double.parseDouble(fruit2.priceAdult), Double.parseDouble(fruit1.priceAdult));
                        }
                    });

                    depAirportFilterArrayList.addAll(depAirportArrayList);

                    flightAdapter = new FlightAdapter(DepartureActivity.this, depAirportArrayList);
                    listFlight.setAdapter(flightAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(DepartureActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(DepartureActivity.this, errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}