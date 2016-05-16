package com.celt.lms.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Attachments", getAttachmentsJsonArray());
            jsonObject.put("Comments", commtens);
            jsonObject.put("Id", id);
            jsonObject.put("PathFile", pathFile);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    private JSONArray getAttachmentsJsonArray() {
        JSONArray jsonArray = new JSONArray();
        for (Attachment item : attachments) {
            jsonArray.put(item.getJSONObject());
        }
        return jsonArray;
    }
}
