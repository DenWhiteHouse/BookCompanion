package com.example.android.bookcompanion.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {
    static String ROOT_URL = "https://www.googleapis.com/books/v1/";
    static BooksInterface BooksInterface;

    public static BooksInterface getRetrofitInstance() {
        Gson gson = new GsonBuilder().create();
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        BooksInterface = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build().create(BooksInterface.class);
        return BooksInterface;
    }
}
