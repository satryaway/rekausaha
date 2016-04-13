package com.jixstreet.rekatoursandtravel.hotel.activity;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.hotel.adapter.HotelAreaAdapter;
import com.jixstreet.rekatoursandtravel.hotel.model.HotelArea;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;
import com.jixstreet.rekatoursandtravel.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChooserHotelActivity extends AppCompatActivity {
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.lv_hotel_area)
    ExpandableStickyListHeadersListView lvHotelArea;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    HotelAreaAdapter hotelAreaAdapter;
    WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<>();
    private List<HotelArea> hotelAreas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_chooser);

        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setCallBack();

        String locale = getResources().getConfiguration().locale.getCountry();
        Log.e("locale",locale);
        getData("id", false);
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

    private void setCallBack() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    getData(s.toString(), true);
                }
            }
        });

        lvHotelArea.setAnimExecutor(new AnimationExecutor());
        hotelAreaAdapter = new HotelAreaAdapter(this);
        lvHotelArea.setAdapter(hotelAreaAdapter);
        lvHotelArea.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if (lvHotelArea.isHeaderCollapsed(headerId)) {
                    lvHotelArea.expand(headerId);
                } else {
                    lvHotelArea.collapse(headerId);
                }
            }
        });
        lvHotelArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HotelArea hotelArea = hotelAreaAdapter.getHotelArea(position);
                Intent myIntent = new Intent();
                myIntent.putExtra(CommonConstants.LABEL_HOTEL_AREA, hotelArea.label);
                myIntent.putExtra(CommonConstants.VALUE_HOTEL_AREA, hotelArea.value);
                setResult(RESULT_OK, myIntent);
                finish();
                Util.hideKeyboard(ChooserHotelActivity.this);

            }
        });
    }

    private void getData(String keywords, boolean bool) {
        String url = CommonConstants.BASE_URL + "search/autocomplete/hotel";

        RequestParams requestParams = new RequestParams();
        if (bool) {
            requestParams.put(CommonConstants.Q, keywords);
        } else {
            url = CommonConstants.BASE_URL + "search/search_area";
            requestParams.put(CommonConstants.UID, "country:" + keywords);
        }

        requestParams.put(CommonConstants.TOKEN, "19d0ceaca45f9ee27e3c51df52786f1d904280f9");
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
        client.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    if (hotelAreas != null) {
                        hotelAreas.clear();
                        hotelAreas = new ArrayList<>();
                        hotelAreaAdapter.removeContent(hotelAreas);
                    }

                    JSONArray hotelAreaArray = response.getJSONObject(CommonConstants.RESULTS).getJSONArray(CommonConstants.RESULT);
                    for (int i = 0; i < hotelAreaArray.length(); i++) {
                        Gson gson = new Gson();
                        hotelAreas.add(gson.fromJson(hotelAreaArray.getJSONObject(i).toString(), HotelArea.class));
                    }

                    hotelAreaAdapter.updateContent(hotelAreas);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ChooserHotelActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON ChooserHotel", errorResponse + "");
                ErrorException.getError(ChooserHotelActivity.this, errorResponse);
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
}
