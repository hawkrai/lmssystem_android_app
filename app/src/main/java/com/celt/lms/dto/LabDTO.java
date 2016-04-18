package com.celt.lms.dto;

import java.util.List;

public class LabDTO {
    private int duration;
    private int labId;
    private int order;
    private String pathFile;
    private List<ScheduleProtectionLabsRecomend> scheduleProtectionLabsRecomend;
    private String shortName;
    private int subjectId;
    private String theme;
    private List<Attachment> attachments;

    public LabDTO(List<Attachment> attachments, int duration, int labId, int order, String pathFile, List<ScheduleProtectionLabsRecomend> scheduleProtectionLabsRecomend, String shortName, int subjectId, String theme) {
        this.attachments = attachments;
        this.duration = duration;
        this.labId = labId;
        this.order = order;
        this.pathFile = pathFile;
        this.scheduleProtectionLabsRecomend = scheduleProtectionLabsRecomend;
        this.shortName = shortName;
        this.subjectId = subjectId;
        this.theme = theme;
    }

    public int getDuration() {
        return duration;
    }

    public int getOrder() {
        return order;
    }

    public String getTheme() {
        return theme;
    }
}
