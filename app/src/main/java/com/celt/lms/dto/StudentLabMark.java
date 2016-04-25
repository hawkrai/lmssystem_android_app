package com.celt.lms.dto;

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
}
