package com.celt.lms.api;

import android.support.annotation.NonNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiFactory {

    private static final String url = "https://collapsed.space";

    @NonNull
    public static ApiLms getService() {
        return getRetrofit().create(ApiLms.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
