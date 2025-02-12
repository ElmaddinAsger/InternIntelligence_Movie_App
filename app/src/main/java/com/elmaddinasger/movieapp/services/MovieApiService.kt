package com.elmaddinasger.movieapp.services

import com.elmaddinasger.movieapp.models.Genres
import com.elmaddinasger.movieapp.models.OnlineCategoryModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/{type}")
    fun getMovies(
        @Path("type") movieType: String,
        @Header("Authorization") token: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<OnlineCategoryModel>

    @GET("genre/movie/list")
    fun getMovieGenreList(
        @Header("Authorization") token: String,
        @Query("language") language: String = "en",
    ): Call<Genres>
}