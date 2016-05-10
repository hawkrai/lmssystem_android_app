package com.celt.lms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.celt.lms.R;
import com.celt.lms.api.ApiLms;
import com.celt.lms.dto.LabDTO;
import com.celt.lms.dto.ParsingJsonLms;
import com.celt.lms.dto.Student;
import com.celt.lms.dto.SubGroup;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class LabMarksListAdapter extends RecyclerView.Adapter<LabMarksListAdapter.ViewHolder> implements ListAdapter {

    private List<Student> data = null;
    private List<LabDTO> labs;

    public LabMarksListAdapter() {
        this.data = new ArrayList<Student>();
        this.labs = new ArrayList<LabDTO>();
    }

    public LabMarksListAdapter(SubGroup subGroup) {
        this.data = subGroup.getStudents();
        this.labs = subGroup.getLabs();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Student item = data.get(position);
        holder.title.setText(item.getFullName());

        String s = "";
        for (int i = 0; i < labs.size(); i++) {
            if (!item.getStudentLabMarks().get(i).getMark().isEmpty())
                s += labs.get(i).getShortName() + " Оценка: " + item.getStudentLabMarks().get(i).getMark() + "\r\n";
        }

        if (!d(item.getLabsMarkTotal()).isEmpty())
            s += "\r\nСредний балл: " + item.getLabsMarkTotal() + "\r\n";
        if (!d(item.getTestMark()).isEmpty())
            s += "Средний балл за тесты: " + d(item.getTestMark()) + "\r\n";
        s += "Рейтинговая оценка: " + dd(item.getLabsMarkTotal(), item.getTestMark());

        holder.body.setText(s);
    }

    private String d(String s) {
        if (s == null || s.isEmpty() || s.equals("null"))
            return "";
        return s;
    }

    private String dd(String s, String s2) {
        if (d(s).isEmpty() && d(s2).isEmpty())
            return "";
        if (d(s).isEmpty() && !d(s2).isEmpty())
            return s2;
        if (!d(s).isEmpty() && d(s2).isEmpty())
            return s;
        return String.format("%.2f", (Float.parseFloat(s) + Float.parseFloat(s2)) / 2);
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
    public Call getCall(ApiLms api) {
        return null;
    }

    @Override
    public void setData(Object data) {
        SubGroup subGroup = (SubGroup) data;
        this.data = subGroup.getStudents();
        this.labs = subGroup.getLabs();
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
