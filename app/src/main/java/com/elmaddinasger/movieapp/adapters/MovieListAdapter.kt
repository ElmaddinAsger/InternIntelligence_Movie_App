package com.elmaddinasger.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmaddinasger.movieapp.databinding.ItemMovieBinding
import com.elmaddinasger.movieapp.models.MovieModel

class MovieListAdapter: RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<MovieModel>(){
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.movieId == newItem.movieId
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val currentMovie = asyncListDiffer.currentList[position]
        holder.bind(currentMovie)
    }

    fun setList(movieList: List<MovieModel>){
        asyncListDiffer.submitList(movieList)
    }

    inner class MovieListViewHolder(val binding:ItemMovieBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (currentMovie: MovieModel) {
            binding.apply {
                txtvwMovieName.text = currentMovie.title
                txtvwOverview.text = currentMovie.overview
                txtvwReleaseDate.text = currentMovie.releaseDate
                txtvwVoteAverage.text = currentMovie.voteAverage.toString()
                Glide.with(binding.root.context)
                    .load(currentMovie.posterPath)
                    .into(imgMoviePhoto)
            }
        }
    }
}