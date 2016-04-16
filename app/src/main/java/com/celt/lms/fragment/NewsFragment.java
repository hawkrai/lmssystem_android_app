package com.celt.lms.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.celt.lms.R;
import com.celt.lms.adapter.NewsListAdapter;
import com.celt.lms.dto.NewsDTO;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_news;

    public static NewsFragment getInstance(Context context) {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("News");

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new NewsListAdapter(createMockData()));

        return view;
    }

    private List<NewsDTO> createMockData() {
        List<NewsDTO> data = new ArrayList();
        data.add(new NewsDTO("item 1"));
        data.add(new NewsDTO("item 2"));
        data.add(new NewsDTO("item 3"));
        data.add(new NewsDTO("item 4"));
        data.add(new NewsDTO("item 5"));
        data.add(new NewsDTO("item 6"));
        data.add(new NewsDTO("item 7"));

        return data;
    }

    private void setContext(Context context) {
        this.context = context;
    }
}
