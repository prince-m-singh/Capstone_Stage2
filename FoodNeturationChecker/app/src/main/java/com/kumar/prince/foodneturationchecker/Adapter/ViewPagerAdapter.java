package com.kumar.prince.foodneturationchecker.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kumar.prince.foodneturationchecker.Fragment.FragmentA;
import com.kumar.prince.foodneturationchecker.Fragment.FragmentB;

/**
 * Created by prince on 08/9/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new FragmentA();


        }
        else if (position == 1)
        {
            fragment = new FragmentB();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Event";
        }
        else if (position == 1)
        {
            title = "FAB";
        }

        return title;
    }
}