package com.celt.lms.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.celt.lms.MainActivity;
import com.celt.lms.R;
import com.celt.lms.adapter.LabMarksListAdapter;
import com.celt.lms.adapter.ListAdapter;
import com.celt.lms.onEventListener;

import static com.celt.lms.MainActivity.setFragment;

public class FragmentSecondTab extends AbsFragment {

    private onEventListener someEventListener;
    private int key;

    public FragmentSecondTab() {
    }

    public FragmentSecondTab(Context context, int key, String title, int layout, ListAdapter adapter) {
        Bundle args = new Bundle();
        setArguments(args);
        this.context = context;
        this.key = key;
        this.title = title;
        this.adapter = adapter;
        this.layout = layout;
        setRetainInstance(true);
    }

    public int getKey() {
        return key;
    }

    public void setAdapter(Object data) {
        this.adapter.setData(data);
        this.adapter.notifyDataSetChanged();
        this.setRefreshing(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setFragment(this);
        if (MainActivity.isCheckRefresh())
            setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        if (isNetworkConnected(context))
            someEventListener.updateGroupList();
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

    public void changeView() {
        ((LabMarksListAdapter) adapter).changeView();
    }

    public boolean isTypeList() {
        return ((LabMarksListAdapter) adapter).isEditStatus();
    }
}