package com.celt.lms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.celt.lms.MainActivity;
import com.celt.lms.R;
import com.celt.lms.api.ApiLms;
import com.celt.lms.dto.*;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class LecturesListAdapter extends RecyclerView.Adapter<LecturesListAdapter.LecturesViewHolder> implements ListAdapter {

    private ArrayList[] mas;
    private boolean[] check = {false, false};


    private List<Pair<Integer, Object>> data;
    private List<ScheduleProtectionLabs> scheduleProtectionLabs;

    public LecturesListAdapter() {
        this.data = new ArrayList<Pair<Integer, Object>>();
        mas = new ArrayList[2];

    }

    @Override
    public LecturesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_labschedule, parent, false);
        return new LecturesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final LecturesViewHolder holder, final int position) {
        switch (data.get(position).first) {
            case 0:
            case 1:
                holder.title.setText((String) data.get(position).second);
                holder.body.setVisibility(View.GONE);
                holder.body2.setVisibility(View.GONE);
                break;
            case 2: {
                LecturesDTO item = (LecturesDTO) data.get(position).second;

                holder.body.setVisibility(View.VISIBLE);
                holder.body2.setVisibility(View.GONE);

                holder.title.setText(String.valueOf(item.getOrder()) + ". " + item.getTheme());
                holder.body.setText("Количество часов: " + String.valueOf(item.getDuration()));
                break;
            }
            case 3: {
                LabDTO item = ((Lab) data.get(position).second).lab;

                holder.body.setVisibility(View.VISIBLE);
                if (((Lab) data.get(position).second).v == View.GONE)
                    holder.body2.setVisibility(View.GONE);
                else
                    holder.body2.setVisibility(View.VISIBLE);

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
                break;
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = data.get(position).first;
                if (type == 0 || type == 1) {
                    if (!check[type]) {
                        data.addAll(position + 1, mas[type]);
                        check[type] = true;
                        notifyItemRangeInserted(position + 1, mas[type].size());
                    } else {
                        data.removeAll(mas[type]);
                        check[type] = false;
                        notifyItemRangeRemoved(position + 1, mas[type].size());
                    }
                }
                if (type == 3) {
                    if (holder.body2.getVisibility() == View.GONE) {
                        holder.body2.setVisibility(View.VISIBLE);
                        ((Lab) data.get(position).second).v = View.VISIBLE;
                    } else {
                        ((Lab) data.get(position).second).v = View.GONE;
                        notifyItemChanged(position);
                    }
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void setData(Object data) {
        Object[] objects = (Object[]) data;
        ArrayList<LecturesDTO> d = (ArrayList<LecturesDTO>) objects[0];
        mas[0] = new ArrayList<Pair<Integer, Object>>();

        this.data.clear();
        this.data.add(new Pair<Integer, Object>(0, "Лекции"));
        for (LecturesDTO item : d) {
            this.mas[0].add(new Pair<Integer, Object>(2, item));
        }

        SubGroup subGroup = (SubGroup) objects[1];
        List<Lab> labs = new ArrayList<Lab>();
        ArrayList<Pair<Integer, Object>> templabs = null;
        if (mas[1] != null)
            templabs = new ArrayList<Pair<Integer, Object>>(mas[1]);

        for (int i = 0; i < subGroup.getLabs().size(); i++) {
            if (templabs != null && i <= templabs.size() && ((Lab) templabs.get(i).second).getV() == View.VISIBLE)
                labs.add(new Lab(subGroup.getLabs().get(i), View.VISIBLE));
            else
                labs.add(new Lab(subGroup.getLabs().get(i)));
        }
        mas[1] = new ArrayList<Pair<Integer, Object>>();

        this.data.add(new Pair<Integer, Object>(1, "Лабораторные работы"));
        for (Lab item : labs) {
            this.mas[1].add(new Pair<Integer, Object>(3, item));
        }
        this.scheduleProtectionLabs = subGroup.getScheduleProtectionLabs();

        int type = 0;
        int position = 0;
        if (check[type]) {
            this.data.addAll(position + 1, mas[type]);
            check[type] = true;
            position = mas[0].size();
        }
        position++;
        type++;
        if (check[type]) {
            this.data.addAll(position + 1, mas[type]);
            check[type] = true;
        }
    }

    @Override
    public List getParse(String s) {
        return ParsingJsonLms.getParseLectures(s);
    }

    @Override
    public Call getCall(ApiLms api) {
        return api.getLectures(MainActivity.getSubjectId());
    }

    static class LecturesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView body;
        TextView body2;

        public LecturesViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.body);
            body2 = (TextView) itemView.findViewById(R.id.body2);
        }
    }

    private class Lab {
        private LabDTO lab;
        private int v = View.GONE;

        Lab(LabDTO lab) {
            this.lab = lab;
        }

        Lab(LabDTO lab, int v) {
            this.lab = lab;
            this.v = v;
        }

        int getV() {
            return v;
        }
    }
}
