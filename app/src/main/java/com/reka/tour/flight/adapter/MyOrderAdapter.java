package com.reka.tour.flight.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.model.MyOrder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class MyOrderAdapter extends ArrayAdapter<MyOrder> {

    private int layoutResourceId;
    private Context context;
    private ViewHolder holder;

    public MyOrderAdapter(Context context, ArrayList<MyOrder> scheduleArrayList) {
        super(context, R.layout.item_myorder, scheduleArrayList);
        this.context = context;
        this.layoutResourceId = R.layout.item_myorder;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyOrder myOrder = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvOrderId.setText(myOrder.orderDetailId);
        holder.tvOrderExpire.setText(myOrder.orderExpireDatetime);
        holder.tvOrderName.setText(myOrder.orderName);
        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.tv_order_id)
        TextView tvOrderId;
        @Bind(R.id.tv_order_expire)
        TextView tvOrderExpire;
        @Bind(R.id.tv_order_name)
        TextView tvOrderName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}