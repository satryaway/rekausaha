package com.reka.tour.hotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reka.tour.R;

public class FasilitasDialogAdapter extends BaseAdapter {
    private final String[] facilitiy;
    private Context context;

    public FasilitasDialogAdapter(Context context, String[] facilitiy) {
        this.context = context;
        this.facilitiy = facilitiy;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;
        if (convertView == null) {
            view = new View(context);
            view = inflater.inflate(R.layout.item_facilitiy, null);
            TextView tvFacilitiy = (TextView) view.findViewById(R.id.tv_facilitiy);
            tvFacilitiy.setText("✓ " + facilitiy[position]);
        } else {
            view = convertView;
        }

        return view;
    }

    @Override
    public int getCount() {
        return facilitiy.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}