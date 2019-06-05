package com.ramon.tmdb.retrofit;

import com.ramon.tmdb.models.CreditsFeed;
import com.ramon.tmdb.models.TVShowDetail;
import com.ramon.tmdb.models.TVShowFeed;
import com.ramon.tmdb.models.TVShowNewsFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetTVShow {
    @GET("tv/{id}")
    Call<TVShowDetail> getTVShow(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("tv/top_rated")
    Call<TVShowFeed> getTop(@Query("api_key") String apiKey);

    @GET("discover/tv")
    Call<TVShowFeed> getDiscover(@Query("api_key") String apiKey);

    @GET("trending/tv/week")
    Call<TVShowNewsFeed> getNews(@Query("api_key") String apiKey);

    @GET("tv/{id}/credits")
    Call<CreditsFeed> getCredits(@Path("id") int id, @Query("api_key") String apiKey);
}
