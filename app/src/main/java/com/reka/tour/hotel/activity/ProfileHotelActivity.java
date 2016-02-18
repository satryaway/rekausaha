package com.reka.tour.hotel.activity;

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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.hotel.adapter.TabAdapter;
import com.reka.tour.hotel.model.Breadcrumb;
import com.reka.tour.hotel.model.Facilitiy;
import com.reka.tour.hotel.model.Foto;
import com.reka.tour.hotel.model.General;
import com.reka.tour.hotel.model.Room;
import com.reka.tour.utils.CommonConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
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
        setCallBack();
        getData();
    }

    private void setCallBack() {

        findViewById(R.id.tv_lihatkamar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSortir = new Intent(ProfileHotelActivity.this,
                        RoomHotelActivity.class);
                startActivity(intentSortir);
            }
        });
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
//        String url = CommonConstants.BASE_URL + "search/hotel";
        String url = "https://api-sandbox.tiket.com/hotel/indonesia/bali/legian/the-sunset-hotel-restaurant?" +
                "startdate=2016-02-18&enddate=2016-02-19&night=1&room=1&adult=2&child=0&is_partner=0&latitude=0&" +
                "longitude=0&distance=0&uid=business%3A3623&token=19d0ceaca45f9ee27e3c51df52786f1d904280f9&output=json";

        RequestParams requestParams = new RequestParams();
//        requestParams.put(CommonConstants.STARTDATE, bundle.getString(CommonConstants.Q));
//        requestParams.put(CommonConstants.ENDDATE, bundle.getString(CommonConstants.STARTDATE));
//        requestParams.put(CommonConstants.ROOM, bundle.getString(CommonConstants.ROOM));
//        requestParams.put(CommonConstants.ADULT, bundle.getString(CommonConstants.ADULT));
//        requestParams.put(CommonConstants.TOKEN, "19d0ceaca45f9ee27e3c51df52786f1d904280f9");
//        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
        client.get(url, null, new JsonHttpResponseHandler() {
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
                try {
                    Toast.makeText(ProfileHotelActivity.this, errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
