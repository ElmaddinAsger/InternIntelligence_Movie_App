package com.elmaddinasger.movieapp.models

data class ReviewsModel(
    val id: Int,
    val page: Int,
    val results: List<ResultXX>,
    val total_pages: Int,
    val total_results: Int
)