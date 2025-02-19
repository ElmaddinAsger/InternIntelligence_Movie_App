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
import com.elmaddinasger.movieapp.services.Retrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val _selectedMovieId = MutableLiveData<Int>()
    val selectedMovieId: LiveData<Int> = _selectedMovieId

    fun selectMovie(movieId: Int) {
        _selectedMovieId.value = movieId
        fetchMovieDetails(movieId)
        fetchMovieVideos(movieId)
    }

    private val _movieDetails = MutableStateFlow<MovieDetailsModel?>(null)
    val movieDetails: StateFlow<MovieDetailsModel?> = _movieDetails

    private val _movieVideos = MutableStateFlow<MovieVideosModel?>(null)
    val movieVideos: StateFlow<MovieVideosModel?> = _movieVideos




    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = Retrofit.movieApi.getMovieDetails(movieId)
                if (response.isSuccessful) {
                    val movie = response.body()

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
                    Log.e("ViewModel", "Veri alındı: $movieVideos")
                    _movieVideos.value = currentMovieVideos
                } else {
                    Log.e("ViewModel", "API başarısız: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Hata: ${e.message}")
            }
        }
    }

}
