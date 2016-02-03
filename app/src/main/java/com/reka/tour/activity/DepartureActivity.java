package com.reka.tour.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.reka.tour.adapter.FlightAdapter;
import com.reka.tour.adapter.ScheduleAdapter;
import com.reka.tour.model.Departures;
import com.reka.tour.model.Flight;
import com.reka.tour.model.NearbyGoDate;
import com.reka.tour.utils.CommonConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DepartureActivity extends AppCompatActivity {
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

    private FlightAdapter flightAdapter;
    private ArrayList<Flight> flightArrayList = new ArrayList<>();
    private Bundle bundle;

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
    }

    private void setCallback() {
        listFlight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(DepartureActivity.this, CheckoutActivity.class));
            }
        });
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

    private void getData() {
        String url = CommonConstants.BASE_URL + "search/flight?V=3";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.D, bundle.getString(CommonConstants.AIRPORT_CODE_D));
        requestParams.put(CommonConstants.A, bundle.getString(CommonConstants.AIRPORT_CODE_A));
        requestParams.put(CommonConstants.ADULT, bundle.getString(CommonConstants.ADULT));
        requestParams.put(CommonConstants.CHILD, bundle.getString(CommonConstants.CHILD));
        requestParams.put(CommonConstants.INFRANT, bundle.getString(CommonConstants.INFRANT));
        requestParams.put(CommonConstants.DATE, bundle.getString(CommonConstants.DATE));
        requestParams.put(CommonConstants.RET_DATE, bundle.getString(CommonConstants.RET_DATE));
        requestParams.put(CommonConstants.TOKEN, "19d0ceaca45f9ee27e3c51df52786f1d904280f9");
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
        client.get(url, requestParams, new JsonHttpResponseHandler() {
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
                    JSONArray nearbyDateArrayList = response.getJSONObject(CommonConstants.NEARBY_GO_DATE).getJSONArray(CommonConstants.NEARBY);
                    for (int i = 0; i < nearbyDateArrayList.length(); i++) {
                        Gson gson = new Gson();
                        nearbyGoDateArrayList.add(gson.fromJson(nearbyDateArrayList.getJSONObject(i).toString(), NearbyGoDate.class));
                    }

                    scheduleAdapter = new ScheduleAdapter(DepartureActivity.this, nearbyGoDateArrayList);
                    listSchedule.setAdapter(scheduleAdapter);

                    JSONArray airportArrayList = response.getJSONObject(CommonConstants.DEPARTURES).getJSONArray(CommonConstants.RESULT);
                    for (int i = 0; i < airportArrayList.length(); i++) {
                        Gson gson = new Gson();
                        depAirportArrayList.add(gson.fromJson(airportArrayList.getJSONObject(i).toString(), Departures.class));
                    }

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
