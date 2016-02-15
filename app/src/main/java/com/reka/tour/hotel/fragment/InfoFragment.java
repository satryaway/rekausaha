package com.reka.tour.hotel.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.reka.tour.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class InfoFragment extends Fragment {

    @Bind(R.id.rb_bintang)
    RatingBar rbBintang;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        ButterKnife.bind(this, view);

        setInit();


        return view;
    }

    private void setInit() {
        Drawable drawable = rbBintang.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#f7961f"), PorterDuff.Mode.SRC_ATOP);

    }
}