package com.jixstreet.rekatoursandtravel.hotel.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.hotel.activity.ProfileHotelActivity;
import com.jixstreet.rekatoursandtravel.hotel.model.Breadcrumb;
import com.jixstreet.rekatoursandtravel.hotel.model.General;

import butterknife.Bind;
import butterknife.ButterKnife;


public class InfoFragment extends Fragment {

    @Bind(R.id.rb_bintang)
    RatingBar rbBintang;
    @Bind(R.id.tv_hotel_location)
    TextView tvHotelLocation;
    GoogleMap mMap;

    private Breadcrumb breadcrumb;
    private General general;
    private View view;
    private FragmentManager fm;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        breadcrumb = ((ProfileHotelActivity) getActivity()).getBreadcrumb();
        general = ((ProfileHotelActivity) getActivity()).getGeneral();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }

        view = inflater.inflate(R.layout.fragment_info, container, false);

//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
            fragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    LatLng sydney = new LatLng(general.latitude, general.longitude);
                    mMap.addMarker(new MarkerOptions().position(sydney));
                }
            });
        }

        fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng sydney = new LatLng(general.latitude, general.longitude);
                mMap.addMarker(new MarkerOptions().position(sydney).title(general.description));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(general.latitude,
                        general.longitude), 16));
            }
        });

        ButterKnife.bind(this, view);

        setInit();
        setValue();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (fm.findFragmentById(R.id.map));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setInit() {
        /*Drawable drawable = rbBintang.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#f7961f"), PorterDuff.Mode.SRC_ATOP);*/

    }

    private void setValue() {
        rbBintang.setRating(Float.parseFloat(breadcrumb.starRating));
        tvHotelLocation.setText(general.address);
    }
}