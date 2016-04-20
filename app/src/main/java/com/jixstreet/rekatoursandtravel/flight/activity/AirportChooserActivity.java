package com.jixstreet.rekatoursandtravel.flight.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.flight.adapter.AirportListAdapter;
import com.jixstreet.rekatoursandtravel.flight.model.Airport;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import cz.msebera.android.httpclient.Header;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @author satryaway
 */
public class AirportChooserActivity extends AppCompatActivity {
    AirportListAdapter mAirportListAdapter;
    WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<>();
    private List<Airport> airportList = new ArrayList<>();
    private ExpandableStickyListHeadersListView airportLV;
    private EditText filterAirportET;
    private Toolbar toolbar;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airport_chooser_layout);

        getData();
        initUI();
        setCallBack();
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

    private void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle(getIntent().getExtras().getString("title"));
        setSupportActionBar(toolbar);

        filterAirportET = (EditText) findViewById(R.id.filter_et);
        airportLV = (ExpandableStickyListHeadersListView) findViewById(R.id.airport_lv);
        airportLV.setAnimExecutor(new AnimationExecutor());
        mAirportListAdapter = new AirportListAdapter(this);
        airportLV.setAdapter(mAirportListAdapter);
        airportLV.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if (airportLV.isHeaderCollapsed(headerId)) {
                    airportLV.expand(headerId);
                } else {
                    airportLV.collapse(headerId);
                }
            }
        });
        airportLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Airport airport = mAirportListAdapter.getAirport(position);

                if (getIntent().getExtras().getString(CommonConstants.FLIGHT).equals(CommonConstants.DEPARTURE)) {
                    Intent myIntent = new Intent();
                    myIntent.putExtra(CommonConstants.AIRPORT_CODE_D, airport.code);
                    myIntent.putExtra(CommonConstants.AIRPORT_NAME_D, airport.name);
                    myIntent.putExtra(CommonConstants.AIRPORT_LOCATION_D, airport.locationName);
                    setResult(RESULT_OK, myIntent);
                    finish();
                } else if (getIntent().getExtras().getString(CommonConstants.FLIGHT).equals(CommonConstants.APERTURE)) {
                    Intent myIntent = new Intent();
                    myIntent.putExtra(CommonConstants.AIRPORT_CODE_A, airport.code);
                    myIntent.putExtra(CommonConstants.AIRPORT_NAME_A, airport.name);
                    myIntent.putExtra(CommonConstants.AIRPORT_LOCATION_A, airport.locationName);
                    setResult(RESULT_OK, myIntent);
                    finish();
                }


            }
        });
    }

    private void setCallBack() {
        filterAirportET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mAirportListAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getData() {
        String url = CommonConstants.BASE_URL + "flight_api/all_airport";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

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
                try {
                    JSONArray airportArrayList = response.getJSONObject(CommonConstants.ALL_AIRPORT).getJSONArray(CommonConstants.AIRPORT);
                    for (int i = 0; i < airportArrayList.length(); i++) {
                        Gson gson = new Gson();
                        airportList.add(gson.fromJson(airportArrayList.getJSONObject(i).toString(), Airport.class));
                    }

                    mAirportListAdapter.updateContent(airportList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(AirportChooserActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON AirportChooser", errorResponse + "");
                ErrorException.getError(AirportChooserActivity.this, errorResponse);
                showDialog();
            }
        });
    }

    //animation executor
    class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

        @Override
        public void executeAnim(final View target, final int animType) {
            if (ExpandableStickyListHeadersListView.ANIMATION_EXPAND == animType && target.getVisibility() == View.VISIBLE) {
                return;
            }
            if (ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE == animType && target.getVisibility() != View.VISIBLE) {
                return;
            }
            if (mOriginalViewHeightPool.get(target) == null) {
                mOriginalViewHeightPool.put(target, target.getHeight());
            }
            final int viewHeight = mOriginalViewHeightPool.get(target);
            float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
            float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
            final ViewGroup.LayoutParams lp = target.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
            animator.setDuration(200);
            target.setVisibility(View.VISIBLE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                        target.setVisibility(View.VISIBLE);
                    } else {
                        target.setVisibility(View.GONE);
                    }
                    target.getLayoutParams().height = viewHeight;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    target.setLayoutParams(lp);
                    target.requestLayout();
                }
            });
            animator.start();

        }
    }

    private void showDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(
                AirportChooserActivity.this).create();

        alertDialog.setTitle("Connection Problem");

        // Setting Dialog Message
        alertDialog.setMessage("Please try again");

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                getData();
                initUI();
                setCallBack();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
