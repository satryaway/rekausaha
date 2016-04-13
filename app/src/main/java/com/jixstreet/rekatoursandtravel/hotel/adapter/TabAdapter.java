package com.jixstreet.rekatoursandtravel.hotel.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jixstreet.rekatoursandtravel.hotel.fragment.FasilitasFragment;
import com.jixstreet.rekatoursandtravel.hotel.fragment.FotoFragment;
import com.jixstreet.rekatoursandtravel.hotel.fragment.InfoFragment;

public class TabAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"INFO", "FASILITAS", "FOTO"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment eventsFragment = null;
        switch (position) {
            case 0:
                eventsFragment = InfoFragment.newInstance();
                break;
            case 1:
                eventsFragment = FasilitasFragment.newInstance();
                break;
            case 2:
                eventsFragment = FotoFragment.newInstance();
                break;
        }
        return eventsFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}