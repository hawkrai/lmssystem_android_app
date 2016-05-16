package com.celt.lms.dto;

import org.json.JSONException;
import org.json.JSONObject;

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

    JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", id);
            jsonObject.put("AttachmentType", attachmentType);
            jsonObject.put("FileName", fileName);
            jsonObject.put("Name", name);
            jsonObject.put("PathName", pathName);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }
}
