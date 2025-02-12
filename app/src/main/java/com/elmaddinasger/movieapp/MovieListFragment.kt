package com.elmaddinasger.movieapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.elmaddinasger.movieapp.adapters.MovieListAdapter
import com.elmaddinasger.movieapp.databinding.FragmentMovieListBinding


class MovieListFragment : Fragment() {
    private lateinit var binding: FragmentMovieListBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var forGenreViewModel: ForGenreViewModel
    private lateinit var movieListAdapter: MovieListAdapter
    private val args by navArgs<MovieListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater,container,false)
        movieListAdapter = MovieListAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMovieList()
    }

    private fun setMovieList () {
        val genreId = args.genreId
        val categoryId = args.categoryId
        if (genreId != 0) {
            Log.e("MovieList",genreId.toString())
            forGenreViewModel = ViewModelProvider(requireActivity())[ForGenreViewModel::class.java]
            forGenreViewModel.categoryListForGenre.observe(viewLifecycleOwner){ categoryList ->
                for (category in categoryList){
                    if (category.id == categoryId){
                        movieListAdapter.setList(category.movieList)
                        binding.rvMovieList.adapter = movieListAdapter
                    }
                }

            }
        }else {
            sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
            sharedViewModel.categoryList.observe(viewLifecycleOwner){ categoryList ->
                for (category in categoryList){
                    if (category.id == categoryId){
                        movieListAdapter.setList(category.movieList)
                        binding.rvMovieList.adapter = movieListAdapter
                    }
                }

            }
        }
    }



}