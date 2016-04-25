package com.celt.lms.dto;

public class ScheduleProtectionLabsRecomend {
    String mark;
    int scheduleProtectionId;

    public ScheduleProtectionLabsRecomend(String mark, int scheduleProtectionId) {
        this.mark = mark;
        this.scheduleProtectionId = scheduleProtectionId;
    }

    public String getMark() {
        return mark;
    }
}
