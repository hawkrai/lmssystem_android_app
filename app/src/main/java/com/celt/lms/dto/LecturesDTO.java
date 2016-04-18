package com.celt.lms.dto;

import java.util.List;

public class LecturesDTO {
    private int duration;
    private int lecturesId;
    private int order;
    private String pathFile;
    private int subjectId;
    private String theme;
    private List<Attachment> attachments;

    public LecturesDTO(List<Attachment> attachments, int duration, int lecturesId, int order, String pathFile, int subjectId, String theme) {
        this.attachments = attachments;
        this.duration = duration;
        this.lecturesId = lecturesId;
        this.order = order;
        this.pathFile = pathFile;
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