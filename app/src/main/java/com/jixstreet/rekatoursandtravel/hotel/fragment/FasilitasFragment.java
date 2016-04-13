package com.jixstreet.rekatoursandtravel.hotel.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.hotel.activity.ProfileHotelActivity;
import com.jixstreet.rekatoursandtravel.hotel.adapter.FasilitasAdapter;
import com.jixstreet.rekatoursandtravel.hotel.model.Facilitiy;
import com.jixstreet.rekatoursandtravel.hotel.model.General;
import com.jixstreet.rekatoursandtravel.views.ExpandableHeightGridView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FasilitasFragment extends Fragment {

    @Bind(R.id.gv_fasilitas)
    ExpandableHeightGridView gvFasilitas;
    @Bind(R.id.tv_description)
    TextView tvDescription;

    private General general;
    private ArrayList<Facilitiy> facilitiys = new ArrayList<>();
    private FasilitasAdapter fasilitasAdapter;

    public static FasilitasFragment newInstance() {
        return new FasilitasFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        general = ((ProfileHotelActivity) getActivity()).getGeneral();
        facilitiys = ((ProfileHotelActivity) getActivity()).getFacilitiys();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fasilitas, container, false);

        ButterKnife.bind(this, view);

        String description = general.description;

        try {
            description = URLDecoder.decode(description, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        tvDescription.setText(description);
        fasilitasAdapter = new FasilitasAdapter(getActivity(), facilitiys);
        gvFasilitas.setAdapter(fasilitasAdapter);
        gvFasilitas.setNumColumns(2);
        gvFasilitas.setExpanded(true);
        return view;
    }
}