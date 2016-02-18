package com.reka.tour.hotel.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.hotel.model.Breadcrumb;
import com.reka.tour.hotel.model.Room;
import com.reka.tour.hotel.model.SearchQueriesHotel;
import com.reka.tour.utils.CommonConstants;
import com.reka.tour.utils.Util;

import org.json.JSONException;
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
    private Room roomObject;
    private Breadcrumb breadcrumb;
    private SearchQueriesHotel searchQueriesHotel;
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

        ((TextView) findViewById(R.id.tv_room_night)).setText(room + " Kamar X " + searchQueriesHotel.night + " Malam");
        ((TextView) findViewById(R.id.tv_adult_price)).setText(" X " + Util.toRupiahFormat(roomObject.price));
        ((TextView) findViewById(R.id.tv_total)).setText(Util.toRupiahFormat(roomObject.price));
        ((TextView) findViewById(R.id.tv_room_name)).setText(roomObject.roomName);

    }

    @OnClick(R.id.tv_next)
    public void onClickPembayaran() {
        getOrder(url);
    }

    private void getOrder(String url) {

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, "19d0ceaca45f9ee27e3c51df52786f1d904280f9");
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
                Log.e("JSON FLIGHT", response.toString() + "");


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(OrderHotelActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(OrderHotelActivity.this, errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


}
