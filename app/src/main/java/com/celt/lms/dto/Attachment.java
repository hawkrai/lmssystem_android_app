package com.celt.lms.dto;

class Attachment {
    private int id;
    private int attachmentType;
    private String fileName;
    private String name;
    private String pathName;

    public Attachment(int id, int attachmentType, String fileName, String name, String pathName) {
        this.id = id;
        this.attachmentType = attachmentType;
        this.fileName = fileName;
        this.name = name;
        this.pathName = pathName;
    }
}
