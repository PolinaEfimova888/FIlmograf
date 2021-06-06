package com.example.filmograf;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Manager {

    @GET("/search")
    Call<Movies> search(@Query("q") String q, @Query("app_id") String app_id, @Query("app_key") String app_key);

//    @GET("/anime/id/episodes")
//    Call<Movies> getFilm(@Query("id") String id,@Query("request") String request,@Query("parameter") String parameter  );

    @GET("/movie/{movie_id}")
    Call<Movie> getFilm(@Path ("movie_id") String movie_id, @Query("api_key") String api_key, @Query("language") String language);

    @GET("/3/movie/popular")
    Call<Movies> getPopular(@Query("api_key") String api_key,@Query("language") String language, @Query("page") int page);
}