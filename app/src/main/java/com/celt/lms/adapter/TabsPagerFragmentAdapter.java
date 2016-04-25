package com.celt.lms.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import com.celt.lms.AbsFragment;

public class TabsPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<AbsFragment> tabs;
//    private Context context;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm, SparseArrayCompat<AbsFragment> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    public SparseArrayCompat<AbsFragment> getTabs() {
        return tabs;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }
}
