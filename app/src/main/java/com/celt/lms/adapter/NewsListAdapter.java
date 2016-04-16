package com.celt.lms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.celt.lms.R;
import com.celt.lms.dto.NewsDTO;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {

    private List<NewsDTO> data;

    public NewsListAdapter(List<NewsDTO> data) {
        this.data = data;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;

        public NewsViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);

            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
