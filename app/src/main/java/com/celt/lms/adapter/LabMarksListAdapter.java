package com.celt.lms.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.celt.lms.InputFilterMinMax;
import com.celt.lms.api.ApiLms;
import com.celt.lms.dto.LabDTO;
import com.celt.lms.dto.ParsingJsonLms;
import com.celt.lms.dto.Student;
import com.celt.lms.dto.SubGroup;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

import static com.celt.lms.AndroidUtils.convertDpToPx;
import static com.celt.lms.AndroidUtils.generateViewId;

public class LabMarksListAdapter extends RecyclerView.Adapter<LabMarksListAdapter.ViewHolder> implements ListAdapter {

    private List<Student> data = null;
    private static int cardId;
    private static int titleId;
    private static int bodyId;
    private static ArrayList<Integer> listTextViewId;
    private static ArrayList<Integer> listEditTextId;
    private List<LabDTO> labs;
    private boolean editStatus;

    public LabMarksListAdapter() {
        this.data = new ArrayList<Student>();
        this.labs = new ArrayList<LabDTO>();
        cardId = generateViewId();
        titleId = generateViewId();
        bodyId = generateViewId();
        editStatus = false;
    }

    public boolean isEditStatus() {
        return editStatus;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
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

        for (int i = 0; i < labs.size(); i++) {
            LinearLayout linearLayout3 = new LinearLayout(parent.getContext());
            linearLayout3.setOrientation(LinearLayout.HORIZONTAL);

            TextView textView2 = new TextView(parent.getContext());
            textView2.setId(listTextViewId.get(i));
            textView2.setPadding(0, convertDpToPx(parent.getContext(), 4), 0, 0);
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            linearLayout3.addView(textView2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            final EditText editText = new EditText(parent.getContext());
            editText.setId(listEditTextId.get(i));
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            editText.setGravity(Gravity.CENTER_HORIZONTAL);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setFilters(new InputFilter[]{new InputFilterMinMax(0, 10)});
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        InputMethodManager imm = (InputMethodManager) parent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                }
            });
            linearLayout3.addView(editText, new LinearLayout.LayoutParams(convertDpToPx(parent.getContext(), 40), LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout2.addView(linearLayout3, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }

        TextView textView2 = new TextView(parent.getContext());
        textView2.setId(bodyId);
        textView2.setPadding(0, convertDpToPx(parent.getContext(), 4), 0, 0);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        linearLayout2.addView(textView2, layoutParamsMPWC);

        cardView.addView(linearLayout2, layoutParamsMPWC);

        linearLayout.addView(cardView, layoutParamsMPWC);

        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Student item = data.get(position);
        holder.title.setText(item.getFullName());

        for (int i = 0; i < labs.size(); i++) {

            if (!item.getStudentLabMarks().get(i).getMark().isEmpty()) {
                holder.listEdiText.get(i).setText(item.getStudentLabMarks().get(i).getMark());
                if (editStatus) {
                    holder.listTextView.get(i).setText(labs.get(i).getShortName() + " Оценка:");
                    holder.listEdiText.get(i).setVisibility(View.VISIBLE);
                } else {
                    holder.listTextView.get(i).setText(labs.get(i).getShortName() + " Оценка: " + item.getStudentLabMarks().get(i).getMark());
                    holder.listEdiText.get(i).setVisibility(View.GONE);
                }
            } else {
                if (editStatus) {
                    holder.listTextView.get(i).setText(labs.get(i).getShortName() + " Оценка:");
                    holder.listTextView.get(i).setVisibility(View.VISIBLE);
                    holder.listEdiText.get(i).setVisibility(View.VISIBLE);
                } else {
                    holder.listTextView.get(i).setVisibility(View.GONE);
                    holder.listEdiText.get(i).setVisibility(View.GONE);
                }
            }
            final int finalI = i;
            holder.listEdiText.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    data.get(position).getStudentLabMarks().get(finalI).setMark(s.toString());
                }
            });
        }

        String s = "";

        if (!checkAndConvertNullToEmpty(item.getLabsMarkTotal()).isEmpty())
            s += "Средний балл: " + item.getLabsMarkTotal() + "\r\n";
        if (!checkAndConvertNullToEmpty(item.getTestMark()).isEmpty())
            s += "Средний балл за тесты: " + checkAndConvertNullToEmpty(item.getTestMark()) + "\r\n";
        s += "Рейтинговая оценка: " + getAverage(item.getLabsMarkTotal(), item.getTestMark());

        holder.body.setText(s);
    }

    private String checkAndConvertNullToEmpty(String s) {
        if (s == null || s.isEmpty() || s.equals("null"))
            return "";
        return s;
    }

    private String getAverage(String s, String s2) {
        if (checkAndConvertNullToEmpty(s).isEmpty() && checkAndConvertNullToEmpty(s2).isEmpty())
            return "";
        if (checkAndConvertNullToEmpty(s).isEmpty() && !checkAndConvertNullToEmpty(s2).isEmpty())
            return s2;
        if (!checkAndConvertNullToEmpty(s).isEmpty() && checkAndConvertNullToEmpty(s2).isEmpty())
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
        if (listTextViewId == null) {
            listTextViewId = new ArrayList<Integer>();
            for (int i = 0; i < this.labs.size(); i++) {
                listTextViewId.add(generateViewId());
            }
        }
        if (listEditTextId == null) {
            listEditTextId = new ArrayList<Integer>();
            for (int i = 0; i < this.labs.size(); i++) {
                listEditTextId.add(generateViewId());
            }
        }
    }

    public void changeView() {
        editStatus = !editStatus;
        notifyDataSetChanged();
    }

    public List<Student> getStudents() {
        return data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView body;
        ArrayList<TextView> listTextView;
        ArrayList<EditText> listEdiText;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(cardId);
            title = (TextView) itemView.findViewById(titleId);
            body = (TextView) itemView.findViewById(bodyId);
            listTextView = new ArrayList<TextView>();
            listEdiText = new ArrayList<EditText>();
            for (int i = 0; i < listTextViewId.size(); i++) {
                listTextView.add((TextView) itemView.findViewById(listTextViewId.get(i)));
                listEdiText.add((EditText) itemView.findViewById(listEditTextId.get(i)));
            }
        }
    }

}
