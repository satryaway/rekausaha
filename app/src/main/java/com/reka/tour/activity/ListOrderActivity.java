package com.reka.tour.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.flight.adapter.MyOrderAdapter;
import com.reka.tour.hotel.activity.InfoCustomerHotelActivity;
import com.reka.tour.model.MyOrder;
import com.reka.tour.utils.CommonConstants;
import com.reka.tour.utils.ErrorException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ListOrderActivity extends AppCompatActivity {
    private static ArrayList<MyOrder> myOrders = new ArrayList<>();
    @Bind(R.id.list_order) ListView listOrder;
    private MyOrderAdapter myOrderAdapter;
    private Bundle bundle;
    private static String whatOrder;

    public static ArrayList<MyOrder> getMyOrders() {
        return myOrders;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        bundle = getIntent().getExtras();
//        whatOrder = bundle.getString(CommonConstants.WHAT_ORDER);
        whatOrder = "FLIGHT";

        Log.e("whatOrder", whatOrder + "");

        getData();
        setCallBack();
    }

    public static String getWhatOrder() {
        return whatOrder;
    }

    private void setCallBack() {
        findViewById(R.id.tv_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOrders.size() > 0) {
                    if (whatOrder.equals("FLIGHT")) {
                        Intent intent = new Intent(ListOrderActivity.this, ListPaymentActivity.class);
                        startActivity(intent);
                    } else if (whatOrder.equals("HOTEL")) {
                        Intent intent = new Intent(ListOrderActivity.this, InfoCustomerHotelActivity.class);
                        intent.putExtra(CommonConstants.DETAIL_ID, myOrders.get(0).orderDetailId);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @OnClick(R.id.tv_checkout)
    public void checkout() {
        if (myOrders.size() == 1) {
            if (whatOrder.equals("FLIGHT")) {
                Intent intent = new Intent(ListOrderActivity.this, ListPaymentActivity.class);
                startActivity(intent);
            } else if (whatOrder.equals("HOTEL")) {
                Intent intent = new Intent(ListOrderActivity.this, InfoCustomerHotelActivity.class);
                intent.putExtra(CommonConstants.DETAIL_ID, myOrders.get(0).orderDetailId);
                startActivity(intent);
            }
        }
    }

    private void getData() {
        String url = CommonConstants.BASE_URL + "order";

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
                Log.e("JSON FLIGHT", response.toString() + "");
                try {
                    Gson gson = new Gson();

                    JSONArray myOrderArrayList = response.getJSONObject(CommonConstants.MYORDER).getJSONArray(CommonConstants.DATA);
                    for (int i = 0; i < myOrderArrayList.length(); i++) {
                        myOrders.add(gson.fromJson(myOrderArrayList.getJSONObject(i).toString(), MyOrder.class));
                    }

                    myOrderAdapter = new MyOrderAdapter(ListOrderActivity.this, myOrders, ListOrderActivity.this);
                    listOrder.setAdapter(myOrderAdapter);

                    /*if (myOrders.size() == 1) {
                        if (whatOrder.equals("FLIGHT")) {
                            Intent intent = new Intent(ListOrderActivity.this, ListPaymentActivity.class);
                            startActivity(intent);
                        } else if (whatOrder.equals("HOTEL")) {
                            Intent intent = new Intent(ListOrderActivity.this, InfoCustomerHotelActivity.class);
                            intent.putExtra(CommonConstants.DETAIL_ID, myOrders.get(0).orderDetailId);
                            startActivity(intent);
                        }

                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ListOrderActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON ListOrder", errorResponse + "");
                ErrorException.getError(ListOrderActivity.this, errorResponse);
            }
        });
    }

    public void deleteOrderId(final int position) {
        String url = CommonConstants.BASE_URL + "order/delete_order";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.ORDER_DETAIL_ID, myOrders.get(position).orderDetailId);
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
                myOrders.remove(position);
                myOrderAdapter.notifyDataSetChanged();
                setCallBack();
//                try {
//                    orderId = response.getJSONObject(CommonConstants.MYORDER).getString(CommonConstants.ORDER_ID);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ListOrderActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON ListOrder", errorResponse + "");
                ErrorException.getError(ListOrderActivity.this, errorResponse);
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
