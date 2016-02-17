package com.reka.tour.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.adapter.StepsAdapter;
import com.reka.tour.flight.activity.DetailOrderActivity;
import com.reka.tour.flight.activity.ListOrderActivity;
import com.reka.tour.flight.model.DeparturesOrder;
import com.reka.tour.model.MyOrder;
import com.reka.tour.model.Steps;
import com.reka.tour.utils.CommonConstants;
import com.reka.tour.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class PaymentActivity extends AppCompatActivity {
    @Bind(R.id.tv_orderid)
    TextView tvOrderId;

    @Bind(R.id.tv_sisa_waktu)
    TextView tvSisaWaktu;    @Bind(R.id.tv_upto)
    TextView tvUpto;
    @Bind(R.id.layout_time)
    RelativeLayout layoutTime;
    @Bind(R.id.list_step_payment)
    ListView listStepPayment;

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

    private DeparturesOrder departures;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("ind", "IDN"));
    private String hasFood, airportTax, baggage, needBaggage, orderId;
    private ArrayList<MyOrder> myOrders;
    private ArrayList<Steps> stepses = new ArrayList<>();
    private StepsAdapter stepsAdapter;
    private String url;
    private boolean finish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        url = getIntent().getExtras().getString(CommonConstants.LINK);
        if (!url.equals("#")) {
            getData(url);
        }

        setValue();
    }

    @OnClick(R.id.tv_next)
    void nextOnClick() {
        if (finish) {
            finish();
        } else {
            getFinishPayment(url);
        }
    }

    private void setValue() {
        departures = DetailOrderActivity.getDepartures();
        hasFood = DetailOrderActivity.getHasFood();
        airportTax = DetailOrderActivity.getAirportTax();
        needBaggage = DetailOrderActivity.getNeedBaggage();
        baggage = DetailOrderActivity.getBaggage();
        myOrders = ListOrderActivity.getMyOrders();


        tvUpto.setText("HINGGA " + myOrders.get(0).orderExpireDatetime);

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

    private void getData(String url) {
        RequestParams requestParams = new RequestParams();
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
                Log.e("JSON METHOD PAYMENT", response.toString() + "");
                try {
                    orderId = response.getJSONObject(CommonConstants.RESULT).getString(CommonConstants.ORDER_ID);
                    tvOrderId.setText(orderId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PaymentActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(PaymentActivity.this, errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void getFinishPayment(String url) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, "19d0ceaca45f9ee27e3c51df52786f1d904280f9");
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        requestParams.put(CommonConstants.BTN_BOOKING, "1");
        requestParams.put(CommonConstants.CURRENCY, "IDR");

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
                Log.e("JSON METHOD PAYMENT", response.toString() + "");
                try {
                    Gson gson = new Gson();

                    JSONArray stepsArray = response.getJSONArray(CommonConstants.STEPS);
                    for (int i = 0; i < stepsArray.length(); i++) {
                        stepses.add(gson.fromJson(stepsArray.getJSONObject(i).toString(), Steps.class));
                    }

                    stepsAdapter = new StepsAdapter(PaymentActivity.this, stepses);
                    listStepPayment.setAdapter(stepsAdapter);
                    Util.setListview(listStepPayment);

                    ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(null);
                    ((TextView)findViewById(R.id.tv_next)).setText("Selesai");
                    tvSisaWaktu.setVisibility(View.GONE);
                    tvUpto.setVisibility(View.GONE);
                    layoutTime.setVisibility(View.GONE);
                    listStepPayment.setVisibility(View.VISIBLE);
                    finish = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PaymentActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(PaymentActivity.this, errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
}
