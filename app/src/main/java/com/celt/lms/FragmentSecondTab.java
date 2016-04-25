package com.celt.lms;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.celt.lms.adapter.ListAdapter;

import static com.celt.lms.MainActivity.setFragment;

public class FragmentSecondTab extends AbsFragment implements SwipeRefreshLayout.OnRefreshListener {
    private onEventListener someEventListener;
    private View view;
    private Context context;
    private int layout;
    private String url;
    private ListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String title;
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

    public String getTitle() {
        return title;
    }

    public void setAdapter(Object data) {
        this.adapter.setData(data);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setFragment(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter((RecyclerView.Adapter<RecyclerView.ViewHolder>) adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        if (adapter.getItemCount() == 0)
            setRefreshing(true);

        return view;
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

    @Override
    public void onRefresh() {
        someEventListener.updateGroupList("https://collapsed.space/ServicesCoreService.svcGetGroups2025.json");
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