package com.reka.tour.hotel.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.hotel.model.Room;
import com.reka.tour.utils.Util;
import com.reka.tour.views.ExpandableHeightGridView;
import com.squareup.picasso.Picasso;

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
    private Room room;
    private int position;

    public RoomAdapter(Context context, ArrayList<Room> rooms) {
        super(context, R.layout.item_room, rooms);
        this.context = context;
        this.layoutResourceId = R.layout.item_room;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        this.position=position;
        room = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivPromo.setVisibility(View.GONE);
        holder.tvNameRoom.setText(room.roomName);
        holder.tvPriceRoom.setText(Util.toRupiahFormat(room.price));
        holder.tvJumlahKamar.setText("Jumlah kamar : " + room.roomAvailable);

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

                setInitDialog(dialog);

                dialog.show();

            }
        });

//        holder.tvorder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentSortir = new Intent(getContext(),
//                        OrderHotelActivity.class);
//                intentSortir.putExtra(CommonConstants.BOOKURI, room.bookUri);
//                intentSortir.putExtra(CommonConstants.POSITION, position);
//                getContext().startActivity(intentSortir);
//            }
//        });
    }

    private void setInitDialog(final Dialog dialog) {
        ImageView dialogClose = (ImageView) dialog.findViewById(R.id.iv_close);
        TextView tvNameRoom = (TextView) dialog.findViewById(R.id.tv_name_room);
        ImageView ivRoom = (ImageView) dialog.findViewById(R.id.iv_room);
        TextView tvDescription = (TextView) dialog.findViewById(R.id.tv_description);
        ExpandableHeightGridView gvFasilitas = (ExpandableHeightGridView) dialog.findViewById(R.id.gv_fasilitas);

        tvNameRoom.setText(room.roomName);
        tvDescription.setText(room.roomDescription);
        Picasso.with(context).load(room.photoUrl)
                .error(R.drawable.bg_sample)
                .into(ivRoom);

        gvFasilitas.setAdapter(new FasilitasDialogAdapter(getContext(), room.roomFacility));
        gvFasilitas.setNumColumns(2);
        gvFasilitas.setExpanded(true);

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.tv_name_room) TextView tvNameRoom;
        @Bind(R.id.tv_price_room) TextView tvPriceRoom;
        @Bind(R.id.tv_jumlah_kamar) TextView tvJumlahKamar;
        @Bind(R.id.iv_promo) ImageView ivPromo;
        @Bind(R.id.iv_info) ImageView ivinfo;
        @Bind(R.id.tv_order) TextView tvorder;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}