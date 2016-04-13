package com.jixstreet.rekatoursandtravel.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.model.Country;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private List<String> listCountry;
    private ArrayList<Country> countries = new ArrayList<>();

    public CustomAdapter(Activity activity, int textViewResourceId,
                         List<String> listCountry, ArrayList<Country> countries) {
        super(activity, textViewResourceId, listCountry);
        this.listCountry = listCountry;
        this.activity = activity;
        this.countries = countries;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return super.getView(position, convertView, parent);

        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.simple_spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.tv_spinner_item);
        label.setText(countries.get(position).countryAreacode);

        return row;
    }


    public View getCustomDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return super.getView(position, convertView, parent);

        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.simple_spinner_dropdown_item, parent, false);
        CheckedTextView label = (CheckedTextView) row.findViewById(R.id.tv_spinner_dropdown_item);
        label.setText(countries.get(position).countryAreacode + " - " + countries.get(position).countryName);

        return row;
    }
}