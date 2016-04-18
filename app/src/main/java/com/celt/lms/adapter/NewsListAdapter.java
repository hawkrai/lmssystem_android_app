package com.celt.lms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.celt.lms.R;
import com.celt.lms.dto.NewsDTO;
import com.celt.lms.dto.ParsingJsonLms;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> implements ListAdapter {

    private List data = null;

    public NewsListAdapter() {
        this.data = new ArrayList<NewsDTO>();
    }

    public NewsListAdapter(List<NewsDTO> data) {
        this.data = data;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsDTO item = (NewsDTO) data.get(position);
        if (!item.getTitle().isEmpty()) {
            holder.title.setText(item.getTitle());
        } else {
            holder.title.setVisibility(View.GONE);
        }
        holder.body.setText(Html.fromHtml(item.getBody().replace("<\\/p>", "").replace("<p>", "")));
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public List getParse(String s) {
        return ParsingJsonLms.getParseNews(s);
    }

    @Override
    public void setData(List data) {
        this.data = data;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView body;
        TextView date;

        public NewsViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.body);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
