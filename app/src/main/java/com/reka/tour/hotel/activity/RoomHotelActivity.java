package com.reka.tour.hotel.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.hotel.adapter.RoomAdapter;
import com.reka.tour.hotel.model.Breadcrumb;
import com.reka.tour.hotel.model.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RoomHotelActivity extends AppCompatActivity {
    private static  ArrayList<Room> rooms = new ArrayList<>();
    private static String dateCheckin;
    private static String dateCheckout;
    private static String room;
    private static String tamu;
    @Bind(R.id.list_room)
    ListView listRoom;
    private RoomAdapter roomAdapter;
    private Breadcrumb breadcrumb;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("ind", "IDN"));

    public static String getTamu() {
        return tamu;
    }

    public static ArrayList<Room> getRooms() {
        return rooms;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_hotel);

        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setValue();
    }

    private void setValue() {
        breadcrumb = ProfileHotelActivity.getBreadcrumb();
        ((TextView) findViewById(R.id.title_hotel)).setText(breadcrumb.businessName);
        ((TextView) findViewById(R.id.location_hotel)).setText(breadcrumb.areaName);

        dateCheckin = HotelActivity.getDateCheckin();
        dateCheckout = HotelActivity.getDateCheckout();
        room = HotelActivity.getRoom();
        tamu = HotelActivity.getTamu();

        try {
            ((TextView) findViewById(R.id.tv_date_checkin)).setText(dateFormatter.format(dateFormat.parse(dateCheckin)));
            ((TextView) findViewById(R.id.tv_date_checkout)).setText(dateFormatter.format(dateFormat.parse(dateCheckout)));
            ((TextView) findViewById(R.id.tv_count_room)).setText(room);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        rooms = ProfileHotelActivity.getRooms();
        roomAdapter = new RoomAdapter(this, rooms);
        listRoom.setAdapter(roomAdapter);
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
