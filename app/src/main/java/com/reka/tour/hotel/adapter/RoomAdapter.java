package com.reka.tour.hotel.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.hotel.activity.OrderHotelActivity;
import com.reka.tour.hotel.model.Room;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class RoomAdapter extends ArrayAdapter<Room> {
    private ViewHolder holder;

    private int layoutResourceId;
    private Context context;

    public RoomAdapter(Context context, ArrayList<Room> passangers) {
        super(context, R.layout.item_room, passangers);
        this.context = context;
        this.layoutResourceId = R.layout.item_room;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Room room = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            holder.tvJumlahKamar.setText(room.getCount());
            holder.ivPromo.setVisibility(View.GONE);

        }

        holder.tvNameRoom.setText(room.getName());
        holder.tvPriceRoom.setText(room.getPrice());

        setCallBack();
        return convertView;
    }

    private void setCallBack() {
        holder.ivinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.dialog_info);
                ImageView dialogClose = (ImageView) dialog.findViewById(R.id.iv_close);
                // if button is clicked, close the custom dialog
                dialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        holder.tvorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSortir = new Intent(getContext(),
                        OrderHotelActivity.class);
                getContext().startActivity(intentSortir);
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.tv_name_room)
        TextView tvNameRoom;
        @Bind(R.id.tv_price_room)
        TextView tvPriceRoom;
        @Bind(R.id.tv_jumlah_kamar)
        TextView tvJumlahKamar;
        @Bind(R.id.iv_promo)
        ImageView ivPromo;
        @Bind(R.id.iv_info)
        ImageView ivinfo;        @Bind(R.id.tv_order)
        TextView tvorder;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}