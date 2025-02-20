package com.elmaddinasger.movieapp.models
import com.squareup.moshi.Json

data class RatingRequest(
    @Json(name = "value") val value: Double
)