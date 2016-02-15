package com.reka.tour.hotel.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.reka.tour.R;
import com.reka.tour.hotel.adapter.FotoAdapter;
import com.reka.tour.model.Foto;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FotoFragment extends Fragment {

    @Bind(R.id.rv_foto)
    GridView gvFoto;

    private FotoAdapter fotoAdapter;
    private ArrayList<Foto> fotos;

    public static FotoFragment newInstance() {
        return new FotoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foto, container, false);

        ButterKnife.bind(this, view);

        fotos = new ArrayList(30);
        for (int i = 0; i < 30; i++) {
            fotos.add(new Foto(i + "", String.valueOf(R.drawable.bg_sample)));
        }

        fotoAdapter = new FotoAdapter(getActivity(), fotos);
        gvFoto.setAdapter(fotoAdapter);
        gvFoto.setNumColumns(2);

        return view;
    }
}