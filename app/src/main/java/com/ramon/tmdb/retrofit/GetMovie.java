package com.ramon.tmdb.retrofit;

import com.ramon.tmdb.models.CreditsFeed;
import com.ramon.tmdb.models.MovieDetail;
import com.ramon.tmdb.models.MovieFeed;
import com.ramon.tmdb.models.MovieNewsFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetMovie {
    @GET("movie/{id}")
    Call<MovieDetail> getMovie(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieFeed> getTop(@Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MovieFeed> getDiscover(@Query("api_key") String apiKey);

    @GET("trending/movie/week")
    Call<MovieNewsFeed> getNews(@Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<CreditsFeed> getCredits(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieFeed> getSearch(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page, @Query("include_adult") boolean include_adult, @Query("query") String query);
}
