package com.reka.tour.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.flight.activity.DetailOrderActivity;
import com.reka.tour.flight.model.DeparturesOrder;
import com.reka.tour.utils.Util;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity {
    @Bind(R.id.tv_orderid)
    TextView tvOrderId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setValue();
    }

    private void setValue() {
        departures = DetailOrderActivity.getDepartures();
        hasFood = DetailOrderActivity.getHasFood();
        airportTax = DetailOrderActivity.getAirportTax();
        needBaggage = DetailOrderActivity.getNeedBaggage();
        baggage = DetailOrderActivity.getBaggage();
        orderId = MethodPaymentActivity.getOrderId();

        tvOrderId.setText(orderId);

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
