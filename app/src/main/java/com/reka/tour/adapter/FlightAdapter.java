package com.reka.tour.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.model.Departures;
import com.reka.tour.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class FlightAdapter extends ArrayAdapter<Departures> {

    private int layoutResourceId;
    private Context context;

    public FlightAdapter(Context context, ArrayList<Departures> flightArrayList) {
        super(context, R.layout.item_flight, flightArrayList);
        this.context = context;
        this.layoutResourceId = R.layout.item_flight;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Departures schedule = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivFlight = (ImageView) convertView.findViewById(R.id.iv_flight);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
        holder.tvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
        holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

        Picasso.with(getContext()).load(schedule.image).into(holder.ivFlight);

        holder.tvName.setText(schedule.airlinesName);
        holder.tvTime.setText(schedule.simpleDepartureTime +" - "+schedule.simpleArrivalTime);
        holder.tvDuration.setText(schedule.duration+", "+schedule.stop);
        holder.tvPrice.setText(Util.toRupiahFormat(schedule.priceAdult));

        return convertView;
    }

    static class ViewHolder {
        ImageView ivFlight;
        TextView tvName;
        TextView tvTime;
        TextView tvDuration;
        TextView tvPrice;
    }
}