package com.reka.tour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.reka.tour.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MethodPaymentActivity extends AppCompatActivity implements View.OnClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_payment);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

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
