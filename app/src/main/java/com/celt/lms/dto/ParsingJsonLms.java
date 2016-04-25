package com.celt.lms.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class ParsingJsonLms {

    private static JSONArray getJsonArray(String json, String nameArray) throws JSONException {
        JSONObject dataJsonObj;
        dataJsonObj = new JSONObject(json);
        String code = dataJsonObj.optString("Code", "");
        String message = dataJsonObj.optString("Message", "");
        return dataJsonObj.getJSONArray(nameArray);
    }

    public static ArrayList<LecturesDTO> getParseLectures(String json) {
        try {
            return getLectures(getJsonArray(json, "Lectures"));
        } catch (JSONException e) {
            return new ArrayList<LecturesDTO>();
        }
    }

    public static ArrayList<LecturesDTO> getLectures(JSONArray lectures) throws JSONException {
        ArrayList<LecturesDTO> lecturesList = new ArrayList<LecturesDTO>();
        for (int i = 0; i < lectures.length(); i++) {
            JSONObject obj = lectures.getJSONObject(i);

            JSONArray attachments = obj.getJSONArray("Attachments");
            ArrayList<Attachment> ats = getAttachments(attachments);

            int duration = obj.optInt("Duration", 0);
            int lecturesId = obj.optInt("LecturesId", 0);
            int order = obj.optInt("Order", 0);
            String pathFile = obj.optString("PathFile", "");
            int subjectId = obj.optInt("SubjectId", 0);
            String theme = obj.optString("Theme", "");

            lecturesList.add(new LecturesDTO(ats, duration, lecturesId, order, pathFile, subjectId, theme));
        }
        return lecturesList;
    }

    public static ArrayList<LabDTO> getParseLabs(String json) {
        try {
            return getLabs(getJsonArray(json, "Labs"));
        } catch (JSONException e) {
            return new ArrayList<LabDTO>();
        }
    }

    private static ArrayList<LabDTO> getLabs(JSONArray labs) throws JSONException {
        ArrayList<LabDTO> labsList = new ArrayList<LabDTO>();
        for (int i = 0; i < labs.length(); i++) {

            JSONObject obj = labs.getJSONObject(i);

            JSONArray attachments = obj.optJSONArray("Attachments");
            ArrayList<Attachment> ats = getAttachments(attachments);

            int duration = obj.optInt("Duration", 0);
            int labId = obj.optInt("LabId", 0);
            int order = obj.optInt("Order", 0);
            String pathFile = obj.optString("PathFile", "");

            JSONArray scheduleProtectionLabsRecomends = obj.optJSONArray("ScheduleProtectionLabsRecomend");
            ArrayList<ScheduleProtectionLabsRecomend> scheduleProtectionLabsRecomend = getScheduleProtectionLabsRecomends(scheduleProtectionLabsRecomends);
            String shortName = obj.optString("ShortName", "");
            int subjectId = obj.optInt("SubjectId", 0);
            String theme = obj.optString("Theme", "");

            labsList.add(new LabDTO(ats, duration, labId, order, pathFile, scheduleProtectionLabsRecomend, shortName, subjectId, theme));
        }
        return labsList;
    }

    private static ArrayList<ScheduleProtectionLabsRecomend> getScheduleProtectionLabsRecomends(JSONArray scheduleProtectionLabsRecomends) {
        ArrayList<ScheduleProtectionLabsRecomend> scheduleProtectionLabsRecomend = new ArrayList<ScheduleProtectionLabsRecomend>();
        if (scheduleProtectionLabsRecomends != null)
            for (int i = 0; i < scheduleProtectionLabsRecomends.length(); i++) {
                JSONObject obj = scheduleProtectionLabsRecomends.optJSONObject(i);
                scheduleProtectionLabsRecomend.add(new ScheduleProtectionLabsRecomend(obj.optString("Mark", ""), obj.optInt("ScheduleProtectionId", 0)));
            }
        return scheduleProtectionLabsRecomend;
    }

    public static ArrayList<Attachment> getAttachments(JSONArray attachments) throws JSONException {
        ArrayList<Attachment> attachmentsList = new ArrayList<Attachment>();
        if (attachments != null)
            for (int j = 0; j < attachments.length(); j++) {
                JSONObject obj = attachments.getJSONObject(j);

                int id = obj.getInt("Id");
                int attachmentType = obj.getInt("AttachmentType");
                String fileName = obj.optString("FileName", "");
                String name = obj.optString("Name", "");
                String pathName = obj.optString("PathName", "");

                attachmentsList.add(new Attachment(id, attachmentType, fileName, name, pathName));
            }
        return attachmentsList;
    }

    public static ArrayList<GroupDTO> getParseGroup(String json) {
        try {
            return getGroups(getJsonArray(json, "Groups"));
        } catch (JSONException e) {
            return new ArrayList<GroupDTO>();
        }
    }

    private static ArrayList<GroupDTO> getGroups(JSONArray jsonGroups) throws JSONException {
        ArrayList<GroupDTO> groupList = new ArrayList<GroupDTO>();
        if (jsonGroups != null)
            for (int i = 0; i < jsonGroups.length(); i++) {
                JSONObject obj = jsonGroups.getJSONObject(i);

                int grouptId = obj.optInt("GroupId", 0);
                String groupName = obj.optString("GroupName", "");
                JSONArray lecturesMarkVisiting = obj.getJSONArray("LecturesMarkVisiting");
                ArrayList<LecturesMarkVisiting> lecturesMarkVisitingList = getLecturesMarkVisiting(lecturesMarkVisiting);
                JSONArray jsonArrayScheduleProtectionPracticals = obj.getJSONArray("ScheduleProtectionPracticals");     //null
                ArrayList<ScheduleProtectionPracticals> scheduleProtectionPracticals = null;
                JSONArray jsonArrayStudents = obj.getJSONArray("Students");
                ArrayList<Student> students = getStudents(jsonArrayStudents);
                SubGroup subGroupsOne = getSubGroup(obj.getJSONObject("SubGroupsOne"));
                SubGroup subGroupsTwo = getSubGroup(obj.getJSONObject("SubGroupsTwo"));

                groupList.add(new GroupDTO(grouptId, groupName, lecturesMarkVisitingList, scheduleProtectionPracticals, students, subGroupsOne, subGroupsTwo));
            }
        return groupList;
    }

    private static SubGroup getSubGroup(JSONObject obj) throws JSONException {
        int groupId = obj.optInt("GroupId", 0);
        List<LabDTO> labs = getLabs(obj.optJSONArray("Labs"));
        String name = obj.optString("Name", "");
        List<ScheduleProtectionLabs> scheduleProtectionLabs = getScheduleProtectionLabs(obj.optJSONArray("ScheduleProtectionLabs"));
        List<ScheduleProtectionLabsRecomendMark> scheduleProtectionLabsRecomendMark = null; // null
        List<Student> students = getStudents(obj.getJSONArray("Students"));
        int subGroupId = obj.optInt("SubGroupId", 0);
        return new SubGroup(groupId, labs, name, scheduleProtectionLabs, scheduleProtectionLabsRecomendMark, students, subGroupId);
    }

    private static ArrayList<ScheduleProtectionLabs> getScheduleProtectionLabs(JSONArray jsonArrayScheduleProtectionLabs) {
        ArrayList<ScheduleProtectionLabs> scheduleProtectionLabs = new ArrayList<ScheduleProtectionLabs>();
        for (int i = 0; i < jsonArrayScheduleProtectionLabs.length(); i++) {
            JSONObject obj = jsonArrayScheduleProtectionLabs.optJSONObject(i);

            String date = obj.optString("Date");
            int scheduleProtectionLabId = obj.optInt("ScheduleProtectionLabId", 0);
            int subGroupId = obj.optInt("SubGroupId", 0);

            scheduleProtectionLabs.add(new ScheduleProtectionLabs(date, scheduleProtectionLabId, subGroupId));
        }
        return scheduleProtectionLabs;
    }

    private static ArrayList<LecturesMarkVisiting> getLecturesMarkVisiting(JSONArray lecturesMarkVisiting) throws JSONException {
        ArrayList<LecturesMarkVisiting> lecturesMarkVisitingList = new ArrayList<LecturesMarkVisiting>();
        for (int i = 0; i < lecturesMarkVisiting.length(); i++) {
            JSONObject obj = lecturesMarkVisiting.getJSONObject(i);

            String login = obj.optString("Login", "");
            JSONArray Marks = obj.getJSONArray("Marks");
            ArrayList<Mark> marksList = getMarks(Marks);
            int studentId = obj.optInt("StudentId", 0);
            String studentName = obj.getString("StudentName");

            lecturesMarkVisitingList.add(new LecturesMarkVisiting(login, marksList, studentId, studentName));
        }
        return lecturesMarkVisitingList;
    }

    private static ArrayList<Student> getStudents(JSONArray students) throws JSONException {
        ArrayList<Student> studentList = new ArrayList<Student>();
        for (int i = 0; i < students.length(); i++) {
            JSONObject obj = students.getJSONObject(i);
            JSONArray jsonArrayFileLabs = obj.optJSONArray("FileLabs");
            ArrayList<FileLab> filelabs = getFileLabs(jsonArrayFileLabs);

            String fullName = obj.optString("FullName", "");
            int groupId = obj.optInt("GroupId", 0);
            JSONArray jsonArrayLabVisitingMark = obj.getJSONArray("LabVisitingMark");
            ArrayList<LabVisitingMark> labVisitingMark = getLabVisitingMark(jsonArrayLabVisitingMark);

            String labsMarkTotal = obj.optString("LabsMarkTotal", "");
            String login = obj.optString("Login", "");
            String practicalMarkTotal = obj.optString("PracticalMarkTotal", "");
            JSONArray jsonArrayPracticalVisitingMark = obj.getJSONArray("PracticalVisitingMark"); //null
            ArrayList<PracticalVisitingMark> practicalVisitingMark = new ArrayList<PracticalVisitingMark>();

            int studentId = obj.optInt("StudentId", 0);
            JSONArray jsonArrayStudentLabMarks = obj.getJSONArray("StudentLabMarks");
            ArrayList<StudentLabMark> studentLabMarks = getStudentLabMarks(jsonArrayStudentLabMarks);

            JSONArray jsonArrayStudentPracticalMarks = obj.getJSONArray("StudentPracticalMarks"); //null
            ArrayList<StudentPracticalMark> studentPracticalMarks = new ArrayList<StudentPracticalMark>();

            String testMark = obj.optString("TestMark", "");
            studentList.add(new Student(filelabs, fullName, groupId, labVisitingMark, labsMarkTotal, login, practicalMarkTotal, practicalVisitingMark, studentId, studentLabMarks, studentPracticalMarks, testMark));
        }
        return studentList;
    }

    private static ArrayList<Mark> getMarks(JSONArray marks) throws JSONException {
        ArrayList<Mark> marksList = new ArrayList<Mark>();
        if (marks != null)
            for (int i = 0; i < marks.length(); i++) {
                JSONObject obj = marks.getJSONObject(i);
                String date = obj.optString("Date", "");
                int LecuresVisitId = obj.optInt("LecuresVisitId", 0);
                String mark = obj.getString("Mark");
                int markId = obj.optInt("MarkId", 0);

                marksList.add(new Mark(date, LecuresVisitId, mark, markId));
            }
        return marksList;
    }

    private static ArrayList<FileLab> getFileLabs(JSONArray fileLabs) throws JSONException {
        ArrayList<FileLab> fileLabList = new ArrayList<FileLab>();
        if (fileLabs != null)
            for (int i = 0; i < fileLabs.length(); i++) {
                JSONObject obj = fileLabs.getJSONObject(i);

                ArrayList<Attachment> attachments = getAttachments(obj.optJSONArray("Attachments"));
                String comment = obj.optString("Comments", "");
                int id = obj.optInt("Id", 0);
                String pathFile = obj.optString("PathFile", "");

                fileLabList.add(new FileLab(attachments, comment, id, pathFile));
            }

        return fileLabList;
    }

    private static ArrayList<LabVisitingMark> getLabVisitingMark(JSONArray labVisitingMark) throws JSONException {
        ArrayList<LabVisitingMark> labVisitingMarksList = new ArrayList<LabVisitingMark>();
        for (int i = 0; i < labVisitingMark.length(); i++) {
            JSONObject obj = labVisitingMark.getJSONObject(i);

            String comment = obj.optString("Comment", "");
            int labVisitingMarkId = obj.optInt("LabVisitingMarkId", 0);
            String mark = obj.optString("Mark", "");
            int scheduleProtectionLabId = obj.optInt("ScheduleProtectionLabId", 0);
            int studentId = obj.optInt("StudentId", 0);
            String studentName = obj.optString("StudentName", "");

            labVisitingMarksList.add(new LabVisitingMark(comment, labVisitingMarkId, mark, scheduleProtectionLabId, studentId, studentName));
        }
        return labVisitingMarksList;
    }

    private static ArrayList<StudentLabMark> getStudentLabMarks(JSONArray studentLabMarks) throws JSONException {
        ArrayList<StudentLabMark> StudentLabMarkList = new ArrayList<StudentLabMark>();
        for (int i = 0; i < studentLabMarks.length(); i++) {
            JSONObject obj = studentLabMarks.getJSONObject(i);

            int labId = obj.optInt("LabId", 0);
            String mark = obj.optString("Mark", "");
            int studentId = obj.optInt("StudentId", 0);
            int studentLabMarkId = obj.optInt("StudentLabMarkId", 0);

            StudentLabMarkList.add(new StudentLabMark(labId, mark, studentId, studentLabMarkId));
        }
        return StudentLabMarkList;
    }

    public static ArrayList<NewsDTO> getParseNews(String json) {
        try {
            return getNews(getJsonArray(json, "News"));
        } catch (JSONException e) {
            return new ArrayList<NewsDTO>();
        }
    }

    private static ArrayList<NewsDTO> getNews(JSONArray news) throws JSONException {
        ArrayList<NewsDTO> newsList = new ArrayList<NewsDTO>();
        for (int i = 0; i < news.length(); i++) {
            JSONObject obj = news.getJSONObject(i);

            String body = obj.optString("Body", "");
            String dateCreate = obj.optString("DateCreate", "");
            int newsId = obj.optInt("NewsId", 0);
            int subjectId = obj.optInt("SubjectId", 0);
            String title = obj.optString("Title", "");

            newsList.add(new NewsDTO(body, dateCreate, newsId, subjectId, title));
        }
        return newsList;
    }
}
