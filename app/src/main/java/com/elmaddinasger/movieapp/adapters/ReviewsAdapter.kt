package com.elmaddinasger.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmaddinasger.movieapp.databinding.ItemReviewBinding
import com.elmaddinasger.movieapp.models.ResultXX

class ReviewsAdapter: RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ResultXX>(){
        override fun areItemsTheSame(oldItem: ResultXX, newItem: ResultXX): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultXX, newItem: ResultXX): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewsViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size


    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        val currentReview = asyncListDiffer.currentList[position]
        holder.bind(currentReview)
    }

    fun setList (reviewList: MutableList<ResultXX>){
        asyncListDiffer.submitList(reviewList)
    }

    inner class ReviewsViewHolder( val binding : ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ResultXX) {
            binding.textAuthor.text = review.author
            binding.textContent.text = review.content
            binding.ratingBar.rating = review.author_details.rating.toFloat()
            binding.textDate.text = review.created_at

            review.author_details.avatar_path.let {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w200$it")
                    .into(binding.imageAvatar)
            }
        }
    }
}