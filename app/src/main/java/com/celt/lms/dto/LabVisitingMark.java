package com.celt.lms.dto;

import org.json.JSONException;
import org.json.JSONObject;

public class LabVisitingMark {
    private String comment;
    private int labVisitingMarkId;
    private String mark;
    private int scheduleProtectionLabId;
    private int studentId;
    private String studentName; //null

    public LabVisitingMark(String comment, int labVisitingMarkId, String mark, int scheduleProtectionLabId, int studentId, String studentName) {
        this.comment = comment;
        this.labVisitingMarkId = labVisitingMarkId;
        this.mark = mark;
        this.scheduleProtectionLabId = scheduleProtectionLabId;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public int getScheduleProtectionLabId() {
        return scheduleProtectionLabId;
    }

    public String getMark() {
        return mark;
    }

    JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Comment", comment);
            jsonObject.put("LabVisitingMarkId", labVisitingMarkId);
            jsonObject.put("Mark", mark);
            jsonObject.put("ScheduleProtectionLabId", scheduleProtectionLabId);
            jsonObject.put("StudentId", studentId);
            if (studentName.equals("null"))
                jsonObject.put("StudentName", JSONObject.NULL);
            else
                jsonObject.put("StudentName", studentName);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }
}
