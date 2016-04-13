package com.jixstreet.rekatoursandtravel.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixstreet.rekatoursandtravel.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HeaderAdapter extends PagerAdapter {
    @Bind(R.id.image_header)
    ImageView imageHeader;
    @Bind(R.id.image_caption)
    TextView imageCaption;

    private Context context;
    private int[] HEADERS = {
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
    };
    private String[] HEADERCAPTION = {
            "Bromo",
            "Raja Ampat",
            "Ubud"
    };

    public HeaderAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return HEADERS.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        ButterKnife.bind(this, view);

        Picasso.with(context).load(HEADERS[position]).into(imageHeader);
        imageCaption.setText(HEADERCAPTION[position]);

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}