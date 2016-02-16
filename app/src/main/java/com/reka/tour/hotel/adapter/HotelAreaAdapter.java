package com.reka.tour.hotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.model.HotelArea;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class HotelAreaAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer, Filterable {
    private final Context mContext;
    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;
    private List<HotelArea> hotelAreaList = new ArrayList<>();
    private List<HotelArea> hotelAreaFilterList;
    private ValueFilter valueFilter;

    public HotelAreaAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<>();
        String lastFirstChar = hotelAreaList.get(0).category;
        sectionIndices.add(0);
        for (int i = 1; i < hotelAreaList.size(); i++) {
            if (!hotelAreaList.get(i).category.equals(lastFirstChar)) {
                lastFirstChar = hotelAreaList.get(i).category;
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
            letters[i] = hotelAreaList.get(mSectionIndices[i]).category.charAt(0);
        }
        return letters;
    }

    @Override
    public int getCount() {
        return hotelAreaList.size();
    }

    @Override
    public Object getItem(int position) {
        return hotelAreaList.get(position);
    }

    public HotelArea getHotelArea(int position) {
        return hotelAreaList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_hotel_area, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (hotelAreaList.get(position).label != null) {
            holder.text.setText(hotelAreaList.get(position).label);
        } else {
            holder.text.setText(hotelAreaList.get(position).value);
        }

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

        CharSequence headerChar = hotelAreaList.get(position).category;
        holder.text.setText(headerChar);

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return hotelAreaList.get(position).category.subSequence(0, 1).charAt(0);
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

    public void updateContent(List<HotelArea> hotelAreaList) {
//        this.hotelAreaList = new ArrayList<>();
//        this.hotelAreaFilterList = new ArrayList<>();

        this.hotelAreaList = hotelAreaList;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        this.hotelAreaFilterList = hotelAreaList;
        getFilter();
        notifyDataSetChanged();
    }

    public void removeContent(List<HotelArea> hotelAreaList) {
        this.hotelAreaList = hotelAreaList;
        mSectionIndices = null;
        mSectionLetters = null;
        this.hotelAreaFilterList = hotelAreaList;
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
                List<HotelArea> filterList = new ArrayList<>();
                for (int i = 0; i < hotelAreaFilterList.size(); i++) {
                    if (hotelAreaFilterList.get(i).label.toLowerCase().contains(constraint)
                            || hotelAreaFilterList.get(i).value.toLowerCase().contains(constraint)
                            || hotelAreaFilterList.get(i).category.toLowerCase().contains(constraint)) {
                        filterList.add(hotelAreaFilterList.get(i));
                    }
                }

                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = hotelAreaList.size();
                results.values = hotelAreaList;
            }

            return results;
        }

        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            hotelAreaList = (List<HotelArea>) results.values;
            notifyDataSetChanged();
        }
    }
}
