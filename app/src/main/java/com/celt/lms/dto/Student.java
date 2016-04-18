package com.celt.lms.dto;

import java.util.ArrayList;

class Student {
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
}
