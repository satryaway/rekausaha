package com.reka.tour.hotel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.model.Hotel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class HotelAdapter extends ArrayAdapter<Hotel> {

    private int layoutResourceId;
    private Context context;

    public HotelAdapter(Context context, ArrayList<Hotel> passangers) {
        super(context, R.layout.item_hotel, passangers);
        this.context = context;
        this.layoutResourceId = R.layout.item_hotel;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Hotel hotel = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position%2==1){
            holder.ivPromo.setVisibility(View.GONE);
        }

        Picasso.with(getContext()).load(R.drawable.ic_fly_to).error(R.drawable.ic_fly_to).into(holder.ivImageHotel);
        holder.rbRateHotel.setRating(Float.parseFloat(hotel.getRating()));
        holder.rbRateHotel.setIsIndicator(true);
        holder.tvNameHotel.setText(hotel.getName());
        holder.tvPriceHotel.setText(hotel.getPrice());

        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.iv_image_hotel)
        ImageView ivImageHotel;
        @Bind(R.id.rb_rate_hotel)
        RatingBar rbRateHotel;
        @Bind(R.id.tv_name_hotel)
        TextView tvNameHotel;
        @Bind(R.id.tv_price_hotel)
        TextView tvPriceHotel;
        @Bind(R.id.iv_promo)
        ImageView ivPromo;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}