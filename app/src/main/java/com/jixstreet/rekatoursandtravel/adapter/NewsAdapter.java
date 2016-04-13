package com.jixstreet.rekatoursandtravel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.model.News;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fachrifebrian on 9/2/15.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    private int layoutResourceId;
    private Context context;


    public NewsAdapter(Context context, ArrayList<News> items) {
        super(context, R.layout.item_news, items);
        this.layoutResourceId = R.layout.item_news;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final News rowItem = getItem(position);

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleNews.setText(rowItem.getTitleNews());
        holder.descNews.setText(rowItem.getDescNews());


        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.title_news)
        TextView titleNews;
        @Bind(R.id.desc_news)
        TextView descNews;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}