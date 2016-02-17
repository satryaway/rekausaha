package com.reka.tour.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.model.Steps;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class StepsAdapter extends ArrayAdapter<Steps> {

    private int layoutResourceId;
    private Context context;
    private ViewHolder holder;

    public StepsAdapter(Context context, ArrayList<Steps> scheduleArrayList) {
        super(context, R.layout.item_steps, scheduleArrayList);
        this.context = context;
        this.layoutResourceId = R.layout.item_steps;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Steps methodPayment = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(methodPayment.name);

        String step = "";
        for (int i = 0; i < methodPayment.step.length; i++) {
            step += "- " + methodPayment.step[i] + "\n";
        }

        holder.tvStep.setText(step);
        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.tv_step)
        TextView tvStep;
        @Bind(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}