package com.elmaddinasger.movieapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.elmaddinasger.movieapp.databinding.FragmentSplashScreenBinding
import com.elmaddinasger.movieapp.dto.toMovieList
import com.elmaddinasger.movieapp.dto.toMovieModel
import com.elmaddinasger.movieapp.models.Genre
import com.elmaddinasger.movieapp.models.Genres
import com.elmaddinasger.movieapp.models.MovieModel
import com.elmaddinasger.movieapp.models.OnlineCategoryModel
import com.elmaddinasger.movieapp.services.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var sharedViewModel: SharedViewModel


    private val categoryNameList = listOf("top_rated","popular","upcoming","now_playing")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater,container,false)
        return binding.root
       }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryNameList.forEach{ categoryName ->
            getApiData(categoryNameList.indexOf(categoryName)+1,Retrofit.movieApi.getMovies(categoryName),categoryName)
        }
        getGenreList()

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]



        sharedViewModel.completedRequests.observe(viewLifecycleOwner) { count ->
            if (count == 5) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)

            }
        }
    }

    private fun getApiData (id: Int, call: Call<OnlineCategoryModel>, listName:String) {

        call.enqueue(object : Callback<OnlineCategoryModel> {
            override fun onResponse(call: Call<OnlineCategoryModel>, response: Response<OnlineCategoryModel>) {

                if (response.isSuccessful) {
                    response.body()?.let { onlineMovieList ->

                        val movieList = mutableListOf<MovieModel>()
                        onlineMovieList.results.forEach{result ->
                            val movie = result.toMovieModel()
                            if (!movieList.contains(movie)){
                                movieList.add(movie)
                            }
                        }

                        val categoryModel = onlineMovieList.toMovieList(id,listName,movieList)
                        sharedViewModel.addCategory(categoryModel)
                        sharedViewModel.incrementRequestCount()

                    }
                }
            }

            override fun onFailure(call: Call<OnlineCategoryModel>, t: Throwable) {
                Log.e("RETROFIT","Network error: ${t.message}")
            }
        })
    }

    private fun getGenreList () {
        val call = Retrofit.movieApi.getMovieGenreList()
        call.enqueue(object : Callback<Genres> {
            override fun onResponse(p0: Call<Genres>, response: Response<Genres>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        val genreList =  mutableListOf(Genre(0,"All"))
                        it.genres.forEach {genre ->
                            genreList.add(genre)
                        }
                        sharedViewModel.setGenreList(genreList)
                        sharedViewModel.incrementRequestCount()

                    }
                }
            }

            override fun onFailure(p0: Call<Genres>, t: Throwable) {
                Log.e("RETROFIT","Network error: ${t.message}")
            }
        })

    }

}