package com.celt.lms.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.celt.lms.R;
import com.celt.lms.adapter.ListAdapter;

public abstract class AbsFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {
    String title;
    ListAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Context context;
    int layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        return view;
    }

    public String getTitle() {
        return title;
    }

    boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onRefresh() {

    }

    public void setRefreshing(boolean bool) {
        if (mSwipeRefreshLayout != null) {
            if (bool)
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                });
            else
                mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}

