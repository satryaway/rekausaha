package com.jixstreet.rekatoursandtravel.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jixstreet.rekatoursandtravel.CheckoutPaymentActivity;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.jixstreet.rekatoursandtravel.adapter.MethodPaymentAdapter;
import com.jixstreet.rekatoursandtravel.flight.activity.InfoPassangerActivity;
import com.jixstreet.rekatoursandtravel.model.MethodPayment;
import com.jixstreet.rekatoursandtravel.model.Policy;
import com.jixstreet.rekatoursandtravel.utils.APIAgent;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cz.msebera.android.httpclient.Header;


public class ListPaymentActivity extends AppCompatActivity {
    private static String orderId;

    @Bind(R.id.list_method_payment)
    ListView listMethodPayment;

    private ArrayList<MethodPayment> methodPayments = new ArrayList<>();
    private MethodPaymentAdapter methodPaymentAdapter;
    public static String policies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_payment);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getMethodPayment();
    }

    @OnItemClick(R.id.list_method_payment)
    void onItemMethodPaymentClick(int position) {

        if (!methodPayments.get(position).link.equals("#")) {
            Intent intent;
            if (methodPayments.get(position).link.contains("api")) {
                intent = new Intent(ListPaymentActivity.this, PaymentActivity.class);
            } else {
                intent = new Intent(ListPaymentActivity.this, CheckoutPaymentActivity.class);
            }
            intent.putExtra(CommonConstants.LINK, methodPayments.get(position).link);
            intent.putExtra(CommonConstants.TYPE, methodPayments.get(position).type);
            intent.putExtra(CommonConstants.TEXT, methodPayments.get(position).text);
            startActivity(intent);
        }
    }


    private void getMethodPayment() {
        String url = CommonConstants.BASE_URL + "checkout/checkout_payment";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

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
                Log.e("JSON AVAILABLE_PAYMENT", response.toString() + "");
                try {
                    Gson gson = new Gson();

                    JSONArray availablePaymentArray = response.getJSONArray(CommonConstants.AVAILABLE_PAYMENT);
                    for (int i = 0; i < availablePaymentArray.length(); i++) {
                        methodPayments.add(gson.fromJson(availablePaymentArray.getJSONObject(i).toString(), MethodPayment.class));
                    }

                    methodPaymentAdapter = new MethodPaymentAdapter(ListPaymentActivity.this, methodPayments);
                    listMethodPayment.setAdapter(methodPaymentAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ListPaymentActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON ListPayment", errorResponse + "");
                ErrorException.getError(ListPaymentActivity.this, errorResponse);
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
