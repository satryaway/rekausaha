package com.reka.tour.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.utils.CommonConstants;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class MethodPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private static String orderId;
    @Bind(R.id.tv_kartu_kredit)
    TextView tvKartuKredit;
    @Bind(R.id.tv_atm_transfer)
    TextView tvAtmTransfer;
    @Bind(R.id.tv_cimb_clicks)
    TextView tvCimbClicks;
    @Bind(R.id.tv_klik_bca)
    TextView tvKlikBca;
    @Bind(R.id.tv_mandiri_clickpay)
    TextView tvMandiriClickpay;
    @Bind(R.id.tv_epay_bri)
    TextView tvEpayBri;
    @Bind(R.id.tv_bca_klikpay)
    TextView tvBcaKlikpay;

    public static String getOrderId() {
        return orderId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_payment);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getData();
        setCallBack();
    }

    private void setCallBack() {
        tvKartuKredit.setOnClickListener(this);
        tvAtmTransfer.setOnClickListener(this);
        tvCimbClicks.setOnClickListener(this);
        tvKlikBca.setOnClickListener(this);
        tvMandiriClickpay.setOnClickListener(this);
        tvEpayBri.setOnClickListener(this);
        tvBcaKlikpay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_kartu_kredit:
//                break;
            case R.id.tv_atm_transfer:
//                break;
            case R.id.tv_cimb_clicks:
//                break;
            case R.id.tv_klik_bca:
//                break;
            case R.id.tv_mandiri_clickpay:
//                break;
            case R.id.tv_epay_bri:
//                break;
            case R.id.tv_bca_klikpay:
                Intent intentDeparture = new Intent(MethodPaymentActivity.this, PaymentActivity.class);
//                intentDeparture.putExtra(CommonConstants.DEPARTURES, depAirportArrayList.get(position));
//                intentDeparture.putExtra(CommonConstants.SEARCH_QUARIES, searchQueries);
                startActivity(intentDeparture);
                break;

            default:
                return;
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
                    orderId = response.getJSONObject(CommonConstants.MYORDER).getString(CommonConstants.ORDER_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MethodPaymentActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(MethodPaymentActivity.this, errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS), Toast.LENGTH_SHORT).show();
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
