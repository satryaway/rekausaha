package com.reka.tour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.reka.tour.R;
import com.reka.tour.adapter.MaskapaiAdapter;
import com.reka.tour.model.Departures;
import com.reka.tour.model.Maskapai;
import com.reka.tour.utils.CommonConstants;
import com.reka.tour.utils.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity {
    @Bind(R.id.cb_pagi)
    CheckBox cbPagi;
    @Bind(R.id.cb_siang)
    CheckBox cbSiang;
    @Bind(R.id.cb_sore)
    CheckBox cbSore;
    @Bind(R.id.cb_malam)
    CheckBox cbMalam;

    @Bind(R.id.cb_nonstop)
    CheckBox cbNonstop;
    @Bind(R.id.cb_transit)
    CheckBox cbTransit;


    @Bind(R.id.rg_price)
    RadioGroup rgPrice;
    @Bind(R.id.list_maskapai)
    ListView listMaskapai;

    @Bind(R.id.tv_save)
    TextView tvSave;

    private RadioButton rbPrice;

    private MaskapaiAdapter maskapaiAdapter;
    private ArrayList<Maskapai> maskapais = new ArrayList<>();
    private ArrayList<Departures> departures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setValue();
        Util.setListview(listMaskapai);
        setCallBack();
    }

    private void setCallBack() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rgPrice.getCheckedRadioButtonId();
                rbPrice = (RadioButton) findViewById(selectedId);
                Toast.makeText(FilterActivity.this, rbPrice.getText(), Toast.LENGTH_SHORT).show();


                Intent myIntent = new Intent();
                myIntent.putExtra(CommonConstants.DEPARTURES, departures);
                myIntent.putExtra(CommonConstants.FILTER_MASKAPAI, filterMaskapai());
                myIntent.putExtra(CommonConstants.FILTER_JAM, filterJam());
                myIntent.putExtra(CommonConstants.FILTER_OPSI, filterOpsi());
                myIntent.putExtra(CommonConstants.FILTER_HARGA, rbPrice.getText());
                setResult(RESULT_OK, myIntent);
                finish();
            }
        });
    }

    private void setValue() {

        departures = (ArrayList<Departures>) getIntent().getSerializableExtra(CommonConstants.FLIGHT);

        for (int i = 0; i < departures.size(); i++) {
            maskapais.add(new Maskapai(
                    departures.get(i).airlinesName,
                    true
            ));
        }

        Map<String, Maskapai> maskapaiMap = new LinkedHashMap<>();
        for (Maskapai maskapai : maskapais) {
            maskapaiMap.put(maskapai.getAirplane(), maskapai);
        }
        maskapais.clear();
        maskapais.addAll(maskapaiMap.values());

        maskapaiAdapter = new MaskapaiAdapter(this, maskapais);
        listMaskapai.setAdapter(maskapaiAdapter);
    }


    private String filterMaskapai() {
        String maskapaiString = "";

        for (int i = 0; i < maskapaiAdapter.getCount(); i++) {
            View view = listMaskapai.getChildAt(i);
            CheckBox cbMaskapai = (CheckBox) view.findViewById(R.id.cb_maskapai);
            if (cbMaskapai.isChecked()) {
                maskapaiString += cbMaskapai.getText().toString() + ",";
            }
        }
        return maskapaiString;
    }

    private String filterJam() {
        String jamString = "";
        if (cbPagi.isChecked() && cbSiang.isChecked() && cbSore.isChecked() && cbMalam.isChecked()) {
            return jamString;
        } else {
            if (cbPagi.isChecked()) {
                jamString += "3:9,";
            }
            if (cbSiang.isChecked()) {
                jamString += "9:15,";
            }
            if (cbSore.isChecked()) {
                jamString += "15:18,";
            }
            if (cbMalam.isChecked()) {
                jamString += "18:3,";
            }
            return jamString;
        }
    }

    private String filterOpsi() {
        String filterOpsi = "";

        if (cbNonstop.isChecked() && cbTransit.isChecked()) {
            return filterOpsi;
        } else {
            if(cbNonstop.isChecked()){
                filterOpsi+="langsung";
            }else {
                filterOpsi+="transit";
            }
            return filterOpsi;
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
