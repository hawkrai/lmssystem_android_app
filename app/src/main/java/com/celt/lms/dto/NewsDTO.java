package com.celt.lms.dto;

public class NewsDTO {

    private String title;
    private String body;
    private String dateCreate;
    private int newsId;
    private int subjectId;

    public NewsDTO(String body, String dateCreate, int newsId, int subjectId, String title) {
        this.body = body;
        this.dateCreate = dateCreate;
        this.newsId = newsId;
        this.subjectId = subjectId;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return dateCreate;
    }

    public String getBody() {
        return body;
    }
}
