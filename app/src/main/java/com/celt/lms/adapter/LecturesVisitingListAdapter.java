package com.celt.lms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.celt.lms.R;
import com.celt.lms.dto.LecturesMarkVisiting;
import com.celt.lms.dto.Mark;
import com.celt.lms.dto.ParsingJsonLms;

import java.util.ArrayList;
import java.util.List;

public class LecturesVisitingListAdapter extends RecyclerView.Adapter<LecturesVisitingListAdapter.ViewHolder> implements ListAdapter {

    private List<LecturesMarkVisiting> data = null;

    public LecturesVisitingListAdapter() {
        this.data = new ArrayList<LecturesMarkVisiting>();
    }

    public LecturesVisitingListAdapter(List<LecturesMarkVisiting> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LecturesMarkVisiting item = data.get(position);
        holder.title.setText(String.valueOf(item.getStudentName()));

        String s = "";
        for (int i = 0; i < item.getMarks().size(); i++) {
            Mark mark = item.getMarks().get(i);
            if (!mark.getMark().isEmpty())
                s += mark.getDate() + " : " + mark.getMark() + "\r\n";
        }
        if (s.isEmpty())
            s = "Пропусков нет!";
        holder.body.setText(s);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public List getParse(String s) {
        return ParsingJsonLms.getParseGroup(s);
    }

    @Override
    public void setData(Object data) {
        this.data = (List<LecturesMarkVisiting>) data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView body;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.body);
        }
    }
}
