package com.example.minhd.ezhome.interact;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ducnd on 5/21/17.
 */

class ApiConnector {
    private static ApiConnector instance = new ApiConnector();
    private ApiService apiService;

    private ApiConnector() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit.Builder retrofit = new Retrofit.Builder();
        retrofit.baseUrl("http://j.ginggong.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build());
        apiService = retrofit.build().create(ApiService.class);
    }

    static ApiConnector getInstance() {
        return instance;
    }

}
