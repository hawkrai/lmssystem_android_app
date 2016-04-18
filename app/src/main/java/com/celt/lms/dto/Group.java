package com.celt.lms.dto;

import java.util.List;

class Group {
    private int groupId;
    private String groupName;
    private List<LecturesMarkVisiting> lecturesMarkVisiting;
    private List<ScheduleProtectionPracticals> scheduleProtectionPracticals;
    private List<Student> students;
    private SubGroup subGroupsOne;
    private SubGroup subGroupsTwo;

    public Group(int groupId, String groupName, List<LecturesMarkVisiting> lecturesMarkVisiting, List<ScheduleProtectionPracticals> scheduleProtectionPracticals, List<Student> students, SubGroup subGroupsOne, SubGroup subGroupsTwo) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.lecturesMarkVisiting = lecturesMarkVisiting;
        this.scheduleProtectionPracticals = scheduleProtectionPracticals;
        this.students = students;
        this.subGroupsOne = subGroupsOne;
        this.subGroupsTwo = subGroupsTwo;
    }
}
