package com.celt.lms.dto;

public class NewsDTO {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NewsDTO(String title) {
        this.title = title;
    }
}
