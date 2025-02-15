package com.elmaddinasger.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmaddinasger.movieapp.databinding.ItemMoviePosterBinding
import com.elmaddinasger.movieapp.models.MovieModel

class MoviePosterAdapter(
    val clickedMovie: (movieId: Int) -> Unit
): RecyclerView.Adapter<MoviePosterAdapter.CoverMovieViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<MovieModel>() {
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.movieId == newItem.movieId
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverMovieViewHolder {
        val binding = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CoverMovieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: CoverMovieViewHolder, position: Int) {
        val currentMovie = asyncListDiffer.currentList[position]
        holder.bind(currentMovie)
    }

    fun setMovieList (currentMovieList: MutableList<MovieModel>){
        asyncListDiffer.submitList(currentMovieList)
    }

    inner class CoverMovieViewHolder(private val binding: ItemMoviePosterBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.imgbtnMovie.setOnClickListener {
                clickedMovie(asyncListDiffer.currentList[bindingAdapterPosition].movieId)
            }
        }

        fun bind(currentMovie: MovieModel){
            Glide.with(binding.root.context)
                .load(currentMovie.posterPath)
                .into(binding.imgbtnMovie)
        }
    }

}