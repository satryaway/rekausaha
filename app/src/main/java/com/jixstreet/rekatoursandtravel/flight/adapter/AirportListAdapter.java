package com.jixstreet.rekatoursandtravel.flight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.flight.model.Airport;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class AirportListAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer, Filterable {
    private final Context mContext;
    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;
    private List<Airport> airportList = new ArrayList<>();
    private List<Airport> airportFilterList;
    private ValueFilter valueFilter;

    public AirportListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        String lastFirstChar = airportList.get(0).countryName;
        sectionIndices.add(0);
        for (int i = 1; i < airportList.size(); i++) {
            if (!airportList.get(i).countryName.equals(lastFirstChar)) {
                lastFirstChar = airportList.get(i).countryName;
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = airportList.get(mSectionIndices[i]).countryName.charAt(0);
        }
        return letters;
    }

    @Override
    public int getCount() {
        return airportList.size();
    }

    @Override
    public Object getItem(int position) {
        return airportList.get(position);
    }

    public Airport getAirport(int position) {
        return airportList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.airport_lv_item_layout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(airportList.get(position).locationName + " - " + airportList.get(position).name);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.country_header_layout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.country_tv);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        CharSequence headerChar = airportList.get(position).countryName;
        holder.text.setText(headerChar);

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return airportList.get(position).countryName.subSequence(0, 1).charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0)
            return 0;

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void updateContent(List<Airport> airportList) {
        this.airportList = airportList;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        this.airportFilterList = airportList;
        getFilter();
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null)
            valueFilter = new ValueFilter();

        return valueFilter;
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }

    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<Airport> filterList = new ArrayList<>();
                for (int i = 0; i < airportFilterList.size(); i++) {
                    if (airportFilterList.get(i).name.toLowerCase().contains(constraint)
                            || airportFilterList.get(i).countryName.toLowerCase().contains(constraint)
                            || airportFilterList.get(i).locationName.toLowerCase().contains(constraint)
                            || airportFilterList.get(i).code.toLowerCase().contains(constraint)) {
                        filterList.add(airportFilterList.get(i));
                    }
                }

                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = airportList.size();
                results.values = airportList;
            }

            return results;
        }

        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            airportList = (List<Airport>) results.values;
            notifyDataSetChanged();
        }
    }
}
