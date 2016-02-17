package com.reka.tour.activity;

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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.adapter.MethodPaymentAdapter;
import com.reka.tour.model.MethodPayment;
import com.reka.tour.utils.CommonConstants;

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
        Intent intent = new Intent(ListPaymentActivity.this, PaymentActivity.class);
        intent.putExtra(CommonConstants.LINK, methodPayments.get(position).link);
        startActivity(intent);
    }


    private void getMethodPayment() {
        String url = CommonConstants.BASE_URL + "checkout/checkout_payment";

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
                try {
                    Toast.makeText(ListPaymentActivity.this, errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS), Toast.LENGTH_SHORT).show();
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
