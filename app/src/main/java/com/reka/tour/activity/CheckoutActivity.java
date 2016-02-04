package com.reka.tour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.model.Departures;
import com.reka.tour.model.SearchQueries;
import com.reka.tour.utils.CommonConstants;
import com.reka.tour.utils.Util;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CheckoutActivity extends AppCompatActivity {
    @Bind(R.id.tv_date)
    TextView tvDate;

    @Bind(R.id.iv_flight)
    ImageView ivFlight;
    @Bind(R.id.tv_name)
    TextView tvAirlinesName;
    @Bind(R.id.tv_flight_number)
    TextView tvFlightNumber;

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


    private Bundle bundle;
    private Departures departures;
    private SearchQueries searchQueries;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("ind", "IDN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);


        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setValue();
        setCallBack();
    }

    private void setCallBack() {
        findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDeparture = new Intent(CheckoutActivity.this, InfoPassangerActivity.class);
//                intentDeparture.putExtra(CommonConstants.SEARCH_QUARIES, searchQueries);
                startActivity(intentDeparture);
            }
        });
    }

    private void setValue() {
        bundle = getIntent().getExtras();
        departures = (Departures) getIntent().getSerializableExtra(CommonConstants.DEPARTURES);
        searchQueries = (SearchQueries) getIntent().getSerializableExtra(CommonConstants.SEARCH_QUARIES);

        try {
            tvDate.setText(dateFormatter.format(dateFormat.parse(searchQueries.date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.with(this).load(departures.image).into(ivFlight);
        tvAirlinesName.setText(departures.airlinesName);
        tvFlightNumber.setText(departures.flightNumber);

        tvRute.setText(departures.fullVia);
        tvDuration.setText("Duration : " + departures.duration);

        tvAdult.setText(searchQueries.adult + " Dewasa");
        tvAdultPrice.setText(Util.toRupiahFormat(departures.priceAdult));

        if (searchQueries.child.equals("0")) {
            layoutChild.setVisibility(View.GONE);
        } else {
            tvChild.setText(searchQueries.child + " Anak");
            tvChildPrice.setText(Util.toRupiahFormat(departures.priceChild));

        }

        if (searchQueries.infant.equals("0")) {
            layoutInfrant.setVisibility(View.GONE);
        } else {
            tvInfrant.setText(searchQueries.infant + " Bayi");
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
}
