package com.reka.tour.flight.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.activity.ListOrderActivity;
import com.reka.tour.model.MyOrder;
import com.reka.tour.utils.Util;
import com.squareup.picasso.Picasso;

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
    private ListOrderActivity activity;

    public MyOrderAdapter(Context context, ArrayList<MyOrder> scheduleArrayList, ListOrderActivity activity) {
        super(context, R.layout.item_myorder, scheduleArrayList);
        this.context = context;
        this.layoutResourceId = R.layout.item_myorder;
        this.activity=activity;
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

        holder.tvOrderType.setText(myOrder.orderType.toUpperCase());
        holder.tvOrderName.setText(myOrder.orderName);
        holder.tvOrderNameDetail.setText(myOrder.orderNameDetail);
        holder.tvOrderExpire.setText("Expired until: "+myOrder.orderExpireDatetime);
        holder.ivPriceOrder.setText(Util.toRupiahFormat(myOrder.subtotalAndCharge));

        Picasso.with(getContext()).load(myOrder.orderPhoto).into(holder.ivOrder);

        holder.ivDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deleteOrderId(position);
            }
        });

        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.tv_order_type) TextView tvOrderType;
        @Bind(R.id.tv_order_name) TextView tvOrderName;
        @Bind(R.id.tv_order_name_detail) TextView tvOrderNameDetail;
        @Bind(R.id.tv_order_expire) TextView tvOrderExpire;
        @Bind(R.id.iv_delete_order) ImageView ivDeleteOrder;
        @Bind(R.id.iv_order) ImageView ivOrder;
        @Bind(R.id.tv_order_price) TextView ivPriceOrder;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}