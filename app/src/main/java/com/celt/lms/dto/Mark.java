package com.celt.lms.dto;

class Mark {
    private String date;
    private int lecuresVisitId;
    private String mark;
    private int markdId;

    public Mark(String date, int lecuresVisitId, String mark, int markdId) {
        this.date = date;
        this.lecuresVisitId = lecuresVisitId;
        this.mark = mark;
        this.markdId = markdId;
    }
}
