package com.celt.lms.api;

import com.google.gson.JsonElement;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiLms {
    @GET("/ServicesLabsLabsService.svcGetLabs{id}.json")
    Call<JsonElement> getLabs(@Path("id") int groupId);

    @GET("/ServicesLecturesLecturesService.svcGetLectures{id}.json")
    Call<JsonElement> getLectures(@Path("id") int groupId);

    @GET("/ServicesNewsNewsService.svcGetNews{id}.json")
    Call<JsonElement> getNews(@Path("id") int groupId);

    @GET("/ServicesCoreService.svcGetGroups{id}.json")
    Call<JsonElement> getGroups(@Path("id") int groupId);

    @Headers("Content-Type: application/json")
    @POST("/Services/News/NewsService.svc/Save")
    Call<JsonElement> addNews(@Body RequestBody json);

    @Headers("Content-Type: application/json")
    @POST("/Services/News/NewsService.svc/Delete")
    Call<JsonElement> deleteNews(@Body RequestBody json);


//    @GET("/Services/Labs/LabsService.svc/GetLabs/{id}")
//    Call<JsonElement> getLabs(@Path("id") int groupId);
//
//    @GET("/Services/Lectures/LecturesService.svc/GetLectures/{id}")
//    Call<JsonElement> getLectures(@Path("id") int groupId);
//
//    @GET("/Services/News/NewsService.svc/GetNews/{id}")
//    Call<JsonElement> getNews(@Path("id") int groupId);
//
//    @GET("/Services/CoreService.svc/GetGroups/{id}")
//    Call<JsonElement> getGroups(@Path("id") int groupId);
//
//    @Headers( "Content-Type: application/json" )
//    @POST("/Services/News/NewsService.svc/Save")
//    Call<JsonElement> addNews(@Body RequestBody json);
//
//    @Headers( "Content-Type: application/json" )
//    @POST("/Services/News/NewsService.svc/Delete")
//    Call<Response> deleteNews(@Body RequestBody json);
}