package com.elmaddinasger.movieapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmaddinasger.movieapp.models.CategoryModel
import com.elmaddinasger.movieapp.models.Genre
import com.elmaddinasger.movieapp.models.Genres
import com.elmaddinasger.movieapp.models.MovieDetailsModel
import com.elmaddinasger.movieapp.models.MovieVideosModel
import com.elmaddinasger.movieapp.models.OnlineCategoryModel
import com.elmaddinasger.movieapp.models.RatingRequest
import com.elmaddinasger.movieapp.models.ReviewsModel
import com.elmaddinasger.movieapp.services.Retrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val _selectedMovieId = MutableLiveData<Int?>()
    val selectedMovieId: LiveData<Int?> = _selectedMovieId

    fun selectMovie(movieId: Int) {
        _selectedMovieId.value = movieId
        fetchMovieDetails(movieId)
        fetchMovieVideos(movieId)
        fetchSimilarMovies(movieId)
        fetchReviews(movieId)
    }


    private val _movieDetails = MutableStateFlow<MovieDetailsModel?>(null)
    val movieDetails: StateFlow<MovieDetailsModel?> = _movieDetails

    private val _movieVideos = MutableStateFlow<MovieVideosModel?>(null)
    val movieVideos: StateFlow<MovieVideosModel?> = _movieVideos

    private val _similarMovies = MutableStateFlow<OnlineCategoryModel?>(null)
    val similarMovies: StateFlow<OnlineCategoryModel?> = _similarMovies

    private val _reviews = MutableStateFlow<ReviewsModel?>(null)
    val reviews: StateFlow<ReviewsModel?> = _reviews



    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = Retrofit.movieApi.getMovieDetails(movieId)
                if (response.isSuccessful) {
                    val movie = response.body()
                    Log.e("ViewModel", "Veri alındı: $movie")
                    _movieDetails.value = movie
                } else {
                    Log.e("ViewModel", "API başarısız: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Hata: ${e.message}")
            }
        }
    }

    fun fetchMovieVideos( movieId: Int){
        viewModelScope.launch {
            try {
                val response = Retrofit.movieApi.getMovieVideos(movieId)
                if (response.isSuccessful) {
                    val currentMovieVideos = response.body()
                    _movieVideos.value = currentMovieVideos
                } else {
                    Log.e("ViewModel", "API başarısız: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Hata: ${e.message}")
            }
        }
    }

    fun fetchSimilarMovies( movieId: Int){
        viewModelScope.launch {
            try {
                val response = Retrofit.movieApi.getSimilarMovies(movieId,"similar")
                if (response.isSuccessful) {
                    val currentSimilarMovies = response.body()
                    _similarMovies.value = currentSimilarMovies
                } else {
                    Log.e("ViewModel", "API başarısız: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Hata: ${e.message}")
            }
        }
    }

    fun fetchReviews( movieId: Int){
        viewModelScope.launch {
            try {
                val response = Retrofit.movieApi.getReviews(movieId)
                if (response.isSuccessful) {
                    val currentReviews = response.body()
                    _reviews.value = currentReviews
                } else {
                    Log.e("ViewModel", "API başarısız: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Hata: ${e.message}")
            }
        }
    }

    fun rateMovie(movieId: Int, rating: Double) {
        viewModelScope.launch {
            try {
                val response = Retrofit.movieApi.rateMovie(movieId, rating = RatingRequest(rating))
                if (response.isSuccessful) {
                    Log.e("RateMovie", "Puan gönderildi: $rating")
                } else {
                    Log.e("RateMovie", "Başarısız: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RateMovie", "Hata: ${e.message}")
            }
        }
    }

}
