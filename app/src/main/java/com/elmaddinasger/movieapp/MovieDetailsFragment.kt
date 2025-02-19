package com.elmaddinasger.movieapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.elmaddinasger.movieapp.adapters.MovieListAdapter
import com.elmaddinasger.movieapp.adapters.ReviewsAdapter
import com.elmaddinasger.movieapp.adapters.VideoAdapter
import com.elmaddinasger.movieapp.databinding.FragmentMovieDetailsBinding
import com.elmaddinasger.movieapp.dto.toMovieList
import com.elmaddinasger.movieapp.dto.toMovieModel
import com.elmaddinasger.movieapp.models.MovieDetailsModel
import com.elmaddinasger.movieapp.models.MovieModel
import com.elmaddinasger.movieapp.models.OnlineCategoryModel
import com.elmaddinasger.movieapp.models.ResultXX
import com.elmaddinasger.movieapp.services.Retrofit
import com.elmaddinasger.movieapp.viewModels.ForGenreViewModel
import com.elmaddinasger.movieapp.viewModels.MovieViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailsFragment : Fragment() {
   private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieVideosAdapter: VideoAdapter
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var reviewsAdapter: ReviewsAdapter

    private lateinit var detailButtonList: List<AppCompatButton>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater,container,false)
        movieListAdapter = MovieListAdapter{
            findNavController().navigate(R.id.action_movieDetailsFragment_self)
        }
        reviewsAdapter = ReviewsAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = ViewModelProvider(requireActivity())[MovieViewModel::class.java]
        detailButtonList = listOf(binding.btnEpisode,binding.btnSimiliar,binding.btnAbout)
        getMovieDetails()
        getVideos()
        binding.btnSimiliar.setOnClickListener {
            getSimilarMovies()
        }
        binding.btnEpisode.setOnClickListener {
            getVideos()
        }
        binding.btnAbout.setOnClickListener {
            getReviews()
        }



    }

    private fun getMovieDetails () {
        lifecycleScope.launch {
            movieViewModel.movieDetails.collectLatest { movie ->
                if (movie != null) {
                    binding.txtvwMovieName.text = movie.title

                    val movieTime = "0${movie.runtime / 60}:${movie.runtime % 60}"
                    var movieGenres = ""
                    movie.genres.forEach { movieGenres += "${it.name} " }

                    binding.txtvwMovieInfo.text = movie.overview
                    binding.txtvwMovieProperty.text =
                        "${movie.release_date} \u00B7 $movieTime · $movieGenres"
                    Glide.with(binding.root.context)
                        .load("https://image.tmdb.org/t/p/w500${movie.backdrop_path}")
                        .into(binding.imgMovie)

                }
            }
        }
    }

    private fun getVideos () {
        lifecycleScope.launch {
            movieViewModel.movieVideos.collectLatest { movieVideos ->

                if (movieVideos != null) {
                    val videoList = movieVideos.results
                    movieVideosAdapter = VideoAdapter(videoList)
                    binding.rvVideoSimiliarAbout.adapter = movieVideosAdapter
                }
            }
        }
        changeDetailButtonBackground(binding.btnEpisode)
    }

    private fun getSimilarMovies () {
        lifecycleScope.launch {
            movieViewModel.similarMovies.collectLatest { similarMovies ->
                Log.e("MOVIEID", similarMovies.toString())
                if (similarMovies != null) {
                    val resultList = similarMovies.results
                    val movieList = mutableListOf<MovieModel>()
                    resultList.forEach { movieList.add(it.toMovieModel()) }
                    movieListAdapter.setList(movieList)
                    binding.rvVideoSimiliarAbout.adapter = movieListAdapter
                }
            }
        }
        changeDetailButtonBackground(binding.btnSimiliar)
    }

    private fun getReviews () {
        lifecycleScope.launch {
            movieViewModel.reviews.collectLatest { reviews ->
                Log.e("MOVIEID", reviews.toString())
                if (reviews != null) {
                    val currentReviews = mutableListOf<ResultXX>()
                    val resultList = reviews.results
                    resultList.forEach { currentReviews.add(it) }
                    reviewsAdapter.setList(currentReviews)
                    binding.rvVideoSimiliarAbout.adapter = reviewsAdapter
                }
            }
        }
        changeDetailButtonBackground(binding.btnAbout)
    }

    private fun changeDetailButtonBackground (currentButton: AppCompatButton) {
        detailButtonList.forEach { button ->
            if (currentButton == button){
                button.setBackgroundResource(R.drawable.active_movie_detail_button)
            }else {
                button.setBackgroundResource(R.drawable.inactive_movie_details_buttons)
            }
        }

    }

}