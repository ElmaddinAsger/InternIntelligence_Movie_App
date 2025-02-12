package com.elmaddinasger.movieapp

sealed class CategoryNameSealed (val resId: Int) {
    object TopRated : CategoryNameSealed(R.string.top_rated)
    object NowPlaying : CategoryNameSealed(R.string.now_playing)
    object Popular : CategoryNameSealed(R.string.popular)
    object Upcoming : CategoryNameSealed(R.string.upcoming)

    companion object {
        fun fromName(name: String): Int? {
            return when (name.lowercase()) {
                "top_rated" -> TopRated.resId
                "now_playing" -> NowPlaying.resId
                "popular" -> Popular.resId
                "upcoming" -> Upcoming.resId
                else -> null
            }
        }
    }
}