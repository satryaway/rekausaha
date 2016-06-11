package com.jixstreet.rekatoursandtravel.flight.activity;

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
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.jixstreet.rekatoursandtravel.flight.adapter.FlightAdapter;
import com.jixstreet.rekatoursandtravel.flight.adapter.ScheduleAdapter;
import com.jixstreet.rekatoursandtravel.flight.model.Departures;
import com.jixstreet.rekatoursandtravel.flight.model.NearbyGoDate;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    @Bind(R.id.tv_noflight)
    TextView tvNoflight;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<NearbyGoDate> nearbyGoDateArrayList = new ArrayList<>();
    private ArrayList<Departures> depAirportArrayList = new ArrayList<>();
    private FlightAdapter flightAdapter;
    private Bundle bundle;
    private String dateValue, retDateValue;
    ;
    private boolean isReturn;
    private String jsonObjectResponse;
    private ArrayList<Departures> depAirportFilterArrayList = new ArrayList<>();
    private String departure_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bundle = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.back);
        isReturn = bundle.getBoolean(CommonConstants.IS_RETURN);

        if (!isReturn) {
            toolbar.setTitle(getString(R.string.pergi));
        } else {
            toolbar.setTitle(bundle.getBoolean(CommonConstants.IS_IN_RETURN) ? getString(R.string.pulang) : getString(R.string.pergi));
        }

        setSupportActionBar(toolbar);

        dariAirportName.setSelected(true);
        menujuAirportName.setSelected(true);

        setValue();
        if (!isReturn) {
            getData();
        } else {
            if (bundle.getBoolean(CommonConstants.IS_IN_RETURN)) {
                collectData(bundle.getString(CommonConstants.JSON));
            } else {
                getData();
            }
        }
        setCallback();

    }

    private void setValue() {
        if (!isReturn) {
            dariAirportCode.setText(bundle.getString(CommonConstants.AIRPORT_CODE_D));
            dariAirportName.setText(bundle.getString(CommonConstants.AIRPORT_LOCATION_D));
            menujuAirportCode.setText(bundle.getString(CommonConstants.AIRPORT_CODE_A));
            menujuAirportName.setText(bundle.getString(CommonConstants.AIRPORT_LOCATION_A));

            isReturn = bundle.getBoolean(CommonConstants.IS_RETURN, false);
            dateValue = bundle.getString(CommonConstants.DATE);
        } else {
            if (bundle.getBoolean(CommonConstants.IS_IN_RETURN)) {
                dariAirportCode.setText(bundle.getString(CommonConstants.AIRPORT_CODE_A));
                dariAirportName.setText(bundle.getString(CommonConstants.AIRPORT_LOCATION_A));
                menujuAirportCode.setText(bundle.getString(CommonConstants.AIRPORT_CODE_D));
                menujuAirportName.setText(bundle.getString(CommonConstants.AIRPORT_LOCATION_D));

                isReturn = bundle.getBoolean(CommonConstants.IS_RETURN, false);
                dateValue = bundle.getString(CommonConstants.DATE);
                retDateValue = bundle.getString(CommonConstants.RET_DATE);
                departure_time = bundle.getString(CommonConstants.DEPARTURE_TIME);
            } else {
                dariAirportCode.setText(bundle.getString(CommonConstants.AIRPORT_CODE_D));
                dariAirportName.setText(bundle.getString(CommonConstants.AIRPORT_LOCATION_D));
                menujuAirportCode.setText(bundle.getString(CommonConstants.AIRPORT_CODE_A));
                menujuAirportName.setText(bundle.getString(CommonConstants.AIRPORT_LOCATION_A));

                isReturn = bundle.getBoolean(CommonConstants.IS_RETURN, false);
                dateValue = bundle.getString(CommonConstants.DATE);
                retDateValue = bundle.getString(CommonConstants.RET_DATE);
            }
        }
    }

    private void setCallback() {
        listSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                if (!bundle.getBoolean(CommonConstants.IS_IN_RETURN)) {
                    dateValue = nearbyGoDateArrayList.get(position).date;
                } else {
                    retDateValue = nearbyGoDateArrayList.get(position).date;
                }
//                                retDate = nearbyGoDateArrayList.get(position).date;
                getData();

            }
        });

        listFlight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isReturn) {
                    Intent intentDeparture = new Intent(DepartureActivity.this, OrderFlightActivity.class);
                    intentDeparture.putExtra(CommonConstants.FLIGHT_ID, depAirportArrayList.get(position).flightId);
                    intentDeparture.putExtra(CommonConstants.HAS_FOOD, depAirportArrayList.get(position).hasFood);
                    intentDeparture.putExtra(CommonConstants.AIRPORT_TAX, depAirportArrayList.get(position).airportTax);
                    intentDeparture.putExtra(CommonConstants.BAGGAGE, depAirportArrayList.get(position).checkInBaggage);
                    intentDeparture.putExtra(CommonConstants.NEED_BAGGAGE, depAirportArrayList.get(position).needBaggage);
                    intentDeparture.putExtra(CommonConstants.DATE, dateValue);
                    startActivity(intentDeparture);
                } else {
                    try {
                        if (bundle.getBoolean(CommonConstants.IS_IN_RETURN)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
                            String departureTimeReturn = depAirportArrayList.get(position).simpleDepartureTime;
                            String arrivalTimeGo = bundle.getString(CommonConstants.ARRIVAL_TIME);
                            Date parsedDepartureTime = sdf.parse(departureTimeReturn);
                            Date parsedArrivalTimeGo = sdf.parse(arrivalTimeGo);
                            Date parsedGoTime = sdf.parse(departure_time);
                            if ((parsedArrivalTimeGo.getTime() > parsedDepartureTime.getTime()) && (dateValue.equals(retDateValue))) {
                                Toast.makeText(DepartureActivity.this, "Waktu pulang harus lebih besar dari waktu pergi", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intentDeparture = new Intent(DepartureActivity.this, OrderFlightActivity.class);
                                intentDeparture.putExtra(CommonConstants.FLIGHT_ID, bundle.getString(CommonConstants.FLIGHT_ID));
                                intentDeparture.putExtra(CommonConstants.RET_FLIGHT_ID, depAirportArrayList.get(position).flightId);
                                intentDeparture.putExtra(CommonConstants.HAS_FOOD, depAirportArrayList.get(position).hasFood);
                                intentDeparture.putExtra(CommonConstants.AIRPORT_TAX, depAirportArrayList.get(position).airportTax);
                                intentDeparture.putExtra(CommonConstants.BAGGAGE, depAirportArrayList.get(position).checkInBaggage);
                                intentDeparture.putExtra(CommonConstants.NEED_BAGGAGE, depAirportArrayList.get(position).needBaggage);
                                intentDeparture.putExtra(CommonConstants.RET_DATE, retDateValue);
                                intentDeparture.putExtra(CommonConstants.IS_RETURN, true);
                                intentDeparture.putExtra(CommonConstants.DATE, dateValue);
                                startActivity(intentDeparture);
                            }
                        } else {
                            Intent findFlightIntent = new Intent(DepartureActivity.this, DepartureActivity.class);
                            findFlightIntent.putExtra(CommonConstants.FLIGHT_ID, depAirportArrayList.get(position).flightId);
                            findFlightIntent.putExtra(CommonConstants.AIRPORT_CODE_D, bundle.getString(CommonConstants.AIRPORT_CODE_A));
                            findFlightIntent.putExtra(CommonConstants.AIRPORT_CODE_A, bundle.getString(CommonConstants.AIRPORT_CODE_D));
                            findFlightIntent.putExtra(CommonConstants.AIRPORT_LOCATION_D, bundle.getString(CommonConstants.AIRPORT_LOCATION_A));
                            findFlightIntent.putExtra(CommonConstants.AIRPORT_LOCATION_A, bundle.getString(CommonConstants.AIRPORT_LOCATION_D));
                            findFlightIntent.putExtra(CommonConstants.ADULT, bundle.getString(CommonConstants.ADULT));
                            findFlightIntent.putExtra(CommonConstants.CHILD, bundle.getString(CommonConstants.CHILD));
                            findFlightIntent.putExtra(CommonConstants.ARRIVAL_TIME, depAirportArrayList.get(position).simpleArrivalTime);
                            findFlightIntent.putExtra(CommonConstants.INFRANT, bundle.getString(CommonConstants.INFRANT));
                            findFlightIntent.putExtra(CommonConstants.DATE, dateValue);
                            findFlightIntent.putExtra(CommonConstants.RET_DATE, bundle.getString(CommonConstants.RET_DATE));
                            findFlightIntent.putExtra(CommonConstants.IS_IN_RETURN, true);
                            findFlightIntent.putExtra(CommonConstants.IS_RETURN, true);
                            findFlightIntent.putExtra(CommonConstants.DEPARTURE_TIME, depAirportArrayList.get(position).simpleDepartureTime);
                            findFlightIntent.putExtra(CommonConstants.JSON, jsonObjectResponse);
                            startActivity(findFlightIntent);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
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

                depAirportArrayList = (ArrayList<Departures>) data.getSerializableExtra(CommonConstants.DEPARTURES);
                String FILTER_MASKAPAI = data.getStringExtra(CommonConstants.FILTER_MASKAPAI);
                String FILTER_JAM = data.getStringExtra(CommonConstants.FILTER_JAM);
                String FILTER_OPSI = data.getStringExtra(CommonConstants.FILTER_OPSI);
                String FILTER_HARGA = data.getStringExtra(CommonConstants.FILTER_HARGA);

                Log.e("FILTER_MASKAPAI", FILTER_MASKAPAI.toLowerCase(Locale.getDefault()) + " " +
                        FILTER_JAM.toLowerCase(Locale.getDefault()) + " " +
                        FILTER_OPSI.toLowerCase(Locale.getDefault()) + " " +
                        FILTER_HARGA.toLowerCase(Locale.getDefault())
                );

                flightAdapter = new FlightAdapter(DepartureActivity.this, depAirportArrayList);
                listFlight.setAdapter(flightAdapter);

                flightAdapter.filter(FILTER_JAM, FILTER_HARGA, FILTER_OPSI, FILTER_MASKAPAI.toLowerCase(Locale.getDefault()));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void noFlight(int visibility) {
        tvNoflight.setVisibility(visibility);
    }

    public void listFlight(int visibility) {
        listFlight.setVisibility(visibility);
    }

    private void getData() {
        noFlight(View.GONE);

        String url = CommonConstants.BASE_URL + "search/flight?V=3";
//        String url = "http://api-sandbox.tiket.com/search/flight?d=CGK&a=DPS&date=" + dateValue +
//                "&ret_date=2014-05-30&adult=" + bundle.getString(CommonConstants.ADULT) +
//                "&child=" + bundle.getString(CommonConstants.CHILD) +
//                "&infant=" + bundle.getString(CommonConstants.INFRANT) +
//                "&token=19d0ceaca45f9ee27e3c51df52786f1d904280f9&v=3&output=json";

        Log.e("dateValue", dateValue + " " + retDateValue);

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.D, bundle.getString(CommonConstants.AIRPORT_CODE_D));
        requestParams.put(CommonConstants.A, bundle.getString(CommonConstants.AIRPORT_CODE_A));
        requestParams.put(CommonConstants.ADULT, bundle.getString(CommonConstants.ADULT));
        requestParams.put(CommonConstants.CHILD, bundle.getString(CommonConstants.CHILD));
        requestParams.put(CommonConstants.INFRANT, bundle.getString(CommonConstants.INFRANT));
        requestParams.put(CommonConstants.DATE, dateValue);
        if (isReturn) requestParams.put(CommonConstants.RET_DATE, retDateValue);
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
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
                jsonObjectResponse = response.toString();
                collectData(jsonObjectResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(DepartureActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON Departure", errorResponse + "");
                ErrorException.getError(DepartureActivity.this, errorResponse);
            }
        });
    }

    private void collectData(String responseString) {
        try {
            JSONObject response = new JSONObject(responseString);
            Log.e("JSON FLIGHT", response.toString() + "");
            listFlight(View.VISIBLE);

            Gson gson = new Gson();

            if (nearbyGoDateArrayList.size() > 0) {
                listSchedule.setAdapter(null);
                scheduleAdapter.notifyDataSetChanged();
                nearbyGoDateArrayList.clear();
            }
            JSONArray nearbyDateArrayList = null;

            //Check if it's return
            if (isReturn) {
                if (bundle.getBoolean(CommonConstants.IS_IN_RETURN))
                    nearbyDateArrayList = response.getJSONObject(CommonConstants.NEARBY_RET_DATE).getJSONArray(CommonConstants.NEARBY);
                else
                    nearbyDateArrayList = response.getJSONObject(CommonConstants.NEARBY_GO_DATE).getJSONArray(CommonConstants.NEARBY);
            } else {
                nearbyDateArrayList = response.getJSONObject(CommonConstants.NEARBY_GO_DATE).getJSONArray(CommonConstants.NEARBY);
            }

            for (int i = 0; i < nearbyDateArrayList.length(); i++) {
                nearbyGoDateArrayList.add(gson.fromJson(nearbyDateArrayList.getJSONObject(i).toString(), NearbyGoDate.class));
            }

            scheduleAdapter = new ScheduleAdapter(DepartureActivity.this, nearbyGoDateArrayList, !bundle.getBoolean(CommonConstants.IS_IN_RETURN) ? dateValue : retDateValue);
            listSchedule.setAdapter(scheduleAdapter);

            for (int i = 0; i < nearbyGoDateArrayList.size(); i++) {
                if (nearbyGoDateArrayList.get(i).date.equals(!bundle.getBoolean(CommonConstants.IS_IN_RETURN) ? dateValue : retDateValue)) {
                    if (i - 1 > 0) {
                        listSchedule.setSelection(i - 1);
                    } else {
                        listSchedule.setSelection(i);
                    }
                }
                if (nearbyGoDateArrayList.get(i).price != null) {
                    if (nearbyGoDateArrayList.get(i).price.equals("0.00")) {
                        noFlight(View.VISIBLE);
                        listFlight(View.GONE);
                    }
                }
            }
            JSONArray airportArrayList = null;
            depAirportArrayList = new ArrayList<>();

            if (isReturn) {
                if (bundle.getBoolean(CommonConstants.IS_IN_RETURN))
                    airportArrayList = response.getJSONObject(CommonConstants.RETURNS).getJSONArray(CommonConstants.RESULT);
                else
                    airportArrayList = response.getJSONObject(CommonConstants.DEPARTURES).getJSONArray(CommonConstants.RESULT);
            } else {
                airportArrayList = response.getJSONObject(CommonConstants.DEPARTURES).getJSONArray(CommonConstants.RESULT);
            }

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

            depAirportFilterArrayList = depAirportArrayList;

            flightAdapter = new FlightAdapter(DepartureActivity.this, depAirportArrayList);
            listFlight.setAdapter(flightAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
