package com.jixstreet.rekatoursandtravel.hotel.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.jixstreet.rekatoursandtravel.flight.activity.InfoPassangerActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.activity.ListOrderActivity;
import com.jixstreet.rekatoursandtravel.hotel.model.Breadcrumb;
import com.jixstreet.rekatoursandtravel.hotel.model.Room;
import com.jixstreet.rekatoursandtravel.hotel.model.SearchQueriesHotel;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;
import com.jixstreet.rekatoursandtravel.utils.Util;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class OrderHotelActivity extends AppCompatActivity {

    private static String dateCheckin;
    private static String dateCheckout;
    private static String room;
    private static String tamu;
    private Bundle bundle;
    private ArrayList<Room> rooms = new ArrayList<>();
    private String url;
    private static Room roomObject;
    private static Breadcrumb breadcrumb;
    private static SearchQueriesHotel searchQueriesHotel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("ind", "IDN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_hotel);
        ButterKnife.bind(this);
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));


        getValue();
        setValue();
    }

    private void getValue() {
        bundle = getIntent().getExtras();
        url = bundle.getString(CommonConstants.BOOKURI);
        rooms = RoomHotelActivity.getRooms();
        roomObject = rooms.get(bundle.getInt(CommonConstants.POSITION));
        breadcrumb = ProfileHotelActivity.getBreadcrumb();
        searchQueriesHotel = ListHotelActivity.getSearchQueriesHotel();
        dateCheckin = HotelActivity.getDateCheckin();
        dateCheckout = HotelActivity.getDateCheckout();
        room = HotelActivity.getRoom();
        tamu = HotelActivity.getTamu();
    }

    private void setValue() {
        ((TextView) findViewById(R.id.tv_name_hotel)).setText(breadcrumb.businessName);
        ((TextView) findViewById(R.id.tv_location_hotel)).setText(breadcrumb.areaName);
        try {
            ((TextView) findViewById(R.id.tv_date_checkin)).setText(dateFormatter.format(dateFormat.parse(dateCheckin)));
            ((TextView) findViewById(R.id.tv_date_checkout)).setText(dateFormatter.format(dateFormat.parse(dateCheckout)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.tv_count_tamu)).setText(tamu +" Tamu");
        ((TextView) findViewById(R.id.tv_room_night)).setText(room + " Kamar X " + searchQueriesHotel.night + " Malam");
        ((TextView) findViewById(R.id.tv_adult_price)).setText(" X " + Util.toRupiahFormat(roomObject.price));
        ((TextView) findViewById(R.id.tv_total)).setText(Util.toRupiahFormat(roomObject.price));
        ((TextView) findViewById(R.id.tv_room_name)).setText(roomObject.roomName);

    }

    @OnClick(R.id.next_btn)
    public void onClickPembayaran() {
//        getOrder(url);
        Intent intent = new Intent(this, InfoPassangerActivity.class);
        intent.putExtra(CommonConstants.RESPONE, getString(R.string.hotel_customer_field));
        intent.putExtra(CommonConstants.IS_HOTEL, true);
        intent.putExtra(CommonConstants.BOOKURI, url);
        startActivity(intent);
    }

    private void getOrder(String url) {

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
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
                Log.e("JSON HOTEL", response.toString() + "");

                Intent intent = new Intent(OrderHotelActivity.this, ListOrderActivity.class);
                intent.putExtra(CommonConstants.WHAT_ORDER, "HOTEL");
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(OrderHotelActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON OrderHotel", errorResponse + "");
                ErrorException.getError(OrderHotelActivity.this, errorResponse);
            }
        });
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

    public static Breadcrumb getBreadcrumb() {
        return breadcrumb;
    }

    public static Room getRoomObject() {
        return roomObject;
    }

    public static SearchQueriesHotel getSearchQueriesHotel() {
        return searchQueriesHotel;
    }


    public static String getDateCheckin() {
        return dateCheckin;
    }

    public static String getDateCheckout() {
        return dateCheckout;
    }

    public static String getRoom() {
        return room;
    }

    public static String getTamu() {
        return tamu;
    }
}
