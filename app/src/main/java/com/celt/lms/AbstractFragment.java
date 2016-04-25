package com.celt.lms;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.celt.lms.adapter.ListAdapter;

import java.util.List;

public class AbstractFragment extends AbsFragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private int layout;
    private String url;
    private ListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String title;

    public AbstractFragment() {
    }

    public AbstractFragment(Context context, String title, int layout, ListAdapter adapter, String url) {
        Bundle args = new Bundle();
        setArguments(args);
        this.context = context;
        this.title = title;
        this.adapter = adapter;
        this.layout = layout;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new gedDataAsyncTask().execute(url);

    }

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
        if (adapter.getItemCount() == 0)
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        return view;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new gedDataAsyncTask().execute(url);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private class gedDataAsyncTask extends AsyncTask<String, Void, List> {

        @Override
        protected List doInBackground(String... params) {
            return adapter.getParse(MainActivity.downloadJson(params[0]));
        }

        @Override
        protected void onPostExecute(List data) {
            if (data != null) {
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
            }

            mSwipeRefreshLayout.setRefreshing(false);

        }
    }
}
