package com.reka.tour.hotel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.reka.tour.R;
import com.reka.tour.hotel.adapter.HotelAdapter;
import com.reka.tour.model.Hotel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListHotelActivity extends AppCompatActivity {
    @Bind(R.id.list_hotel)
    ListView listHotel;

    private HotelAdapter hotelAdapter;
    private ArrayList<Hotel> hotels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hotel);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setValue();
        setCallBack();
    }

    private void setCallBack() {
        listHotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentSortir = new Intent(ListHotelActivity.this,
                        ProfileHotelActivity.class);
                startActivity(intentSortir);
            }
        });
    }

    private void setValue() {

        for (int i = 0; i < 10; i++) {
            hotels.add(new Hotel("", "3", "Hotel " + i, 20000 * i + ""));
        }

        hotelAdapter = new HotelAdapter(ListHotelActivity.this, hotels);
        listHotel.setAdapter(hotelAdapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_hotel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;

            case R.id.action_search:

                break;
            case R.id.action_sort:
                Intent intentSortir = new Intent(ListHotelActivity.this,
                        SortirHotelActivity.class);
                startActivity(intentSortir);
                break;
            case R.id.action_filter:
                Intent intentFilter = new Intent(ListHotelActivity.this,
                        FilterHotelActivity.class);
                startActivity(intentFilter);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
