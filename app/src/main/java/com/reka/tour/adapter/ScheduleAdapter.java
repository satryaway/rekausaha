package com.reka.tour.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.model.NearbyGoDate;
import com.reka.tour.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class ScheduleAdapter extends ArrayAdapter<NearbyGoDate> {

    private int layoutResourceId;
    private Context context;
    private ViewHolder holder;

    public ScheduleAdapter(Context context, ArrayList<NearbyGoDate> scheduleArrayList) {
        super(context, R.layout.item_schedule, scheduleArrayList);
        this.context = context;
        this.layoutResourceId = R.layout.item_schedule;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final NearbyGoDate schedule = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


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

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayFormatter = new SimpleDateFormat("EEE", new Locale("ind", "IDN"));
        SimpleDateFormat dateMonthFormatter = new SimpleDateFormat("dd MMM", new Locale("ind", "IDN"));


        try {
            holder.tvDay.setText(dayFormatter.format(format1.parse(schedule.date)) + "");
            holder.tvDate.setText(dateMonthFormatter.format(format1.parse(schedule.date)) + "");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(schedule.price!=null){
            holder.tvPrice.setText(Util.toRupiahFormat(schedule.price));
        }
        return convertView;
    }

    public void onSelect(int position) {
        holder.tvDay.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
    }

    static class ViewHolder {
        TextView tvDay;
        TextView tvDate;
        TextView tvPrice;
    }
}