package com.reka.tour.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.model.Schedule;

import java.util.ArrayList;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class ScheduleAdapter extends ArrayAdapter<Schedule> {

    private int layoutResourceId;
    private Context context;


    public ScheduleAdapter(Context context, ArrayList<Schedule> scheduleArrayList) {
        super(context, R.layout.item_schedule, scheduleArrayList);
        this.context = context;
        this.layoutResourceId = R.layout.item_schedule;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Schedule schedule = getItem(position);

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

        holder.tvDay = (TextView) convertView.findViewById(R.id.tv_day);
        holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
        holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

        holder.tvDay.setText(schedule.getDay());
        holder.tvDate.setText(schedule.getDate());
        holder.tvPrice.setText(schedule.getPrice());

        return convertView;
    }

    static class ViewHolder {
        TextView tvDay;
        TextView tvDate;
        TextView tvPrice;
    }
}