package com.celt.lms.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Student {
    int groupId;
    private ArrayList<FileLab> fileLabs;
    private String fullName;
    private ArrayList<LabVisitingMark> labVisitingMark;
    private String labsMarkTotal;
    private String login;
    private String practicalMarkTotal;
    private ArrayList<PracticalVisitingMark> practicalVisitingMark; //null
    private int studentId;
    private ArrayList<StudentLabMark> studentLabMarks; //null
    private ArrayList<StudentPracticalMark> studentPracticalMarks; //null
    private String testMark; //null

    public Student(ArrayList<FileLab> fileLabs, String fullName, int groupId, ArrayList<LabVisitingMark> labVisitingMark, String labsMarkTotal, String login, String practicalMarkTotal, ArrayList<PracticalVisitingMark> practicalVisitingMark, int studentId, ArrayList<StudentLabMark> studentLabMarks, ArrayList<StudentPracticalMark> studentPracticalMarks, String testMark) {
        this.fileLabs = fileLabs;
        this.fullName = fullName;
        this.groupId = groupId;
        this.labVisitingMark = labVisitingMark;
        this.labsMarkTotal = labsMarkTotal;
        this.login = login;
        this.practicalMarkTotal = practicalMarkTotal;
        this.practicalVisitingMark = practicalVisitingMark;
        this.studentId = studentId;
        this.studentLabMarks = studentLabMarks;
        this.studentPracticalMarks = studentPracticalMarks;
        this.testMark = testMark;
    }

    public String getTestMark() {
        return testMark;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLabsMarkTotal() {
        return labsMarkTotal;
    }

    public ArrayList<StudentLabMark> getStudentLabMarks() {
        return studentLabMarks;
    }

    public ArrayList<LabVisitingMark> getLabVisitingMark() {
        return labVisitingMark;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("FileLabs", getFileLabsJsonArray());
            jsonObject.put("FullName", fullName);
            jsonObject.put("GroupId", groupId);
            jsonObject.put("LabVisitingMark", getLabVisitingMarkJsonArray());
            jsonObject.put("LabsMarkTotal", labsMarkTotal);
            jsonObject.put("Login", login);
            if (practicalMarkTotal.equals("null"))
                jsonObject.put("PracticalMarkTotal", JSONObject.NULL);
            else
                jsonObject.put("PracticalMarkTotal", practicalMarkTotal);
            jsonObject.put("PracticalVisitingMark", new JSONArray());
            jsonObject.put("StudentId", studentId);
            jsonObject.put("StudentLabMarks", getStudentLabMarksJsonArray());
            jsonObject.put("StudentPracticalMarks", new JSONArray());
            jsonObject.put("TestMark", testMark);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    private JSONArray getFileLabsJsonArray() {
        JSONArray jsonArray = new JSONArray();
        for (FileLab item : fileLabs) {
            jsonArray.put(item.getJSONObject());
        }
        return jsonArray;
    }

    private JSONArray getLabVisitingMarkJsonArray() {
        JSONArray jsonArray = new JSONArray();
        for (LabVisitingMark item : labVisitingMark) {
            jsonArray.put(item.getJSONObject());
        }
        return jsonArray;
    }

    private JSONArray getStudentLabMarksJsonArray() {
        JSONArray jsonArray = new JSONArray();
        for (StudentLabMark item : studentLabMarks) {
            jsonArray.put(item.getJSONObject());
        }
        return jsonArray;
    }
}
