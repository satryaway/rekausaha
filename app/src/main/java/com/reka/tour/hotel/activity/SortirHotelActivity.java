package com.reka.tour.hotel.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.reka.tour.R;

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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_terpopuler:
                break;
            case R.id.layout_termurah:
                break;
            case R.id.layout_termahal:
                break;
            case R.id.layout_tertinggi:
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
