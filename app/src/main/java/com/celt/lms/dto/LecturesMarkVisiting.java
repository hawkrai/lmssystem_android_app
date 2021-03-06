package com.celt.lms.dto;

import java.util.List;

public class LecturesMarkVisiting {
    private String login;
    private List<Mark> marks;
    private int studentId;
    private String studentName;

    public LecturesMarkVisiting(String login, List<Mark> marks, int studentId, String studentName) {
        this.login = login;
        this.marks = marks;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public List<Mark> getMarks() {
        return marks;
    }
}
