package com.jixstreet.rekatoursandtravel.flight.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.jixstreet.rekatoursandtravel.flight.model.DeparturesOrder;
import com.jixstreet.rekatoursandtravel.flight.model.Resource;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;
import com.jixstreet.rekatoursandtravel.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class OrderFlightActivity extends AppCompatActivity {
    private static String flightID, dateValue, hasFood, airportTax, baggage, needBaggage;
    private static DeparturesOrder departuresReturn;
    @Bind(R.id.detail_order_wrapper)
    LinearLayout detailOrderWrapper;

    @Bind(R.id.layout_detail_order)
    ScrollView layoutDetailOrder;

    private Bundle bundle;
    private DeparturesOrder departures;
    private Resource resource;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("ind", "IDN"));
    private String responeString;
    private String retDateValue;
    private boolean isReturn;
    private String retFlightID;

    public static DeparturesOrder getDepartures() {
        return departuresReturn;
    }

    public static String getHasFood() {
        return hasFood;
    }

    public static String getAirportTax() {
        return airportTax;
    }

    public static String getNeedBaggage() {
        return needBaggage;
    }

    public static String getBaggage() {
        return baggage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);


        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        bundle = getIntent().getExtras();
        flightID = bundle.getString(CommonConstants.FLIGHT_ID);
        dateValue = bundle.getString(CommonConstants.DATE);
        hasFood = bundle.getString(CommonConstants.HAS_FOOD);
        airportTax = bundle.getString(CommonConstants.AIRPORT_TAX);
        needBaggage = bundle.getString(CommonConstants.NEED_BAGGAGE);
        baggage = bundle.getString(CommonConstants.BAGGAGE);
        isReturn = bundle.getBoolean(CommonConstants.IS_RETURN, false);
        if (isReturn) {
            retDateValue = bundle.getString(CommonConstants.RET_DATE);
            retFlightID = bundle.getString(CommonConstants.RET_FLIGHT_ID);
        }

        getData();
        setCallBack();

    }

    private void getData() {
        layoutDetailOrder.setVisibility(View.GONE);
        String url = CommonConstants.BASE_URL + "flight_api/get_flight_data";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.DATE, dateValue);
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);
        requestParams.put(CommonConstants.FLIGHT_ID, flightID);
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put("V", "3");
        if (isReturn) {
            requestParams.put(CommonConstants.RET_FLIGHT_ID, retFlightID);
            requestParams.put(CommonConstants.RET_DATE, retDateValue);
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("user-agent", "twh:[22490422]:[Reka Tours dan Travel]");
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
                responeString = response.toString();

                setResponeString(response.toString());
                layoutDetailOrder.setVisibility(View.VISIBLE);

                try {
                    Gson gson = new Gson();

                    JSONObject departuresObject = response.getJSONObject(CommonConstants.DEPARTURES);
                    departures = gson.fromJson(departuresObject.toString(), DeparturesOrder.class);
                    departures.status = getString(R.string.pergi);

                    setPassenger(departures);

                    JSONObject returnObject = response.getJSONObject(CommonConstants.RETURNS);
                    if (returnObject != null) {
                        departuresReturn = gson.fromJson(returnObject.toString(), DeparturesOrder.class);
                        departuresReturn.status = getString(R.string.pulang);
                        setPassenger(departuresReturn);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(OrderFlightActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON OrderFlight", errorResponse + "");
                ErrorException.getError(OrderFlightActivity.this, errorResponse);
            }
        });
    }

    private void setCallBack() {
        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDeparture = new Intent(OrderFlightActivity.this, InfoPassangerActivity.class);
                intentDeparture.putExtra(CommonConstants.RESPONE, getResponseString());
                intentDeparture.putExtra(CommonConstants.FLIGHT_ID, flightID);
                intentDeparture.putExtra(CommonConstants.RET_FLIGHT_ID, retFlightID);
                intentDeparture.putExtra(CommonConstants.IS_RETURN, isReturn);
                startActivity(intentDeparture);
            }
        });
    }

    private void setPassenger(DeparturesOrder departures) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_order_detail, null);

        TextView status = (TextView) view.findViewById(R.id.pergi);
        View ivFood = view.findViewById(R.id.iv_food);
        View ivTax = view.findViewById(R.id.iv_tax);
        ImageView ivBaggage = (ImageView) view.findViewById(R.id.iv_baggage);
        ImageView ivFlight = (ImageView) view.findViewById(R.id.iv_flight);
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
        TextView tvAirlinesName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvFlightNumber = (TextView) view.findViewById(R.id.tv_flight_number);
        TextView tvRute = (TextView) view.findViewById(R.id.tv_rute);
        TextView tvDuration = (TextView) view.findViewById(R.id.tv_duration);
        TextView tvAdult = (TextView) view.findViewById(R.id.tv_adult);
        TextView tvAdultPrice = (TextView) view.findViewById(R.id.tv_adult_price);
        TextView tvChild = (TextView) view.findViewById(R.id.tv_child);
        TextView tvChildPrice = (TextView) view.findViewById(R.id.tv_child_price);
        TextView tvInfrant = (TextView) view.findViewById(R.id.tv_baby);
        TextView tvInfrantPrice = (TextView) view.findViewById(R.id.tv_baby_price);
        View layoutChild = view.findViewById(R.id.layout_child);
        View layoutInfrant = view.findViewById(R.id.layout_baby);
        TextView tvTotal = (TextView) view.findViewById(R.id.tv_total);

        status.setText(departures.status);

        if (hasFood.equals("0")) {
            ivFood.setVisibility(View.GONE);
        }
        if (!Boolean.valueOf(airportTax)) {
            ivTax.setVisibility(View.GONE);
        }
        if (needBaggage.equals("0")) {
            ivBaggage.setVisibility(View.GONE);
        } else {
            if (baggage.equals("15")) {
                ivBaggage.setImageResource(R.drawable.ic_baggage_15);
            } else if (baggage.equals("20")) {
                ivBaggage.setImageResource(R.drawable.ic_baggage_20);
            }
        }

        try {
            tvDate.setText(dateFormatter.format(dateFormat.parse(departures.flightDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.with(this).load(departures.image).into(ivFlight);
        tvAirlinesName.setText(departures.airlinesName);
        tvFlightNumber.setText(departures.flightNumber);

        tvRute.setText(departures.fullVia);
        tvDuration.setText("Duration : " + departures.duration + ", " + departures.stop);

        tvAdult.setText(departures.countAdult + " Dewasa");
        tvAdultPrice.setText(Util.toRupiahFormat(departures.priceAdult));

        if (departures.countChild.equals("0")) {
            layoutChild.setVisibility(View.GONE);
        } else {
            tvChild.setText(departures.countChild + " Anak");
            tvChildPrice.setText(Util.toRupiahFormat(departures.priceChild));

        }

        if (departures.countInfant.equals("0")) {
            layoutInfrant.setVisibility(View.GONE);
        } else {
            tvInfrant.setText(departures.countInfant + " Bayi");
            tvInfrantPrice.setText(Util.toRupiahFormat(departures.priceInfant));

        }

        tvTotal.setText(Util.toRupiahFormat(departures.priceValue));

        detailOrderWrapper.addView(view);
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

    public String getResponseString() {
        return responeString;
    }

    public void setResponeString(String responeString) {
        this.responeString = responeString;
    }
}
