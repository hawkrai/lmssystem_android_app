package com.celt.lms.dto;

import java.util.List;

class SubGroup {
    private int groupId;
    private List<LabDTO> labs;
    private String name;
    private List<ScheduleProtectionLabs> scheduleProtectionLabs;
    private List<ScheduleProtectionLabsRecomendMark> scheduleProtectionLabsRecomendMark; //null
    private List<Student> students;
    private int subGroupId;

    public SubGroup(int groupId, List<LabDTO> labs, String name, List<ScheduleProtectionLabs> scheduleProtectionLabs, List<ScheduleProtectionLabsRecomendMark> scheduleProtectionLabsRecomendMark, List<Student> students, int subGroupId) {
        this.groupId = groupId;
        this.labs = labs;
        this.name = name;
        this.scheduleProtectionLabs = scheduleProtectionLabs;
        this.scheduleProtectionLabsRecomendMark = scheduleProtectionLabsRecomendMark;
        this.students = students;
        this.subGroupId = subGroupId;
    }
}
