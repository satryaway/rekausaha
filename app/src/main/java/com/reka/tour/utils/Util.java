package com.reka.tour.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by fachrifebrian on 2/4/16.
 */
public class Util {
    public static String toRupiahFormat(String nominal) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatIDR = new DecimalFormatSymbols();

        formatIDR.setCurrencySymbol("IDR ");
        formatIDR.setMonetaryDecimalSeparator(',');
        formatIDR.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatIDR);
        kursIndonesia.format(Double.parseDouble(nominal));

        String result = kursIndonesia.format(Double.parseDouble(nominal)).substring(4);

        return "IDR " + result.substring(0, result.lastIndexOf(','));
    }

    public static void setListview(ListView listMaskapai) {
        listMaskapai.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setListViewHeightBasedOnChildren(listMaskapai);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
