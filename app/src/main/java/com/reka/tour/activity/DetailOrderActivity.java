package com.reka.tour.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.model.DeparturesOrder;
import com.reka.tour.model.Resource;
import com.reka.tour.utils.CommonConstants;
import com.reka.tour.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DetailOrderActivity extends AppCompatActivity {
    @Bind(R.id.tv_date)
    TextView tvDate;

    @Bind(R.id.iv_flight)
    ImageView ivFlight;
    @Bind(R.id.tv_name)
    TextView tvAirlinesName;
    @Bind(R.id.tv_flight_number)
    TextView tvFlightNumber;

    @Bind(R.id.iv_baggage)
    ImageView ivBaggage;
    @Bind(R.id.iv_food)
    ImageView ivFood;
    @Bind(R.id.iv_tax)
    ImageView ivTax;

    @Bind(R.id.tv_rute)
    TextView tvRute;
    @Bind(R.id.tv_duration)
    TextView tvDuration;

    @Bind(R.id.tv_adult)
    TextView tvAdult;
    @Bind(R.id.tv_adult_price)
    TextView tvAdultPrice;

    @Bind(R.id.layout_child)
    RelativeLayout layoutChild;
    @Bind(R.id.tv_child)
    TextView tvChild;
    @Bind(R.id.tv_child_price)
    TextView tvChildPrice;

    @Bind(R.id.layout_baby)
    RelativeLayout layoutInfrant;
    @Bind(R.id.tv_baby)
    TextView tvInfrant;
    @Bind(R.id.tv_baby_price)
    TextView tvInfrantPrice;

    @Bind(R.id.tv_total)
    TextView tvTotal;

    @Bind(R.id.layout_detail_order)
    ScrollView layoutDetailOrder;


    private Bundle bundle;
    private String flightID, dateValue, hasFood, airportTax, baggage, needBaggage;
    private DeparturesOrder departures;
    private Resource resource;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("ind", "IDN"));
    private String responeString;

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

        getData();
        setCallBack();

    }


    private void getData() {
        layoutDetailOrder.setVisibility(View.GONE);
        String url = CommonConstants.BASE_URL + "flight_api/get_flight_data";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.FLIGHT_ID, flightID);
        requestParams.put(CommonConstants.DATE, dateValue);
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
                responeString = response.toString();

                setResponeString(response.toString());

                try {
                    Gson gson = new Gson();

                    JSONObject departuresObject = response.getJSONObject(CommonConstants.DEPARTURES);
                    departures = gson.fromJson(departuresObject.toString(), DeparturesOrder.class);

                    setValue();
                    layoutDetailOrder.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(DetailOrderActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(DetailOrderActivity.this, errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setCallBack() {
        findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDeparture = new Intent(DetailOrderActivity.this, InfoPassangerActivity.class);
                intentDeparture.putExtra(CommonConstants.RESPONE, getResponeString());
                startActivity(intentDeparture);
            }
        });
    }

    private void setValue() {

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

    public String getResponeString() {
        return responeString;
    }

    public void setResponeString(String responeString) {
        this.responeString = responeString;
    }

}
