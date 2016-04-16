package com.celt.lms.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.celt.lms.R;

public class ExampleFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_example;

    public static ExampleFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ExampleFragment fragment = new ExampleFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("News");

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }

    private void setContext(Context context) {
        this.context = context;
    }
}
