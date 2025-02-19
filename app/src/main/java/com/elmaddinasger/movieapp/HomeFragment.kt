package com.elmaddinasger.movieapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.elmaddinasger.movieapp.adapters.CategoryAdapter
import com.elmaddinasger.movieapp.adapters.GenreAdapter
import com.elmaddinasger.movieapp.adapters.MovieSlideAdapter
import com.elmaddinasger.movieapp.databinding.FragmentHomeBinding
import com.elmaddinasger.movieapp.dto.toMovieModel
import com.elmaddinasger.movieapp.models.CategoryModel
import com.elmaddinasger.movieapp.models.Genre
import com.elmaddinasger.movieapp.models.MovieModel
import com.elmaddinasger.movieapp.models.OnlineCategoryModel
import com.elmaddinasger.movieapp.services.Retrofit
import com.elmaddinasger.movieapp.viewModels.ForGenreViewModel
import com.elmaddinasger.movieapp.viewModels.MovieViewModel
import com.elmaddinasger.movieapp.viewModels.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
   private lateinit var binding: FragmentHomeBinding
   private lateinit var movieSlideAdapter: MovieSlideAdapter
   private lateinit var genreAdapter: GenreAdapter
   private lateinit var categoryAdapter: CategoryAdapter
   private lateinit var sharedViewModel: SharedViewModel
   private lateinit var forGenreViewModel: ForGenreViewModel
   private lateinit var movieViewModel: MovieViewModel

    private var currentGenreId = 0
    private lateinit var generalCategoryList: MutableList<CategoryModel>
    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0
    private val sliderRunnable = object : Runnable {
        override fun run() {
            if (movieSlideAdapter.itemCount == 0) return

            currentIndex = (currentIndex + 1) % movieSlideAdapter.itemCount

            binding.rvHeaderMovieSlide.smoothScrollToPosition(currentIndex)
            handler.postDelayed(this, 5000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        movieSlideAdapter = MovieSlideAdapter()
        categoryAdapter = CategoryAdapter(requireContext(),{
            val action = HomeFragmentDirections.actionHomeFragmentToMovieListFragment(it,currentGenreId)
            findNavController().navigate(action)
        },{
            movieViewModel = ViewModelProvider(requireActivity())[MovieViewModel::class.java]
            movieViewModel.selectMovie(it)
            lifecycleScope.launch {
                movieViewModel.movieDetails.collectLatest { movie ->
                    if (movie != null) {
                        val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment()
                        findNavController().navigate(action)
                    }
                }
            }


        })
        return binding.root
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        forGenreViewModel = ViewModelProvider(requireActivity())[ForGenreViewModel::class.java]


        sharedViewModel.categoryList.observe(viewLifecycleOwner) { categoryList ->
            if (categoryList != null) {
                getSlideAdapter(categoryList)
                getCategoryAdapter(categoryList)
                categoryList.forEach{ category ->
                    getApiData(category,Retrofit.movieApi.getMovies(category.listName, page = 2))
                    getApiData(category,Retrofit.movieApi.getMovies(category.listName, page = 3))
                }
                generalCategoryList = categoryList
                getCategoryAdapter(categoryList)

            }

        }
        sharedViewModel.genreList.observe(viewLifecycleOwner) { genreList ->
            if (genreList != null) {
                getGenreRv(genreList)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(sliderRunnable) // For Memory leak
    }



    private fun  getSlideAdapter ( categoryList: MutableList<CategoryModel>) {
        val currentCategoryList = mutableListOf<MovieModel>()
        for (index in 0 ..6) {
            currentCategoryList.add(categoryList[0].movieList[index])
        }

        movieSlideAdapter.setMovieList(currentCategoryList)
        binding.rvHeaderMovieSlide.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHeaderMovieSlide.adapter = movieSlideAdapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvHeaderMovieSlide)
        addDots(currentCategoryList.size)
        updateDots(0)
        binding.rvHeaderMovieSlide.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    updateDots(position)
                }
            }
        })
        handler.postDelayed(sliderRunnable, 5000)
    }

    private fun getCategoryAdapter (currentCategoryList: MutableList<CategoryModel>){
        categoryAdapter.setList(currentCategoryList)
        binding.rvCategory.adapter = categoryAdapter
    }

    private fun getGenreRv (genreList: List<Genre>){
        genreAdapter = GenreAdapter(genreList){genreId ->
            setRecViewForGenre(genreId)
            currentGenreId = genreId
        }
        binding.rvGenre.adapter =genreAdapter

    }

    private fun setRecViewForGenre (genreId: Int){

        generalCategoryList.forEach{currentCategory ->
            val movieListForGenre = mutableListOf<MovieModel>()
            currentCategory.movieList.forEach {movie ->
                if (movie.genreIds.contains(genreId) && !movieListForGenre.contains(movie)){
                    movieListForGenre.add(movie)

                }
            }
            val categoryForGenre = with(currentCategory){
                CategoryModel(id,listName,page,movieListForGenre,totalPages,totalResults)
            }

            Log.e("RETROFIT",categoryForGenre.toString())
            forGenreViewModel.addCategoryForGenre(categoryForGenre)
        }

        forGenreViewModel.categoryListForGenre.observe(viewLifecycleOwner){categoryListForGenre ->
            if (categoryListForGenre != null) {
                categoryAdapter.setList(categoryListForGenre)
            }
        }
    }

    private fun updateDots(position: Int) {
        for (i in 0 until binding.dotsContainer.childCount) {
            val dot = binding.dotsContainer.getChildAt(i) as ImageView
            if (i == position) {
                dot.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.active_dot))
            } else {
                dot.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.inactive_dot))
            }
        }
    }

    private fun addDots(count: Int) {
        binding.dotsContainer.removeAllViews()
        for (i in 0 until count) {
            val dot = ImageView(requireContext())
            dot.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.inactive_dot))

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.marginStart = 20
            params.marginEnd = 20
            dot.layoutParams = params

            binding.dotsContainer.addView(dot)
        }
    }

    private fun getApiData ( category: CategoryModel, call: Call<OnlineCategoryModel>) {

        call.enqueue(object : Callback<OnlineCategoryModel> {
            override fun onResponse(call: Call<OnlineCategoryModel>, response: Response<OnlineCategoryModel>) {

                if (response.isSuccessful) {
                    response.body()?.let { onlineMovieList ->

                        onlineMovieList.results.forEach{result ->
                            val movie = result.toMovieModel()

                            if(!category.movieList.contains(movie)){
                                category.movieList.add(movie)
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<OnlineCategoryModel>, t: Throwable) {
                Log.e("RETROFIT","Network error: ${t.message}")
            }
        })
    }

}