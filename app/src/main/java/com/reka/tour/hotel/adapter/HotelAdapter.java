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
import com.reka.tour.hotel.model.Hotel;
import com.reka.tour.utils.Util;
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

        holder.ivPromo.setVisibility(View.GONE);

        Picasso.with(getContext()).load(hotel.photoPrimary).error(R.drawable.ic_fly_to).into(holder.ivImageHotel);

        holder.rbStarHotel.setRating(Float.parseFloat(hotel.star_rating));
        holder.rbStarHotel.setIsIndicator(true);

//        holder.rbStarHotel.getProgressDrawable().setColorFilter(Color.parseColor("#f7961f"), PorterDuff.Mode.SRC_ATOP);
//        holder.rbStarHotel.getBackground().setColorFilter(Color.parseColor("#eedd8b"), PorterDuff.Mode.SRC_ATOP);

        if (hotel.rating != "") {
            holder.tvRating.setVisibility(View.VISIBLE);
            holder.tvRating.setText(hotel.rating);
        }

        holder.tvNameHotel.setText(hotel.name);

        if (hotel.price != "") {
            holder.tvPriceHotel.setText(Util.toRupiahFormat(hotel.price));
            if (hotel.oldprice != "")
                holder.tvOldPriceHotel.setText(Util.toRupiahFormat(hotel.oldprice));
        } else {
            holder.tvPriceHotel.setText("");
            holder.tvOldPriceHotel.setText("");
        }




        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.iv_image_hotel)
        ImageView ivImageHotel;
        @Bind(R.id.tv_rating)
        TextView tvRating;
        @Bind(R.id.rb_star_hotel)
        RatingBar rbStarHotel;
        @Bind(R.id.tv_name_hotel)
        TextView tvNameHotel;
        @Bind(R.id.tv_price_hotel)
        TextView tvPriceHotel;
        @Bind(R.id.tv_old_price_hotel)
        TextView tvOldPriceHotel;
        @Bind(R.id.iv_promo)
        ImageView ivPromo;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}