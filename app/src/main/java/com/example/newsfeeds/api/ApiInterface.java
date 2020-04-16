package com.example.newsfeeds.api;

import com.example.newsfeeds.models.News;
import com.example.newsfeeds.util.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET(Constant.GET_TOP_HEADLINES)
    Call<News> getNews (
            @Query(Constant.QUERRY_COUNTRY) String country ,
            @Query(Constant.QUERRY_API_KEY) String apiKey
    );

    @GET(Constant.GET_EVERYTHING)
    Call<News> getNewsSearch (
            @Query(Constant.QUERRY_KEY_WORK) String keyWord ,
            //@Query("language") String language ,
            @Query(Constant.QUERRY_SORT_BY) String sortBy ,
            @Query(Constant.QUERRY_API_KEY) String apiKey
    );
}
