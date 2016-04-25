package com.celt.lms.dto;

import java.util.List;

public class GroupDTO {
    private int groupId;
    private String groupName;
    private List<LecturesMarkVisiting> lecturesMarkVisiting;
    private List<ScheduleProtectionPracticals> scheduleProtectionPracticals;
    private List<Student> students;
    private SubGroup subGroupsOne;
    private SubGroup subGroupsTwo;

    public GroupDTO() {
    }

    public GroupDTO(int groupId, String groupName, List<LecturesMarkVisiting> lecturesMarkVisiting, List<ScheduleProtectionPracticals> scheduleProtectionPracticals, List<Student> students, SubGroup subGroupsOne, SubGroup subGroupsTwo) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.lecturesMarkVisiting = lecturesMarkVisiting;
        this.scheduleProtectionPracticals = scheduleProtectionPracticals;
        this.students = students;
        this.subGroupsOne = subGroupsOne;
        this.subGroupsTwo = subGroupsTwo;
    }

    public SubGroup getSubGroup(int i) {
        if (i == 0)
            return subGroupsOne;
        else
            return subGroupsTwo;
    }

    public SubGroup getSubGroupsOne() {
        return subGroupsOne;
    }

    public SubGroup getSubGroupsTwo() {
        return subGroupsTwo;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<LecturesMarkVisiting> getLecturesMarkVisiting() {
        return lecturesMarkVisiting;
    }
}
