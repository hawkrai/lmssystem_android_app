package com.celt.lms.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import com.celt.lms.AbstractFragment;
import com.celt.lms.R;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private SparseArrayCompat<AbstractFragment> tabs;
//    private Context context;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
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
        tabs = new SparseArrayCompat<AbstractFragment>();
        tabs.put(0, new AbstractFragment(context, "News", R.layout.fragment, new NewsListAdapter(), "https://collapsed.space/ServicesNewsNewsService.svcGetNews2025.json"));
        tabs.put(1, new AbstractFragment(context, "Lectures", R.layout.fragment, new LecturesListAdapter(), "https://collapsed.space/ServicesLecturesLecturesService.svcGetLectures2025.json"));
        tabs.put(2, new AbstractFragment(context, "Labs", R.layout.fragment, new LabsListAdapter(), "https://collapsed.space/ServicesLabsLabsService.svcGetLabs2025.json"));
    }
}
