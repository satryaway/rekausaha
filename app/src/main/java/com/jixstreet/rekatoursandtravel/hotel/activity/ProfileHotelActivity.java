package com.jixstreet.rekatoursandtravel.hotel.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.hotel.adapter.TabAdapter;
import com.jixstreet.rekatoursandtravel.hotel.model.Breadcrumb;
import com.jixstreet.rekatoursandtravel.hotel.model.Facilitiy;
import com.jixstreet.rekatoursandtravel.hotel.model.Foto;
import com.jixstreet.rekatoursandtravel.hotel.model.General;
import com.jixstreet.rekatoursandtravel.hotel.model.Room;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;
import com.jixstreet.rekatoursandtravel.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ProfileHotelActivity extends AppCompatActivity {


    private static ArrayList<Room> rooms = new ArrayList<>();
    private static Breadcrumb breadcrumb;

    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.tabLayout) TabLayout tabLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;

    private ArrayList<Foto> fotos = new ArrayList<>();
    private ArrayList<Facilitiy> facilitiys = new ArrayList<>();
    private General general;

    private String url;
    private Bundle bundle;


    public static Breadcrumb getBreadcrumb() {
        return breadcrumb;
    }

    public static ArrayList<Room> getRooms() {
        return rooms;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_profile);

        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ((Toolbar) findViewById(R.id.toolbar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        setupCollapsingToolbarLayout();
        setValue();
        getData();
    }

    private void setValue() {
        bundle = getIntent().getExtras();
        ((TextView) findViewById(R.id.tv_harga_hotel)).setText(Util.toRupiahFormat(bundle.getString(CommonConstants.PRICE_START)) + " /malam");
    }

    @OnClick(R.id.tv_lihatkamar)
    public void onClickLihatKamar() {
        Intent intentSortir = new Intent(ProfileHotelActivity.this,
                RoomHotelActivity.class);
        startActivity(intentSortir);
    }

    private void setupCollapsingToolbarLayout() {

        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
            collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));
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

    private void getData() {
        url = bundle.getString(CommonConstants.BUSSINESSURI);

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.addHeader("user-agent", "twh:[22691871]:[Reka Tours dan Travel]");
        client.setTimeout(100000);
        client.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                progressDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("JSON FLIGHT", response.toString() + "");
                try {

                    fotos = new ArrayList<>();
                    facilitiys = new ArrayList<>();
                    rooms = new ArrayList<>();

                    //getValue
                    Gson gson = new Gson();
                    JSONArray photoArray = response.getJSONObject(CommonConstants.ALL_PHOTO).getJSONArray(CommonConstants.PHOTO);
                    for (int i = 0; i < photoArray.length(); i++) {
                        fotos.add(gson.fromJson(photoArray.getJSONObject(i).toString(), Foto.class));
                    }

                    JSONArray facilitiyArray = response.getJSONObject(CommonConstants.AVAIL_FACILITIES).getJSONArray(CommonConstants.AVAIL_FACILITY);
                    for (int i = 0; i < facilitiyArray.length(); i++) {
                        facilitiys.add(gson.fromJson(facilitiyArray.getJSONObject(i).toString(), Facilitiy.class));
                    }

                    JSONArray roomArray = response.getJSONObject(CommonConstants.RESULTS).getJSONArray(CommonConstants.RESULT);
                    for (int i = 0; i < roomArray.length(); i++) {
                        rooms.add(gson.fromJson(roomArray.getJSONObject(i).toString(), Room.class));
                    }

                    JSONObject breadcrumbObject = response.getJSONObject(CommonConstants.BREADCRUMB);
                    breadcrumb = gson.fromJson(breadcrumbObject.toString(), Breadcrumb.class);

                    JSONObject generalObject = response.getJSONObject(CommonConstants.GENERAL);
                    general = gson.fromJson(generalObject.toString(), General.class);

                    String primaryPhotos = response.getString(CommonConstants.PRIMARYPHOTO);

                    //setValue
                    ((TextView) findViewById(R.id.title_hotel)).setText(breadcrumb.businessName);
                    ((TextView) findViewById(R.id.location_hotel)).setText(breadcrumb.areaName);
                    Picasso.with(ProfileHotelActivity.this).load(primaryPhotos)
                            .error(R.drawable.bg_sample)
                            .into(((ImageView) findViewById(R.id.iv_primaryphoto)));

                    viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
                    tabLayout.setupWithViewPager(viewPager);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ProfileHotelActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON ProfileHotel", errorResponse + "");
                ErrorException.getError(ProfileHotelActivity.this, errorResponse);
            }
        });
    }

    public ArrayList<Foto> getFotos() {
        return fotos;
    }

    public General getGeneral() {
        return general;
    }

    public ArrayList<Facilitiy> getFacilitiys() {
        return facilitiys;
    }
}
