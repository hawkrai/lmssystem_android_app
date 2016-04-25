package com.celt.lms.dto;

public class ScheduleProtectionLabs {
    private String date;
    private int scheduleProtectionLabId;
    private int subGroupId;

    public ScheduleProtectionLabs(String date, int scheduleProtectionLabId, int subGroupId) {
        this.date = date;
        this.scheduleProtectionLabId = scheduleProtectionLabId;
        this.subGroupId = subGroupId;
    }

    public String getDate() {
        return date;
    }
}
