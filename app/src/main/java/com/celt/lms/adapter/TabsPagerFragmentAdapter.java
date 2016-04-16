package com.celt.lms.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.celt.lms.fragment.AbstractTabFragment;
import com.celt.lms.fragment.ExampleFragment;
import com.celt.lms.fragment.NewsFragment;

import java.util.HashMap;
import java.util.Map;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initTabsMap(context);
    }

    @Override
    public Fragment getItem(int position) {
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

    private void initTabsMap(Context context) {
        tabs = new HashMap<Integer, AbstractTabFragment>();
        tabs.put(0, NewsFragment.getInstance(context));
        tabs.put(1, ExampleFragment.getInstance(context));
    }
}
