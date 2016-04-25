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
import com.celt.lms.dto.ScheduleProtectionLabs;
import com.celt.lms.dto.SubGroup;

import java.util.ArrayList;
import java.util.List;

public class LabsScheduleListAdapter extends RecyclerView.Adapter<LabsScheduleListAdapter.ViewHolder> implements ListAdapter {

    private List<LabDTO> data = null;
    private List<ScheduleProtectionLabs> scheduleProtectionLabs;

    public LabsScheduleListAdapter() {
        this.data = new ArrayList<LabDTO>();
        this.scheduleProtectionLabs = new ArrayList<ScheduleProtectionLabs>();
    }

    public LabsScheduleListAdapter(SubGroup subGroup) {
        this.data = subGroup.getLabs();
        this.scheduleProtectionLabs = subGroup.getScheduleProtectionLabs();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_labSchedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LabDTO item = data.get(position);
        holder.title.setText(String.valueOf(item.getOrder()) + " " + item.getTheme());
        holder.body.setText("Количество часов: " + String.valueOf(item.getDuration()));

        String s = "";
        for (int i = 0; i < item.getScheduleProtectionLabsRecomend().size(); i++) {
            if (item.getScheduleProtectionLabsRecomend().get(i).getMark().equals("10") && item.getScheduleProtectionLabsRecomend().get(i + 1).getMark().equals("10"))
                continue;
            if (i > 0 && item.getScheduleProtectionLabsRecomend().get(i - 1).getMark().equals("0") && item.getScheduleProtectionLabsRecomend().get(i).getMark().equals("0"))
                break;
            if (!item.getScheduleProtectionLabsRecomend().get(i).getMark().isEmpty()) {
                s += "до " + scheduleProtectionLabs.get(i).getDate() + " Оценка: " + item.getScheduleProtectionLabsRecomend().get(i).getMark() + "\r\n";
            }
        }
        holder.body2.setText(s);
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
        SubGroup subGroup = (SubGroup) data;
        this.data = subGroup.getLabs();
        this.scheduleProtectionLabs = subGroup.getScheduleProtectionLabs();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView body;
        TextView body2;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.body);
            body2 = (TextView) itemView.findViewById(R.id.body2);
        }
    }
}
