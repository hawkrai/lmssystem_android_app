package com.celt.lms.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.celt.lms.R;
import com.celt.lms.adapter.ListAdapter;
import com.celt.lms.api.ApiFactory;
import com.celt.lms.api.ApiLms;
import com.google.gson.JsonElement;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;

public class FragmentFirstTab extends AbsFragment {

    private String url;
    private ApiLms api;

    public FragmentFirstTab() {
    }

    public FragmentFirstTab(Context context, String title, int layout, ListAdapter adapter, String url) {
        Bundle args = new Bundle();
        setArguments(args);
        this.context = context;
        this.title = title;
        this.adapter = adapter;
        this.layout = layout;
        this.url = url;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        api = ApiFactory.getService();

        if (isNetworkConnected(context))
            new gedDataAsyncTask().execute(url);
        else
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        if (isNetworkConnected(context)) {
            new gedDataAsyncTask().execute(url);
        } else {
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
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
            try {
                Call<JsonElement> call = adapter.getCall(api);
                JsonElement json = call.execute().body();
                if (json != null)
                    return adapter.getParse(json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
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
