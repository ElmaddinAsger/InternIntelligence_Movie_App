package com.elmaddinasger.movieapp.models

data class CategoryModel(
    val id: Int,
    val listName: String,
    val page: Int,
    val movieList: MutableList<MovieModel>,
    val totalPages: Int,
    val totalResults: Int
)
