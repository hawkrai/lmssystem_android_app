package com.celt.lms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.celt.lms.R;
import com.celt.lms.api.ApiLms;
import com.celt.lms.dto.LecturesDTO;
import com.celt.lms.dto.ParsingJsonLms;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class LecturesListAdapter extends RecyclerView.Adapter<LecturesListAdapter.LecturesViewHolder> implements ListAdapter {
    private List data;

    public LecturesListAdapter() {
        this.data = new ArrayList<LecturesDTO>();
    }

    public LecturesListAdapter(List<LecturesDTO> data) {
        this.data = data;
    }

    @Override
    public LecturesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lectures, parent, false);
        return new LecturesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LecturesViewHolder holder, int position) {
        LecturesDTO item = (LecturesDTO) data.get(position);
        holder.title.setText(String.valueOf(item.getOrder()) + ". " + item.getTheme());
        holder.body.setText("Количество часов: " + String.valueOf(item.getDuration()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public List getParse(String s) {
        return ParsingJsonLms.getParseLectures(s);
    }

    @Override
    public Call getCall(ApiLms api) {
        return api.getLectures(2025);
    }

    @Override
    public void setData(Object data) {
        this.data = (List) data;
    }

    static class LecturesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView body;

        public LecturesViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.body);
        }
    }
}
