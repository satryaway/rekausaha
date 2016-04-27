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
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.jixstreet.rekatoursandtravel.flight.adapter.MyOrderAdapter;
import com.jixstreet.rekatoursandtravel.model.MyOrder;
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
import cz.msebera.android.httpclient.Header;

public class MyBookingActivity extends AppCompatActivity {

    @Bind(R.id.list_order)
    ListView listOrderView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<MyOrder> myOrders = new ArrayList<>();
    private String orderId;
    private MyOrderAdapter myOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);

        getData();
        setCallBack();
    }

    private void setCallBack() {
    }

    private void getData() {
        String url = CommonConstants.BASE_URL + "order";

        RequestParams requestParams = new RequestParams();
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
                Log.e("JSON FLIGHT", response.toString() + "");
                try {
                    Gson gson = new Gson();
                    JSONArray myOrderArrayList = response.getJSONObject(CommonConstants.MYORDER).getJSONArray(CommonConstants.DATA);

                    for (int i = 0; i < myOrderArrayList.length(); i++) {
                        myOrders.add(gson.fromJson(myOrderArrayList.getJSONObject(i).toString(), MyOrder.class));
                    }

                    orderId = response.getJSONObject(CommonConstants.MYORDER).getString(CommonConstants.ORDER_ID);

                    myOrderAdapter = new MyOrderAdapter(MyBookingActivity.this, myOrders);
                    listOrderView.setAdapter(myOrderAdapter);
                    myOrderAdapter.setOnDeleteClickListener(new MyOrderAdapter.OnDeleteClickListener() {
                        @Override
                        public void callBack(int position) {
                            deleteOrderId(position);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MyBookingActivity.this, "You don't have any orders yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MyBookingActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON ListOrder", errorResponse + "");
                ErrorException.getError(MyBookingActivity.this, errorResponse);
            }
        });
    }

    public void deleteOrderId(final int position) {
        String url = CommonConstants.BASE_URL + "order/delete_order";

        Log.d("TOKEN", RekaApplication.getInstance().getToken());

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.ORDER_DETAIL_ID, myOrders.get(position).orderDetailId);
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
                Log.e("JSON FLIGHT", response.toString() + "");
                myOrders.remove(position);
                myOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MyBookingActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON ListOrder", errorResponse + "");
                ErrorException.getError(MyBookingActivity.this, errorResponse);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyBookingActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
