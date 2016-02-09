package com.reka.tour.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.adapter.PassangerAdapter;
import com.reka.tour.model.Passanger;
import com.reka.tour.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InfoPassangerActivity extends AppCompatActivity {
    @Bind(R.id.ev_titel)
    EditText evTitel;
    @Bind(R.id.ev_name)
    EditText evName;
    @Bind(R.id.ev_notelp)
    EditText evNotelp;
    @Bind(R.id.ev_email)
    EditText evEmail;

    @Bind(R.id.list_passanger)
    ListView listPassanger;

    @Bind(R.id.tv_next)
    TextView tvNext;

    private PassangerAdapter passangerAdapter;
    private ArrayList<Passanger> passangers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_passanger);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));


        setValue();
        Util.setListview(listPassanger);
        setCallBack();
    }


    private void setValue() {
        for (int i = 1; i <= 3; i++) {
            passangers.add(new Passanger(
                    "Dewasa " + i,
                    "Tuan",
                    "Fachri Febrian" + i
            ));
        }

        passangerAdapter = new PassangerAdapter(this, passangers);
        listPassanger.setAdapter(passangerAdapter);
    }

    private void setCallBack() {
        final String[] itemsData = {"Pilih Titel", "Tuan", "Nyonya", "Nona"};
        evTitel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InfoPassangerActivity.this);
                    builder.setItems(itemsData, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            evTitel.setText(itemsData[item]);
                        }
                    }).create().show();
                }
                return true;
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDeparture = new Intent(InfoPassangerActivity.this, MethodPaymentActivity.class);
//                intentDeparture.putExtra(CommonConstants.DEPARTURES, depAirportArrayList.get(position));
//                intentDeparture.putExtra(CommonConstants.SEARCH_QUARIES, searchQueries);
                startActivity(intentDeparture);
            }
        });
    }

    public Passanger getProfile() {
        return new Passanger(
                "Dewasa 1",
                evTitel.getText().toString(),
                evName.getText().toString()
        );
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
