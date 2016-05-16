package com.celt.lms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.celt.lms.R;
import com.celt.lms.api.ApiLms;
import com.celt.lms.dto.*;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

import static com.celt.lms.AndroidUtils.convertDpToPx;
import static com.celt.lms.AndroidUtils.generateViewId;

public class VisitingListAdapter extends RecyclerView.Adapter<VisitingListAdapter.ViewHolder> implements ListAdapter {

    private static int cardId;
    private static int titleId;
    private static int lecturesTitleId;
    private static int labsTitleId;
    private static int bodyId;
    private static ArrayList<Integer> listLecturesTextViewId;
    private static ArrayList<Integer> listLabsTextViewId;
    private List<LecturesMarkVisiting> data = null;
    private List<Student> students;
    private List<ScheduleProtectionLabs> scheduleProtectionLabs;

    public VisitingListAdapter() {
        this.data = new ArrayList<LecturesMarkVisiting>();
        this.students = new ArrayList<Student>();
        this.scheduleProtectionLabs = new ArrayList<ScheduleProtectionLabs>();

        cardId = generateViewId();
        titleId = generateViewId();
        lecturesTitleId = generateViewId();
        labsTitleId = generateViewId();
        bodyId = generateViewId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams layoutParams0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams0.setMargins(convertDpToPx(parent.getContext(), 12), convertDpToPx(parent.getContext(), 5), convertDpToPx(parent.getContext(), 12), convertDpToPx(parent.getContext(), 5));
        LinearLayout.LayoutParams layoutParamsMPWC = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(layoutParams0);

        CardView cardView = new CardView(parent.getContext());
        cardView.setId(cardId);
        cardView.setCardElevation(2);
        cardView.setUseCompatPadding(true);

        LinearLayout linearLayout2 = new LinearLayout(parent.getContext());
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        linearLayout2.setPadding(convertDpToPx(parent.getContext(), 16), convertDpToPx(parent.getContext(), 16), convertDpToPx(parent.getContext(), 16), convertDpToPx(parent.getContext(), 16));

        TextView textView = new TextView(parent.getContext());
        textView.setId(titleId);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        linearLayout2.addView(textView, layoutParamsMPWC);

        TextView textView22 = new TextView(parent.getContext());
        textView22.setId(lecturesTitleId);
        textView22.setPadding(0, convertDpToPx(parent.getContext(), 4), 0, 0);
        textView22.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView22.setText(parent.getResources().getString(R.string.lectures, ":"));
        textView22.setVisibility(View.GONE);
        linearLayout2.addView(textView22, layoutParamsMPWC);

        for (Integer id : listLecturesTextViewId) {
            LinearLayout linearLayout3 = new LinearLayout(parent.getContext());
            linearLayout3.setOrientation(LinearLayout.HORIZONTAL);

            TextView textView2 = new TextView(parent.getContext());
            textView2.setId(id);
            textView2.setPadding(0, convertDpToPx(parent.getContext(), 4), 0, 0);
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            linearLayout3.addView(textView2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout2.addView(linearLayout3, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        TextView textView222 = new TextView(parent.getContext());
        textView222.setId(labsTitleId);
        textView222.setPadding(0, convertDpToPx(parent.getContext(), 4), 0, 0);
        textView222.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView222.setText(parent.getResources().getString(R.string.labs, ":"));
        textView22.setVisibility(View.GONE);
        linearLayout2.addView(textView222, layoutParamsMPWC);
        for (Integer id : listLabsTextViewId) {
            LinearLayout linearLayout3 = new LinearLayout(parent.getContext());
            linearLayout3.setOrientation(LinearLayout.HORIZONTAL);

            TextView textView2 = new TextView(parent.getContext());
            textView2.setId(id);
            textView2.setPadding(0, convertDpToPx(parent.getContext(), 4), 0, 0);
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView2.setVisibility(View.GONE);
            linearLayout3.addView(textView2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout2.addView(linearLayout3, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        TextView textView2 = new TextView(parent.getContext());
        textView2.setId(bodyId);
        textView2.setPadding(0, convertDpToPx(parent.getContext(), 4), 0, 0);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView2.setText(R.string.absenteeism_no);
        textView2.setVisibility(View.GONE);
        linearLayout2.addView(textView2, layoutParamsMPWC);

        cardView.addView(linearLayout2, layoutParamsMPWC);

        linearLayout.addView(cardView, layoutParamsMPWC);

        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LecturesMarkVisiting item = data.get(position);
        holder.title.setText(String.valueOf(item.getStudentName()));

        boolean absLectures = false;
        boolean absLabs = false;
        for (int i = 0; i < item.getMarks().size(); i++) {
            Mark mark = item.getMarks().get(i);
            if (!mark.getMark().isEmpty()) {
                absLectures = true;
                holder.listLecturesTextView.get(i).setText(mark.getDate() + ": " + mark.getMark());
                holder.listLecturesTextView.get(i).setVisibility(View.VISIBLE);
            } else {
                holder.listLecturesTextView.get(i).setVisibility(View.GONE);
            }
        }

        Student student = getStudent(item.getStudentName());
        if (student != null) {
            for (int i = 0; i < student.getLabVisitingMark().size(); i++) {
                LabVisitingMark visit = student.getLabVisitingMark().get(i);
                if (!visit.getMark().isEmpty()) {
                    absLabs = true;
                    holder.listLabsTextView.get(i).setText(getDate(visit.getScheduleProtectionLabId()) + ": " + visit.getMark());
                    holder.listLabsTextView.get(i).setVisibility(View.VISIBLE);
                } else {
                    holder.listLabsTextView.get(i).setVisibility(View.GONE);
                }
            }
        } else {
            for (int i = 0; i < holder.listLabsTextView.size(); i++) {
                holder.listLabsTextView.get(i).setVisibility(View.GONE);
            }
        }
        setViewVisibility(holder.lecturesTitle, absLectures);
        setViewVisibility(holder.labsTitle, absLabs);
        setViewVisibility(holder.body, !absLectures && !absLabs);
    }

    private void setViewVisibility(View view, boolean visibility) {
        view.setVisibility(visibility ? View.VISIBLE : View.GONE);
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
        this.scheduleProtectionLabs = new ArrayList<ScheduleProtectionLabs>(groupDTO.getSubGroup(0).getScheduleProtectionLabs());
        this.scheduleProtectionLabs.addAll(groupDTO.getSubGroup(1).getScheduleProtectionLabs());
        if (listLecturesTextViewId == null) {
            listLecturesTextViewId = new ArrayList<Integer>();
            for (int i = 0; i < this.data.get(0).getMarks().size(); i++) {
                listLecturesTextViewId.add(generateViewId());
            }
        }
        if (listLabsTextViewId == null) {
            listLabsTextViewId = new ArrayList<Integer>();
            int count = Math.max(groupDTO.getSubGroup(0).getScheduleProtectionLabs().size(), groupDTO.getSubGroup(1).getScheduleProtectionLabs().size());
            for (int i = 0; i < count; i++) {
                listLabsTextViewId.add(generateViewId());
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView lecturesTitle;
        TextView labsTitle;
        TextView body;
        ArrayList<TextView> listLecturesTextView;
        ArrayList<TextView> listLabsTextView;
        ArrayList<EditText> listEdiText;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(cardId);
            title = (TextView) itemView.findViewById(titleId);
            lecturesTitle = (TextView) itemView.findViewById(lecturesTitleId);
            labsTitle = (TextView) itemView.findViewById(labsTitleId);
            body = (TextView) itemView.findViewById(bodyId);
            listLecturesTextView = new ArrayList<TextView>();

            listEdiText = new ArrayList<EditText>();
            for (Integer id : listLecturesTextViewId) {
                listLecturesTextView.add((TextView) itemView.findViewById(id));
            }

            listLabsTextView = new ArrayList<TextView>();
            for (Integer id : listLabsTextViewId) {
                listLabsTextView.add((TextView) itemView.findViewById(id));
            }
        }
    }
}
