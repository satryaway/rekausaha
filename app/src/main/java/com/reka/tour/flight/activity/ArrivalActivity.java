package com.reka.tour.flight.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.reka.tour.R;
import com.reka.tour.flight.adapter.FlightAdapter;
import com.reka.tour.flight.adapter.ScheduleAdapter;
import com.reka.tour.flight.model.Flight;
import com.reka.tour.flight.model.Schedule;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArrivalActivity extends AppCompatActivity {
    @Bind(R.id.list_schedule)
    TwoWayView listSchedule;
    @Bind(R.id.list_flight)
    ListView listFlight;

    private ScheduleAdapter scheduleAdapter;
    private ArrayList<Schedule> scheduleArrayList = new ArrayList<>();

    private FlightAdapter flightAdapter;
    private ArrayList<Flight> flightArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setValue();

    }

    private void setValue() {
//        for (int i = 1; i <= 8; i++) {
//            scheduleArrayList.add(new Schedule(
//                    "Sun",
//                    "1" + i + " Jan",
//                    "IDR 123." + (23 * i + 152)
//            ));
//        }
//
//        scheduleAdapter = new ScheduleAdapter(this, scheduleArrayList);
//        listSchedule.setAdapter(scheduleAdapter);
//
//        for (int i = 1; i <= 8; i++) {
//            flightArrayList.add(new Flight(
//                    "" + i,
//                    "Batik Air " + i,
//                    i + "1:00 - 10:" + i,
//                    i + ":20 ,Direct",
//                    "IDR 123." + (23 * i + 152)
//            ));
//        }
//
//        flightAdapter = new FlightAdapter(this, flightArrayList);
//        listFlight.setAdapter(flightAdapter);
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
