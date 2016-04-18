package com.celt.lms.dto;

import java.util.List;

class FileLab {
    private List<Attachment> attachments;
    private String commtens;
    private int id;
    private String pathFile;

    public FileLab(List<Attachment> attachments, String commtens, int id, String pathFile) {
        this.attachments = attachments;
        this.commtens = commtens;
        this.id = id;
        this.pathFile = pathFile;
    }
}
