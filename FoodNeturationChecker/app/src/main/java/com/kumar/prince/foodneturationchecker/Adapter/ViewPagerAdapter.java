package com.kumar.prince.foodneturationchecker.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kumar.prince.foodneturationchecker.Fragment.FragmentA;
import com.kumar.prince.foodneturationchecker.Fragment.FragmentB;
import com.kumar.prince.foodneturationchecker.R;

/**
 * Created by prince on 08/9/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    Context context;
    public ViewPagerAdapter(FragmentManager fm,Context context) {

        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new FragmentA();


        } else if (position == 1) {
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
        if (position == 0) {
            title = context.getResources().getString(R.string.event);
        } else if (position == 1) {
            title = context.getResources().getString(R.string.fab_tab);;
        }

        return title;
    }
}