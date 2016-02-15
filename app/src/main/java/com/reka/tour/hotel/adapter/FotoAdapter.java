package com.reka.tour.hotel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.reka.tour.R;
import com.reka.tour.model.Foto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fachrifebrian on 9/2/15.
 */
public class FotoAdapter extends ArrayAdapter<Foto> {

    int itemLayout;
    private Context context;

    public FotoAdapter(Context context, ArrayList<Foto> items) {
        super(context, R.layout.item_foto, items);
        this.itemLayout = R.layout.item_foto;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Foto foto = getItem(position);

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(itemLayout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(foto.getImageName())
                .error(R.drawable.bg_sample)
                .into(holder.ivFoto);

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_foto)
        ImageView ivFoto;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}