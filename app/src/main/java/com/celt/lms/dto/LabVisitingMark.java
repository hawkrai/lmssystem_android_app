package com.celt.lms.dto;

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
}
