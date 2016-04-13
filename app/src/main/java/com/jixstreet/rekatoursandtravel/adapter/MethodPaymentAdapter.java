package com.jixstreet.rekatoursandtravel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.model.MethodPayment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class MethodPaymentAdapter extends ArrayAdapter<MethodPayment> {

    private int layoutResourceId;
    private Context context;
    private ViewHolder holder;

    public MethodPaymentAdapter(Context context, ArrayList<MethodPayment> scheduleArrayList) {
        super(context, R.layout.item_method_payment, scheduleArrayList);
        this.context = context;
        this.layoutResourceId = R.layout.item_method_payment;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final MethodPayment methodPayment = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMethodPayment.setText(methodPayment.text);
        if(!methodPayment.message.equals("")){
            holder.tvMessagge.setVisibility(View.VISIBLE);
            holder.tvMessagge.setText(methodPayment.message);
        }
        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.tv_method_payment) TextView tvMethodPayment;
        @Bind(R.id.tv_messagge) TextView tvMessagge;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}