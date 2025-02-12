package com.elmaddinasger.movieapp.dto

import com.elmaddinasger.movieapp.models.CategoryModel
import com.elmaddinasger.movieapp.models.MovieModel
import com.elmaddinasger.movieapp.models.OnlineCategoryModel
import com.elmaddinasger.movieapp.models.Result


fun Result.toMovieModel(): MovieModel {
    return MovieModel(
        adult = adult,
        backdropPath= "https://image.tmdb.org/t/p/w500$backdrop_path",
        genreIds= genre_ids,
        movieId= id,
        originalLanguage= original_language,
        originalTitle= original_title,
        overview= overview,
        popularity= popularity,
        posterPath= "https://image.tmdb.org/t/p/w185$poster_path",
        releaseDate= release_date,
        title= title,
        video= video,
        voteAverage= vote_average,
        voteCount= vote_count,
    )
}
fun OnlineCategoryModel.toMovieList(id: Int,listName: String, movieList: List<MovieModel>): CategoryModel {
    val currentMovieList = mutableListOf<MovieModel>()
    movieList.forEach { movie ->
        currentMovieList.add(movie)
    }
    return CategoryModel(
        id = id,
        listName = listName,
        page = page,
        movieList = currentMovieList,
        totalPages = total_pages,
        totalResults= total_results
    )
}