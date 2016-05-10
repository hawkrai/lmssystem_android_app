package com.celt.lms.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ApiLms api;

    public FragmentFirstTab(Context context, String title, int layout, ListAdapter adapter) {
        Bundle args = new Bundle();
        setArguments(args);
        this.context = context;
        this.title = title;
        this.adapter = adapter;
        this.layout = layout;
        api = ApiFactory.getService();
        setRetainInstance(true);
    }

    public FragmentFirstTab() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (isNetworkConnected(context)) {
            new GetDataAsyncTask().execute();
            setRefreshing(true);
        } else {
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    @Override
    public void onRefresh() {
        if (isNetworkConnected(context)) {
            new GetDataAsyncTask().execute();
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

    private class GetDataAsyncTask extends AsyncTask<Void, Void, List> {

        @Override
        protected List doInBackground(Void... params) {
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
