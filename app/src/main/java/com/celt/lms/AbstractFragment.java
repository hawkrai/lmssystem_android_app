package com.celt.lms;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.celt.lms.adapter.ListAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AbstractFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private int layout;
    private String url;
    private ListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String title;

    public AbstractFragment() {
    }

//    public static AbstractFragment getInstance(Context context, String title, int layout, ListAdapter adapter, String url) {
//        Bundle args = new Bundle();
//        AbstractFragment fragment = new AbstractFragment();
//        fragment.setArguments(args);
//        fragment.context = context;
//        fragment.title = title;
//        fragment.adapter = adapter;
//        fragment.layout = layout;
//        fragment.url = url;
//        return fragment;
//    }

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

        return view;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new NewsAsyncTask().execute(url);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private String downloadJson(String param) {
        try {
            URL url = new URL(param);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                return stringBuilder.toString();
            } catch (Exception e) {
                return null;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, List> {

        @Override
        protected List doInBackground(String... params) {
            return adapter.getParse(downloadJson(params[0]));
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
