package com.celt.lms.adapter;

import com.celt.lms.api.ApiLms;
import retrofit2.Call;

import java.util.List;

public interface ListAdapter {
    List getParse(String s);

    Call getCall(ApiLms api);

    void setData(Object data);

    void notifyDataSetChanged();

    int getItemCount();
}
