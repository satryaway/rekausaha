package com.reka.tour.hotel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;
import com.reka.tour.R;
import com.reka.tour.utils.CommonConstants;
import com.reka.tour.utils.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterHotelActivity extends AppCompatActivity {
    @Bind(R.id.rb_bintang_min)
    RatingBar rbBintangMin;
    @Bind(R.id.rb_bintang_max)
    RatingBar rbBintangMax;
    @Bind(R.id.rangebar_bintang)
    RangeBar rangebarBintang;

    @Bind(R.id.tv_harga_min)
    TextView tvHargaMin;
    @Bind(R.id.tv_harga_max)
    TextView tvHargaMax;
    @Bind(R.id.rangebar_harga)
    RangeBar rangebarHarga;


    private double MAX_PRICE;
    private double minPrice, maxPrice;
    private int minStar = 0, maxStar = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_filter);

        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setValue();
        setCallBack();
    }

    private void setValue() {
        MAX_PRICE = getIntent().getExtras().getDouble(CommonConstants.MAX_PRICE);
        minPrice = MAX_PRICE * 0 / 100;
        maxPrice = MAX_PRICE * 100 / 100;

        tvHargaMin.setText(Util.toRupiahFormat(String.valueOf(minPrice)));
        tvHargaMax.setText(Util.toRupiahFormat(String.valueOf(maxPrice)));
    }

    private void setCallBack() {
        rangebarBintang.setTickHeight(0);
        rangebarBintang.setTickCount(6);
        rangebarBintang.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
                minStar = leftThumbIndex;
                maxStar = rightThumbIndex;

                rbBintangMin.setRating(leftThumbIndex);
                rbBintangMin.setIsIndicator(true);

                rbBintangMax.setRating(rightThumbIndex);
                rbBintangMax.setIsIndicator(true);

                Log.e("minStar", minStar + " " + maxStar);
            }
        });

        rangebarHarga.setTickHeight(0);
        rangebarHarga.setTickCount(100);
        rangebarHarga.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
                minPrice = MAX_PRICE * leftThumbIndex / 100;
                maxPrice = MAX_PRICE * rightThumbIndex / 100;
                tvHargaMin.setText(Util.toRupiahFormat(String.valueOf(minPrice)));
                tvHargaMax.setText(Util.toRupiahFormat(String.valueOf(maxPrice)));
            }
        });

    }

    @OnClick(R.id.tv_save)
    public void tvSave(View view) {
        Intent myIntent = new Intent();
        myIntent.putExtra(CommonConstants.FILTER_MAX_BINTANG, maxStar);
        myIntent.putExtra(CommonConstants.FILTER_MIN_BINTANG, minStar);
        myIntent.putExtra(CommonConstants.FILTER_MAX_PRICE, maxPrice);
        myIntent.putExtra(CommonConstants.FILTER_MIN_PRICE, minPrice);
        setResult(RESULT_OK, myIntent);
        finish();
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
