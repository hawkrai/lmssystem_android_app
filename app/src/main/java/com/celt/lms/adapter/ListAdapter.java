package com.celt.lms.adapter;

import java.util.List;

public interface ListAdapter {
    List getParse(String s);

    void setData(Object data);

    void notifyDataSetChanged();

    int getItemCount();
}
