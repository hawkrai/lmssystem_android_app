package com.celt.lms.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.celt.lms.MainActivity;
import com.celt.lms.R;
import com.celt.lms.adapter.ListAdapter;
import com.celt.lms.onEventListener;

import static com.celt.lms.MainActivity.setFragment;

public class FragmentSecondTab extends AbsFragment {

    private onEventListener someEventListener;
    private String url;
    private int key;

    public FragmentSecondTab() {
    }

    public FragmentSecondTab(Context context, int key, String title, int layout, ListAdapter adapter, String url) {
        Bundle args = new Bundle();
        setArguments(args);
        this.context = context;
        this.key = key;
        this.title = title;
        this.adapter = adapter;
        this.layout = layout;
        this.url = url;
    }

    public int getKey() {
        return key;
    }


    public void setAdapter(Object data) {
        this.adapter.setData(data);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setFragment(this);
        if (MainActivity.is())
            setRefreshing(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            setRetainInstance(true);
    }


    @Override
    public void onRefresh() {
        if (isNetworkConnected(context))
            someEventListener.updateGroupList("https://collapsed.space/ServicesCoreService.svcGetGroups2025.json");
        else {
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onEventListener");
        }
    }
}