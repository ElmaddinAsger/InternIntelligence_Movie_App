package com.elmaddinasger.movieapp.services

import com.elmaddinasger.movieapp.BuildConfig
import com.elmaddinasger.movieapp.models.Genres
import com.elmaddinasger.movieapp.models.MovieDetailsModel
import com.elmaddinasger.movieapp.models.MovieVideosModel
import com.elmaddinasger.movieapp.models.OnlineCategoryModel
import com.google.gson.internal.GsonBuildConfig
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/{type}")
    fun getMovies(
        @Path("type") movieType: String,
        @Header("Authorization") token: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<OnlineCategoryModel>

    @GET("genre/movie/list")
    fun getMovieGenreList(
        @Header("Authorization") token: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en",
    ): Call<Genres>


    @GET("movie/{type}")
    suspend fun getMovieDetails(
        @Path("type") movieId: Int,
        @Header("Authorization") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
    ): Response<MovieDetailsModel>

    @GET("movie/{movieId}/videos")
    suspend fun getMovieVideos(
        @Path("movieId") movieId: Int,
        @Header("Authorization") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US",
    ): Response<MovieVideosModel>
}