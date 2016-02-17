package com.reka.tour.hotel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.hotel.model.Facilitiy;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fachrifebrian on 9/2/15.
 */
public class FasilitasAdapter extends ArrayAdapter<Facilitiy> {

    int itemLayout;
    private Context context;

    public FasilitasAdapter(Context context, ArrayList<Facilitiy> items) {
        super(context, R.layout.item_facilitiy, items);
        this.itemLayout = R.layout.item_facilitiy;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Facilitiy facilitiy = getItem(position);

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(itemLayout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvFacilitiy.setText("✓ " + facilitiy.facilityType + " " + facilitiy.facilityName);

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_facilitiy)
        TextView tvFacilitiy;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}