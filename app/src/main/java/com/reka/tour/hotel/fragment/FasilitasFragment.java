package com.reka.tour.hotel.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reka.tour.R;


public class FasilitasFragment extends Fragment {

    private String LOG_TAG = FasilitasFragment.class.getName();

    public static FasilitasFragment newInstance() {
        return new FasilitasFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fasilitas, container, false);


        return view;
    }
}