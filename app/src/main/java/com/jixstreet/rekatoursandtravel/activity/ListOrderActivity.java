package com.jixstreet.rekatoursandtravel.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ListOrderActivity extends AppCompatActivity {
    private static ArrayList<MyOrder> myOrders = new ArrayList<>();
    @Bind(R.id.list_order)
    ListView listOrder;
    private MyOrderAdapter myOrderAdapter;
    private Bundle bundle;
    private String orderId;
    private HashMap<String, String> contactMap = new HashMap<>();
    private String checkoutCustomerURL;
    private boolean isHotel = false;
    private boolean isSecondTime = false;
    private static HashMap<String, String> staticContactMap;
    private HashMap<String, String> hotelCustomerMap = new HashMap<>();
    private int orderIteration = -1;

    public static ArrayList<MyOrder> getMyOrders() {
        return myOrders;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);

        Log.d("TOKEN", RekaApplication.getInstance().getToken());
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        bundle = getIntent().getExtras();
        isHotel = bundle.getBoolean(CommonConstants.IS_HOTEL);
        contactMap = (HashMap<String, String>) bundle.getSerializable(CommonConstants.CONTACT_MAP);
        hotelCustomerMap = (HashMap<String, String>) bundle.getSerializable(CommonConstants.HOTE_CUSTOMER_MAP);

        staticContactMap = contactMap;

        getData();

        setCallBack();
    }

    private void setCallBack() {
        findViewById(R.id.tv_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOrders.size() > 0) {
                    checkoutRequest();
                }
            }
        });
    }

    private void checkoutRequest() {
        String url = CommonConstants.BASE_URL + "order/checkout/" + orderId + "/IDR";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

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
                    int statCode = response.getJSONObject(CommonConstants.DIAGNOSTIC).getInt(CommonConstants.STATUS);
                    checkoutCustomerURL = response.getString(CommonConstants.NEXT_CHECHOUT_URI);
                    if (statCode == 200) {
                        checkoutCustomer();
                    }

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

    private void checkoutCustomer() {
        String url = checkoutCustomerURL;

        RequestParams requestParams = new RequestParams();
        if (!isHotel) {
            requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
            requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

            String key = "salutation";
            requestParams.put(key, getValue(key));
            key = "firstName";
            requestParams.put(key, getValue(key));
            key = "lastName";
            requestParams.put(key, getValue(key));
            key = "emailAddress";
            requestParams.put(key, getValue(key));
            key = "phone";
            requestParams.put(key, getValue(key));

            requestParams.put("saveContinue", 2);
        } else {
            if (!isSecondTime)
                requestParams = getRequestParamsForCheckoutLogin();
            else
                requestParams = getRequestParamsForCheckoutProcess();
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.registering));

        if (isSecondTime) {
            progressDialog.setMessage("Saving your order");
        }

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
                    int statCode = response.getJSONObject(CommonConstants.DIAGNOSTIC).getInt(CommonConstants.STATUS);
                    if (statCode == 200) {
                        if (!isHotel) {
                            Intent intent = new Intent(ListOrderActivity.this, ListPaymentActivity.class);
                            startActivity(intent);
                        } else {
                            if (orderIteration == myOrders.size() - 1) {
                                Intent intent = new Intent(ListOrderActivity.this, ListPaymentActivity.class);
                                startActivity(intent);
                            } else {
                                isSecondTime = true;
                                orderIteration++;
                                checkoutCustomer();
                            }
                        }
                    } else {
                        String message = response.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.error_msgs);
                        showAlertDialog(message);
                    }

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

    private void showAlertDialog(String message) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                ListOrderActivity.this).create();

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private RequestParams getRequestParamsForCheckoutProcess() {
        RequestParams requestParams = new RequestParams();
        String key = "express_fullname";
        requestParams.put(key, hotelCustomerMap.get(key));
        key = "express_phone";
        requestParams.put(key, hotelCustomerMap.get(key));
        key = "express_email";
        requestParams.put(key, hotelCustomerMap.get(key));
        key = "conSalutation";
        requestParams.put(key, hotelCustomerMap.get(key));
        key = "conFirstName";
        requestParams.put(key, hotelCustomerMap.get(key));
        key = "conLastName";
        requestParams.put(key, hotelCustomerMap.get(key));
        key = "conPhone";
        requestParams.put(key, hotelCustomerMap.get(key));

        requestParams.put("detailId", myOrders.get(orderIteration).orderDetailId);
        requestParams.put("country", "ID");
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);
        return requestParams;
    }

    private RequestParams getRequestParamsForCheckoutLogin() {
        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.SALUTATION, hotelCustomerMap.get("conSalutation"));
        requestParams.put(CommonConstants.FIRSTNAME, hotelCustomerMap.get("conFirstName"));
        requestParams.put(CommonConstants.LASTNAME, hotelCustomerMap.get("conLastName"));
        requestParams.put("phone", hotelCustomerMap.get("conPhone"));
        requestParams.put(CommonConstants.EMAILADDRESS, hotelCustomerMap.get("conEmailAddress"));
        requestParams.put("saveContinue", "2");
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        return requestParams;
    }

    private String getValue(String key) {
        String value = "";
        for (String keyMap : contactMap.keySet()) {
            if (keyMap.toLowerCase().contains(key.toLowerCase())) {
                value = contactMap.get(keyMap);
            }
        }
        return value;
    }
/*
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
    }*/

    private void getData() {
        String url = CommonConstants.BASE_URL + "order";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(100000);
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

                    myOrders.clear();
                    JSONArray myOrderArrayList = response.getJSONObject(CommonConstants.MYORDER).getJSONArray(CommonConstants.DATA);
                    for (int i = 0; i < myOrderArrayList.length(); i++) {
                        myOrders.add(gson.fromJson(myOrderArrayList.getJSONObject(i).toString(), MyOrder.class));
                    }

                    orderId = response.getJSONObject(CommonConstants.MYORDER).getString(CommonConstants.ORDER_ID);

                    myOrderAdapter = new MyOrderAdapter(ListOrderActivity.this, myOrders);
                    listOrder.setAdapter(myOrderAdapter);
                    myOrderAdapter.setOnDeleteClickListener(new MyOrderAdapter.OnDeleteClickListener() {
                        @Override
                        public void callBack(int position) {
                            deleteOrderId(position);
                        }
                    });

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

        Log.d("TOKEN", RekaApplication.getInstance().getToken());

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.ORDER_DETAIL_ID, myOrders.get(position).orderDetailId);
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

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
