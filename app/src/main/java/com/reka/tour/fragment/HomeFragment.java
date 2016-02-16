package com.reka.tour.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.adapter.HeaderAdapter;
import com.reka.tour.flight.activity.FlightActivity;
import com.reka.tour.hotel.activity.HotelActivity;
import com.reka.tour.views.pagerindicator.AutoScrollViewPager;
import com.reka.tour.views.pagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {


    @Bind(R.id.indicator)
    CirclePageIndicator mIndicator;
    @Bind(R.id.pager)
    AutoScrollViewPager mPager;
    @Bind(R.id.txt_runningtext)
    TextView textViewRunning;
    @Bind(R.id.nav_flight)
    ImageView navFlight;
    @Bind(R.id.nav_hotel)
    ImageView navHotel;

    private String LOG_TAG = HomeFragment.class.getName();

    public static HomeFragment newInstance(Context context) {
//        Bundle args = new Bundle();
////        args.putInt("Columns", columns);
//        SearchSpaFragment fragment = new SearchSpaFragment();
//        fragment.setArguments(args);
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        columns = getArguments().getInt("Columns");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setCallBack();

        HeaderAdapter mAdapter = new HeaderAdapter(getActivity());
        mPager.setAdapter(mAdapter);
        mPager.setInterval(10000);
        mPager.startAutoScroll();
        mIndicator.setViewPager(mPager);

        textViewRunning.setSelected(true);

        return view;
    }

    private void setCallBack() {
        navFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), FlightActivity.class));
            }
        });

        navHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), HotelActivity.class));
            }
        });
    }
}