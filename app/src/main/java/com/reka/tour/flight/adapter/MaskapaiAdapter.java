package com.reka.tour.flight.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.reka.tour.R;
import com.reka.tour.flight.model.Maskapai;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class MaskapaiAdapter extends ArrayAdapter<Maskapai> {

    private int layoutResourceId;
    private Context context;

    public MaskapaiAdapter(Context context, ArrayList<Maskapai> passangers) {
        super(context, R.layout.item_maskapai, passangers);
        this.context = context;
        this.layoutResourceId = R.layout.item_maskapai;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Maskapai maskapai = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cbMaskapai.setChecked(maskapai.getStatus());
        holder.cbMaskapai.setText(maskapai.getAirplane());

        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.cb_maskapai)
        CheckBox cbMaskapai;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}