package com.jixstreet.rekatoursandtravel.hotel.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.jixstreet.rekatoursandtravel.hotel.adapter.HotelAdapter;
import com.jixstreet.rekatoursandtravel.hotel.model.Hotel;
import com.jixstreet.rekatoursandtravel.hotel.model.SearchQueriesHotel;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ListHotelActivity extends AppCompatActivity {
    private static SearchQueriesHotel searchQueriesHotel;
    private final int HOTEL_SORTIR = 110;
    private final int HOTEL_FILTER = 111;
    @Bind(R.id.list_hotel)
    ListView listHotel;
    @Bind(R.id.tv_hotel_area)
    TextView tvHotelArea;
    int FILTER_MAX_BINTANG = 0;
    int FILTER_MIN_BINTANG = 0;
    double FILTER_MAX_PRICE = 0;
    double FILTER_MIN_PRICE = 0;
    private HotelAdapter hotelAdapter;
    private ArrayList<Hotel> hotels = new ArrayList<>();
    private Bundle bundle;
    private double MAX_PRICE = 0;
    private String TYPE_SORT;

    public static SearchQueriesHotel getSearchQueriesHotel() {
        return searchQueriesHotel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hotel);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setValue();
        setCallBack();
        getData("popular");
    }

    private void setCallBack() {
        listHotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (hotels.get(position).price.equals("")){
                    Toast.makeText(ListHotelActivity.this, "Invalid price", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ListHotelActivity.this,
                        ProfileHotelActivity.class);
                intent.putExtra(CommonConstants.BUSSINESSURI, hotels.get(position).businessUri);
                intent.putExtra(CommonConstants.PRICE_START, hotels.get(position).price);
                startActivity(intent);
            }
        });
    }

    private void setValue() {
        bundle = getIntent().getExtras();
        tvHotelArea.setText(bundle.getString(CommonConstants.Q));
    }

    private void getData(String sort) {
        Log.e("SORT", sort);

        TYPE_SORT = sort;

        String url = CommonConstants.BASE_URL + "search/hotel";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.Q, bundle.getString(CommonConstants.Q));
        requestParams.put(CommonConstants.STARTDATE, bundle.getString(CommonConstants.STARTDATE));
        requestParams.put(CommonConstants.ENDDATE, bundle.getString(CommonConstants.ENDDATE));
        requestParams.put(CommonConstants.ROOM, bundle.getString(CommonConstants.ROOM));
        requestParams.put(CommonConstants.SORT, sort);
        requestParams.put(CommonConstants.ADULT, bundle.getString(CommonConstants.ADULT));
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        requestParams.put(CommonConstants.MINPRICE, String.valueOf((int) FILTER_MIN_PRICE));
        requestParams.put(CommonConstants.MAXPRICE, String.valueOf((int) FILTER_MAX_PRICE));
        requestParams.put(CommonConstants.MINSTAR, String.valueOf(FILTER_MIN_BINTANG));
        requestParams.put(CommonConstants.MAXSTAR, String.valueOf(FILTER_MAX_BINTANG));

        Log.e("FILTER", FILTER_MAX_BINTANG + " " +
                FILTER_MIN_BINTANG + " " +
                FILTER_MAX_PRICE + " " +
                FILTER_MIN_PRICE + " ");

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
                try {

                    Log.e("hotels.size", hotels.size() + "");

                    if (hotels.size() > 0) {
                        hotels.clear();
                        hotels = new ArrayList<>();
                        hotelAdapter.notifyDataSetChanged();
                    }

                    Gson gson = new Gson();
                    JSONArray hotelAreaArray = response.getJSONObject(CommonConstants.RESULTS).getJSONArray(CommonConstants.RESULT);
                    for (int i = 0; i < hotelAreaArray.length(); i++) {
                        hotels.add(gson.fromJson(hotelAreaArray.getJSONObject(i).toString(), Hotel.class));
                    }

                    JSONObject searchQueries = response.getJSONObject(CommonConstants.SEARCH_QUERIES);
                    searchQueriesHotel = gson.fromJson(searchQueries.toString(), SearchQueriesHotel.class);

                    for (int i = 0; i < hotels.size(); i++) {
                        if (hotels.get(i).price != "") {
                            if (Double.parseDouble(hotels.get(i).price) > MAX_PRICE) {
                                MAX_PRICE = Double.parseDouble(hotels.get(i).price);
                            }
                        }
                    }

                    hotelAdapter = new HotelAdapter(ListHotelActivity.this, hotels);
                    listHotel.setAdapter(hotelAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ListHotelActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON ListHotelActivity", errorResponse + "");
                ErrorException.getError(ListHotelActivity.this, errorResponse);
            }
        });
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
                startActivityForResult(intentSortir, HOTEL_SORTIR);
                break;
            case R.id.action_filter:
                Intent intentFilter = new Intent(ListHotelActivity.this,
                        FilterHotelActivity.class);
                intentFilter.putExtra(CommonConstants.MAX_PRICE, MAX_PRICE);
                startActivityForResult(intentFilter, HOTEL_FILTER);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == HOTEL_SORTIR) {

                Boolean SORTIR_TERPOPULER = data.getBooleanExtra(CommonConstants.SORTIR_TERPOPULER, false);
                Boolean SORTIR_HARGA_TERMAHAL = data.getBooleanExtra(CommonConstants.SORTIR_HARGA_TERMAHAL, false);
                Boolean SORTIR_HARGA_TERMURAH = data.getBooleanExtra(CommonConstants.SORTIR_HARGA_TERMURAH, false);
                Boolean SORTIR_RATING_TERTINGGI = data.getBooleanExtra(CommonConstants.SORTIR_RATING_TERTINGGI, false);
                Boolean SORTIR_RATING_TERENDAH = data.getBooleanExtra(CommonConstants.SORTIR_RATING_TERENDAH, false);

                if (SORTIR_TERPOPULER) {
                    getData("popular");
                } else if (SORTIR_HARGA_TERMAHAL) {
                    getData("pricedesc");
                } else if (SORTIR_HARGA_TERMURAH) {
                    getData("priceasc");
                } else if (SORTIR_RATING_TERTINGGI) {
                    getData("stardesc");
                } else if (SORTIR_RATING_TERENDAH) {
                    getData("starasc");
                }

            }

            if (requestCode == HOTEL_FILTER) {
                FILTER_MAX_BINTANG = data.getIntExtra(CommonConstants.FILTER_MAX_BINTANG, 0);
                FILTER_MIN_BINTANG = data.getIntExtra(CommonConstants.FILTER_MIN_BINTANG, 0);
                FILTER_MAX_PRICE = data.getDoubleExtra(CommonConstants.FILTER_MAX_PRICE, 0);
                FILTER_MIN_PRICE = data.getDoubleExtra(CommonConstants.FILTER_MIN_PRICE, 0);

                Log.e("FILTER", FILTER_MAX_BINTANG + " " +
                        FILTER_MIN_BINTANG + " " +
                        FILTER_MAX_PRICE + " " +
                        FILTER_MIN_PRICE + " ");

                getData(TYPE_SORT);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
