package com.celt.lms.api;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiLms {
    @GET("/ServicesLabsLabsService.svcGetLabs{id}.json")
    Call<JsonElement> getLabs(@Path("id") int groupId);

    @GET("/ServicesLecturesLecturesService.svcGetLectures{id}.json")
    Call<JsonElement> getLectures(@Path("id") int groupId);

    @GET("/ServicesNewsNewsService.svcGetNews{id}.json")
    Call<JsonElement> getNews(@Path("id") int groupId);

    @GET("/ServicesCoreService.svcGetGroups{id}.json")
    Call<JsonElement> getGroups(@Path("id") int groupId);
}