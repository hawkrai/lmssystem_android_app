package com.celt.lms.dto;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentLabMark {
    private int labId;
    private String mark;
    private int studentId;
    private int studentLabMarkId;

    public StudentLabMark(int labId, String mark, int studentId, int studentLabMarkId) {
        this.labId = labId;
        this.mark = mark;
        this.studentId = studentId;
        this.studentLabMarkId = studentLabMarkId;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("LabId", labId);
            jsonObject.put("Mark", mark);
            jsonObject.put("StudentId", studentId);
            jsonObject.put("StudentLabMarkId", studentLabMarkId);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }
}
