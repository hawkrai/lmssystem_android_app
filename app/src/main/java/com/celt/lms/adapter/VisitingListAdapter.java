package com.celt.lms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.celt.lms.R;
import com.celt.lms.api.ApiLms;
import com.celt.lms.dto.*;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class VisitingListAdapter extends RecyclerView.Adapter<VisitingListAdapter.ViewHolder> implements ListAdapter {

    private List<LecturesMarkVisiting> data = null;
    private List<Student> students;
    private List<ScheduleProtectionLabs> scheduleProtectionLabs;

    public VisitingListAdapter() {
        this.data = new ArrayList<LecturesMarkVisiting>();
        this.students = new ArrayList<Student>();
        this.scheduleProtectionLabs = new ArrayList<ScheduleProtectionLabs>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LecturesMarkVisiting item = data.get(position);
        holder.title.setText(String.valueOf(item.getStudentName()));

        String s = "";
        for (int i = 0; i < item.getMarks().size(); i++) {
            Mark mark = item.getMarks().get(i);
            if (!mark.getMark().isEmpty()) {
                if (s.isEmpty())
                    s += "Лекции:\r\n";
                s += mark.getDate() + " : " + mark.getMark() + "\r\n";
            }
        }
        if (!s.isEmpty())
            s += "\r\n";

        boolean check = false;
        Student student = getStudent(item.getStudentName());
        if (student != null)
            for (LabVisitingMark visit : student.getLabVisitingMark()) {
                if (!visit.getMark().isEmpty()) {
                    if (!check) {
                        s += "Лабараторные работы:\r\n";
                        check = true;
                    }

                    s += getDate(visit.getScheduleProtectionLabId()) + " : " + visit.getMark() + "\r\n";
                }
            }

        if (s.isEmpty())
            s = "Пропусков нет!";
        holder.body.setText(s);
    }

    private String getDate(int id) {
        for (ScheduleProtectionLabs item : scheduleProtectionLabs) {
            if (item.getScheduleProtectionLabId() == id)
                return item.getDate();
        }
        return "";
    }

    private Student getStudent(String name) {
        for (Student item : students) {
            if (item.getFullName().equals(name))
                return item;
        }
        return null;
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
        GroupDTO groupDTO = (GroupDTO) data;
        this.data = groupDTO.getLecturesMarkVisiting();
        this.students = new ArrayList<Student>(groupDTO.getSubGroup(0).getStudents());
        this.students.addAll(groupDTO.getSubGroup(1).getStudents());
        this.scheduleProtectionLabs = groupDTO.getSubGroup(0).getScheduleProtectionLabs();
        this.scheduleProtectionLabs.addAll(groupDTO.getSubGroup(1).getScheduleProtectionLabs());
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
