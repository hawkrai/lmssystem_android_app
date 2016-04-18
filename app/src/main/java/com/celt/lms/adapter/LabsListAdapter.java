package com.celt.lms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.celt.lms.R;
import com.celt.lms.dto.LabDTO;
import com.celt.lms.dto.ParsingJsonLms;

import java.util.ArrayList;
import java.util.List;

public class LabsListAdapter extends RecyclerView.Adapter<LabsListAdapter.LabViewHolder> implements ListAdapter {

    private List data = null;

    public LabsListAdapter() {
        this.data = new ArrayList<LabDTO>();
    }

    public LabsListAdapter(List<LabDTO> data) {
        this.data = data;
    }

    @Override
    public LabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lab, parent, false);
        return new LabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LabViewHolder holder, int position) {
        LabDTO item = (LabDTO) data.get(position);
        holder.title.setText(String.valueOf(item.getOrder()) + " " + item.getTheme());
        holder.body.setText("Количество часов: " + String.valueOf(item.getDuration()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public List getParse(String s) {
        return ParsingJsonLms.getParseLabs(s);
    }

    @Override
    public void setData(List data) {
        this.data = data;
    }

    static class LabViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView body;
        TextView date;

        public LabViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.body);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
