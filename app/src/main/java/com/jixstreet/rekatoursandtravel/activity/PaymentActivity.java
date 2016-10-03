package com.jixstreet.rekatoursandtravel.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.jixstreet.rekatoursandtravel.adapter.StepsAdapter;
import com.jixstreet.rekatoursandtravel.model.MyOrder;
import com.jixstreet.rekatoursandtravel.model.Policy;
import com.jixstreet.rekatoursandtravel.model.Steps;
import com.jixstreet.rekatoursandtravel.utils.APIAgent;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.DateTimeUtils;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;
import com.jixstreet.rekatoursandtravel.utils.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class PaymentActivity extends AppCompatActivity {
    @Bind(R.id.tv_orderid)
    TextView tvOrderId;

    @Bind(R.id.tv_sisa_waktu)
    TextView tvSisaWaktu;

    @Bind(R.id.tv_upto)
    TextView tvUpto;

    @Bind(R.id.tv_expire_time)
    TextView tvExpiredTime;

    @Bind(R.id.layout_time)
    RelativeLayout layoutTime;

    @Bind(R.id.list_step_payment)
    ListView listStepPayment;

    @Bind(R.id.discount_tv)
    TextView discountTV;

    @Bind(R.id.ev_klikbca)
    EditText evKlikbca;
    @Bind(R.id.tv_step_payment)
    TextView tvStepPayment;

    @Bind(R.id.layout_flight)
    LinearLayout layoutFlight;

    @Bind(R.id.layout_hotel)
    LinearLayout layoutHotel;

    @Bind(R.id.layout_klikbca)
    CardView layoutKlikbca;

    @Bind(R.id.layout_mandiriclickpay)
    CardView layoutMandiriKlikPay;

    @Bind(R.id.tv_total)
    TextView tvTotal;

    @Bind(R.id.order_wrapper)
    LinearLayout orderWrapper;

    @Bind(R.id.step_wrapper)
    LinearLayout stepWrapper;

    @Bind(R.id.mandiri_card_number_et)
    EditText mandiriCardNumberEt;

    @Bind(R.id.token_response_et)
    EditText tokenResponseEt;

    private String orderId;
    private ArrayList<Steps> stepses = new ArrayList<>();
    private StepsAdapter stepsAdapter;
    private String url, type;
    private ArrayList<MyOrder> orders = new ArrayList<>();
    private LayoutInflater inflater;
    private HashMap<String, Policy> policiesMap = new HashMap<>();
    private boolean isAfter = false;
    private boolean isKlikBCA = false;
    private String text;
    private Calendar calendar;
    private long elapsedDays;
    private long elapsedHours;
    private long elapsedMinutes;
    private long elapsedSeconds;
    private String initialTime;
    private boolean isMandiri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        url = getIntent().getExtras().getString(CommonConstants.LINK);
        type = getIntent().getExtras().getString(CommonConstants.TYPE);
        text = getIntent().getExtras().getString(CommonConstants.TEXT);

        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        ((Toolbar) findViewById(R.id.toolbar)).setTitle(text);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        orders = ListOrderActivity.getMyOrders();
        initialTime = orders.get(0).orderExpireDatetime;

        if (type.equals("klikbca")) {
            isKlikBCA = true;
            layoutTime.setVisibility(View.GONE);
            layoutKlikbca.setVisibility(View.VISIBLE);
        } else if (type.equals("mandiri_clickpay")) {
            isMandiri = true;
            layoutTime.setVisibility(View.GONE);
            layoutMandiriKlikPay.setVisibility(View.VISIBLE);
        }

        getData(url);

        /*if (!url.equals("#")) {
            getData(url);
        }*/
        /*Log.e("URL", url);

        String text = getIntent().getExtras().getString(CommonConstants.TEXT);
        ((Toolbar) findViewById(R.id.toolbar)).setTitle(text);*/



        /*if (type.equals(CommonConstants.KLIKBCA.toLowerCase())) {
            layoutKlikbca.setVisibility(View.VISIBLE);
        }else if (type.equals("creditcard")){
            showPayment();
        }*/
        /*String token = "19d0ceaca45f9ee27e3c51df52786f1d904280f9";
        url = url + "&checkouttoken=" + token;
        showPayment(url);*/

        /*whatOrder = ListOrderActivity.getWhatOrder();
        if (whatOrder.equals("FLIGHT")) {
            layoutFlight.setVisibility(View.VISIBLE);
            layoutHotel.setVisibility(View.GONE);
            setValueFlight();
        } else if (whatOrder.equals("HOTEL")) {
            layoutFlight.setVisibility(View.GONE);
            layoutHotel.setVisibility(View.VISIBLE);
            setValueHotel();
        }*/
    }

    private void getPolicies() {
        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        String url = CommonConstants.BASE_URL + "general_api/getPolicies";

        APIAgent client = new APIAgent();
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
                    JSONObject object = response.getJSONObject(CommonConstants.POLICIES);
                    parseData(object);
                    setValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PaymentActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON Payment", errorResponse + "");
                ErrorException.getError(PaymentActivity.this, errorResponse);
            }
        });
    }

    private void parseData(JSONObject object) throws Exception {
        Iterator<String> iterator = object.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            JSONObject policyObj = object.getJSONObject(key);
            JSONArray beforeArray = policyObj.getJSONArray(CommonConstants.BEFORE);
            JSONArray afterArray = policyObj.getJSONArray(CommonConstants.AFTER);

            ArrayList<String> beforeList = new ArrayList<>();
            ArrayList<String> afterList = new ArrayList<>();

            for (int i = 0; i < beforeArray.length(); i++) {
                String beforeItem = beforeArray.getString(i);
                beforeList.add(beforeItem);
            }

            for (int i = 0; i < afterArray.length(); i++) {
                String afterItem = afterArray.getString(i);
                afterList.add(afterItem);
            }

            Policy policy = new Policy();
            policy.name = policyObj.getString(CommonConstants.NAME);
            policy.before = beforeList;
            policy.after = afterList;
            policy.type = key;

            policiesMap.put(key, policy);
        }
    }

    private void setValue() {
        for (int i = 0; i < orders.size(); i++) {
            MyOrder myOrder = orders.get(i);
            View view = inflater.inflate(R.layout.item_myorder, null);
            TextView tvOrderType = (TextView) view.findViewById(R.id.tv_order_type);
            TextView tvOrderName = (TextView) view.findViewById(R.id.tv_order_name);
            TextView tvOrderNameDetail = (TextView) view.findViewById(R.id.tv_order_name_detail);
            TextView tvOrderExpire = (TextView) view.findViewById(R.id.tv_order_expire);
            ImageView ivDeleteOrder = (ImageView) view.findViewById(R.id.iv_delete_order);
            ImageView ivOrder = (ImageView) view.findViewById(R.id.iv_order);
            TextView ivPriceOrder = (TextView) view.findViewById(R.id.tv_order_price);

            tvOrderType.setText(myOrder.orderType.toUpperCase());
            tvOrderName.setText(myOrder.orderName);
            tvOrderNameDetail.setText(myOrder.orderNameDetail);
            tvOrderExpire.setText("Expired until: " + myOrder.orderExpireDatetime);
            ivPriceOrder.setText(Util.toRupiahFormat(myOrder.subtotalAndCharge));
            Picasso.with(this).load(myOrder.orderPhoto).into(ivOrder);
            ivDeleteOrder.setVisibility(View.INVISIBLE);

            orderWrapper.addView(view);
        }

        Policy currentPolicy = policiesMap.get(type);
        for (int i = 0; i < currentPolicy.before.size(); i++) {
            TextView view = (TextView) inflater.inflate(R.layout.item_step, null);
            view.setText("\u2022 " + currentPolicy.before.get(i));
            stepWrapper.addView(view);
        }
    }

    @OnClick(R.id.next_btn)
    void nextOnClick() {
        if (isKlikBCA) {
            if (evKlikbca.getText().toString().isEmpty()) {
                evKlikbca.setError("Diperlukan");
            } else {
                getData(url);
            }
        } else {
            getData(url);
        }
    }

    private void getData(String url) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);
        requestParams.put(CommonConstants.LANG, CommonConstants.ID);
        requestParams.put(CommonConstants.CURRENCY, "IDR");

        if (isAfter) {
            requestParams.put(CommonConstants.BTN_BOOKING, "1");
        }

        if (isAfter && isKlikBCA)
            requestParams.put(CommonConstants.USER_BCA, evKlikbca.getText().toString());

        if (isAfter && isMandiri) {
            requestParams.put("card_no", mandiriCardNumberEt.getText().toString());
            requestParams.put("token_response", tokenResponseEt.getText().toString());
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.getting_order));

        APIAgent client = new APIAgent();
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
                    JSONObject diagnosticObj = response.getJSONObject(CommonConstants.DIAGNOSTIC);
                    int status = diagnosticObj.getInt(CommonConstants.STATUS);

                    if (status != 200) {
                        String errorMsgs = diagnosticObj.getString(CommonConstants.ERROR_MSGS);
                        showAlertDialog(errorMsgs);
                        return;
                    }

                    if (!isAfter) {
                        JSONObject object = response.getJSONObject(CommonConstants.RESULT);
                        orderId = object.getString(CommonConstants.ORDER_ID);
                        int grandSubtotal = object.getInt(CommonConstants.GRAND_TOTAL);
                        int discount = object.getInt(CommonConstants.PAYMENT_DISCOUNT);
                        tvOrderId.setText(orderId);
                        tvTotal.setText("IDR " + grandSubtotal);
                        discountTV.setText("IDR " + discount);
                        isAfter = true;

                        getPolicies();
                        countDownTime();
                    } else {
                        Intent intent = new Intent(PaymentActivity.this, FinishOrderActivity.class);
                        intent.putExtra(CommonConstants.RESPONE, response.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
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
                Log.e("JSON Payment", errorResponse + "");
                ErrorException.getError(PaymentActivity.this, errorResponse);
            }
        });
    }

    private void countDownTime() {
//        final String dummy = "2016-04-19 09:51:00";
        final String interval = Util.printDifference(initialTime);
//        final String interval = Util.printDifference(dummy);

        if (interval.equals("expired")) {
            tvExpiredTime.setText("Time Expired");
            showAlertDialog("your order has expired please recheck your order");
        } else {
            tvExpiredTime.postDelayed(new Runnable() {
                public void run() {
                    String interval = Util.printDifference(initialTime);
//                    String interval = Util.printDifference(dummy);
                    tvExpiredTime.setText(interval);
                    tvExpiredTime.postDelayed(this, 1000);

                    if (interval.equals("expired")) {
                        tvExpiredTime.setText("Time Expired");
                        tvExpiredTime.removeCallbacks(this);
                        showAlertDialog("your order has expired please recheck your order");
                    }
                }
            }, 1000);

        }
    }

    private void showAlertDialog(String message) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                this).create();

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void getFinishPayment(String url) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, "19d0ceaca45f9ee27e3c51df52786f1d904280f9");
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        requestParams.put(CommonConstants.BTN_BOOKING, "1");
        requestParams.put(CommonConstants.CURRENCY, "IDR");

        if (type.equals(CommonConstants.KLIKBCA.toLowerCase())) {
            requestParams.put(CommonConstants.USER_BCA, evKlikbca.getText().toString());
            tvStepPayment.setVisibility(View.VISIBLE);
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        APIAgent client = new APIAgent();
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
                Log.e("JSON FINISH PAYMENT", response.toString() + "");
                try {
                    Gson gson = new Gson();

                    JSONArray stepsArray = response.getJSONArray(CommonConstants.STEPS);
                    for (int i = 0; i < stepsArray.length(); i++) {
                        stepses.add(gson.fromJson(stepsArray.getJSONObject(i).toString(), Steps.class));
                    }

                    stepsAdapter = new StepsAdapter(PaymentActivity.this, stepses);
                    listStepPayment.setAdapter(stepsAdapter);
                    Util.setListview(listStepPayment);

                } catch (JSONException e) {
                    e.printStackTrace();

                    try {
                        JSONArray stepArray = response.getJSONObject(CommonConstants.STEPS).getJSONArray(CommonConstants.STEP);
                        String step = "";
                        for (int i = 0; i < stepArray.length(); i++) {
                            step += "- " + stepArray.getString(i) + "\n";
                        }
                        if (type.equals(CommonConstants.KLIKBCA.toLowerCase())) {
                            tvStepPayment.setText(step);
                            tvStepPayment.setVisibility(View.VISIBLE);
                            layoutKlikbca.setVisibility(View.GONE);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

                ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(null);
                ((TextView) findViewById(R.id.next_btn)).setText("Selesai");
                tvSisaWaktu.setVisibility(View.GONE);
                tvUpto.setVisibility(View.GONE);
                layoutTime.setVisibility(View.GONE);
                listStepPayment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PaymentActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON Payment", errorResponse + "");
                ErrorException.getError(PaymentActivity.this, errorResponse);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
