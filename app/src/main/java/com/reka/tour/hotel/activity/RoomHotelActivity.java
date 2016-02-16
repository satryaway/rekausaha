package com.reka.tour.hotel.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.reka.tour.R;
import com.reka.tour.hotel.adapter.RoomAdapter;
import com.reka.tour.hotel.model.Room;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RoomHotelActivity extends AppCompatActivity {
    @Bind(R.id.list_room)
    ListView listRoom;

    private RoomAdapter roomAdapter;
    private ArrayList<Room> rooms = new ArrayList<>();

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

        for (int i = 0; i < 10; i++) {
            rooms.add(new Room("Excecutive Room" + i, 213141 * i + "", "" + i));
        }

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
