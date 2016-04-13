package com.jixstreet.rekatoursandtravel.hotel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SortirHotelActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.layout_terpopuler)
    RelativeLayout layoutTerpopuler;
    @Bind(R.id.layout_termurah)
    RelativeLayout layoutTermurah;
    @Bind(R.id.layout_termahal)
    RelativeLayout layoutTermahal;
    @Bind(R.id.layout_tertinggi)
    RelativeLayout layoutTertinggi;
    @Bind(R.id.layout_terendah)
    RelativeLayout layoutTerendah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_sortir);

        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setCallBack();
    }

    private void setCallBack() {
        layoutTerpopuler.setOnClickListener(this);
        layoutTermurah.setOnClickListener(this);
        layoutTermahal.setOnClickListener(this);
        layoutTertinggi.setOnClickListener(this);
        layoutTerendah.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent();
        Boolean SORTIR_TERPOPULER = false;
        Boolean SORTIR_HARGA_TERMURAH = false;
        Boolean SORTIR_HARGA_TERMAHAL = false;
        Boolean SORTIR_RATING_TERTINGGI = false;
        Boolean SORTIR_RATING_TERENDAH = false;

        switch (v.getId()) {
            case R.id.layout_terpopuler:
                SORTIR_TERPOPULER = true;
                break;
            case R.id.layout_termurah:
                SORTIR_HARGA_TERMURAH = true;
                break;
            case R.id.layout_termahal:
                SORTIR_HARGA_TERMAHAL = true;
                break;
            case R.id.layout_tertinggi:
                SORTIR_RATING_TERTINGGI = true;
                break;
            case R.id.layout_terendah:
                SORTIR_RATING_TERENDAH = true;
                break;
            default:
                return;
        }

        myIntent.putExtra(CommonConstants.SORTIR_TERPOPULER, SORTIR_TERPOPULER);
        myIntent.putExtra(CommonConstants.SORTIR_HARGA_TERMURAH, SORTIR_HARGA_TERMURAH);
        myIntent.putExtra(CommonConstants.SORTIR_HARGA_TERMAHAL, SORTIR_HARGA_TERMAHAL);
        myIntent.putExtra(CommonConstants.SORTIR_RATING_TERTINGGI, SORTIR_RATING_TERTINGGI);
        myIntent.putExtra(CommonConstants.SORTIR_RATING_TERENDAH, SORTIR_RATING_TERENDAH);
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
