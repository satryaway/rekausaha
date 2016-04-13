package com.jixstreet.rekatoursandtravel.flight.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.flight.model.Departures;
import com.jixstreet.rekatoursandtravel.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class FlightAdapter extends ArrayAdapter<Departures> {

    private int layoutResourceId;
    private Context context;
    private ArrayList<Departures> flightArrayList;
    private ArrayList<Departures> arraylist = new ArrayList<>();
    private ArrayList<Departures> arraylist2 = new ArrayList<>();
    private ArrayList<Departures> arraylist3 = new ArrayList<>();

    public FlightAdapter(Context context, ArrayList<Departures> flightArrayList) {
        super(context, R.layout.item_flight, flightArrayList);
        this.context = context;
        this.layoutResourceId = R.layout.item_flight;
        this.flightArrayList = flightArrayList;
        this.arraylist.addAll(flightArrayList);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Departures schedule = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivFlight = (ImageView) convertView.findViewById(R.id.iv_flight);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
        holder.tvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
        holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

        Picasso.with(getContext()).load(schedule.image).into(holder.ivFlight);

        holder.tvName.setText(schedule.airlinesName);
        holder.tvTime.setText(schedule.simpleDepartureTime + " - " + schedule.simpleArrivalTime);
        holder.tvDuration.setText(schedule.duration + ", " + schedule.stop);
        holder.tvPrice.setText(Util.toRupiahFormat(schedule.priceAdult));

        return convertView;
    }

    // Filter Class
    public void filter(String jamText, String hargaText, String opsiText, String maskapaiText) {
        //search maskapaiText
        flightArrayList.clear();
        if (maskapaiText.length() == 0) {
            flightArrayList.addAll(arraylist);
        } else {
            maskapaiText = maskapaiText.toLowerCase(Locale.getDefault());
            maskapaiText = deleteLastComa(maskapaiText);
            String[] maskapaiString = maskapaiText.split(",");

            for (int i = 0; i < maskapaiString.length; i++) {
                for (Departures wp : arraylist) {
                    Log.e("maskapaihasil", wp.airlinesName.toLowerCase(Locale.getDefault()));

                    if (wp.airlinesName.toLowerCase(Locale.getDefault()).contains(maskapaiString[i])) {
                        flightArrayList.add(wp);
                    }
                }
            }
        }

        //sort hargaText
        if (hargaText.length() != 0) {
            hargaText = hargaText.toLowerCase(Locale.getDefault());

            if (hargaText.equals("termahal")) {
                Collections.sort(flightArrayList, new Comparator<Departures>() {
                    @Override
                    public int compare(Departures flight2, Departures flight1) {
                        return Double.compare(Double.parseDouble(flight1.priceAdult), Double.parseDouble(flight2.priceAdult));
                    }
                });
            } else {
                Collections.sort(flightArrayList, new Comparator<Departures>() {
                    @Override
                    public int compare(Departures flight2, Departures flight1) {
                        return Double.compare(Double.parseDouble(flight2.priceAdult), Double.parseDouble(flight1.priceAdult));
                    }
                });
            }
        }

        //sort opsiText
        if (opsiText.length() != 0) {
            arraylist2.addAll(flightArrayList);
            flightArrayList.clear();

            opsiText = opsiText.toLowerCase(Locale.getDefault());
            if (opsiText.equals("langsung")) {
                for (Departures wp : arraylist2) {
                    if (wp.stop.toLowerCase(Locale.getDefault()).contains(opsiText)) {
                        flightArrayList.add(wp);
                    }
                }
            } else {
                for (Departures wp : arraylist2) {
                    if (wp.stop.toLowerCase(Locale.getDefault()).contains(opsiText)) {
                        flightArrayList.add(wp);
                    }
                }
            }
        }

        //sort jamText
        if (jamText.length() != 0) {
            arraylist3.addAll(flightArrayList);
            flightArrayList.clear();

            jamText = jamText.toLowerCase(Locale.getDefault());
            jamText = deleteLastComa(jamText);

            String[] jamString = jamText.split(",");
            for (int i = 0; i < jamString.length; i++) {
                for (Departures wp : arraylist3) {
                    String[] arrival = wp.simpleDepartureTime.split(":");
                    String[] jam = jamString[i].split(":");

                    if (Integer.parseInt(jam[1]) == 3) {
                        if (Integer.parseInt(arrival[0]) > Integer.parseInt(jam[0]) ||
                                Integer.parseInt(arrival[0]) < Integer.parseInt(jam[1])) {
                            flightArrayList.add(wp);
                        }
                    } else {
                        if (Integer.parseInt(arrival[0]) > Integer.parseInt(jam[0]) &&
                                Integer.parseInt(arrival[0]) < Integer.parseInt(jam[1])) {
                            flightArrayList.add(wp);
                        }
                    }

                }
            }
        }
        notifyDataSetChanged();
    }

    public String deleteLastComa(String str) {
        if (str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    static class ViewHolder {
        ImageView ivFlight;
        TextView tvName;
        TextView tvTime;
        TextView tvDuration;
        TextView tvPrice;
    }
}