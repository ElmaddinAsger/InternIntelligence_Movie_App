package com.elmaddinasger.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmaddinasger.movieapp.databinding.ItemMovieSlideBinding
import com.elmaddinasger.movieapp.models.MovieModel

class MovieSlideAdapter: RecyclerView.Adapter<MovieSlideAdapter.MovieSlideViewHolder>() {
    private val diffUtil = object : DiffUtil.ItemCallback<MovieModel>() {
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.movieId == newItem.movieId
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSlideViewHolder {
        val binding = ItemMovieSlideBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieSlideViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: MovieSlideViewHolder, position: Int) {
        val currentMovie = asyncListDiffer.currentList[position]
        holder.bind(currentMovie)
    }

    fun setMovieList(currentMovieList: MutableList<MovieModel>){
        asyncListDiffer.submitList(currentMovieList)
    }

    inner class MovieSlideViewHolder(private val binding: ItemMovieSlideBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentMovie: MovieModel){
            Glide.with(binding.root.context)
                .load(currentMovie.backdropPath)
                .into(binding.imgCurrentMovie)
            binding.txtvwMovieTitle.text = currentMovie.title
        }
    }
}