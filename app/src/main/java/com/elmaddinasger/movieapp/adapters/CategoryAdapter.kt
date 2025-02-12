package com.elmaddinasger.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elmaddinasger.movieapp.CategoryNameSealed
import com.elmaddinasger.movieapp.databinding.ItemCategoryBinding
import com.elmaddinasger.movieapp.models.CategoryModel

class CategoryAdapter(val context: Context,val seeAll: (categoryId: Int) -> Unit): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private val diffUtil = object : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem
        }
    }
    private val asyncListDiffer = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCover = asyncListDiffer.currentList[position]
        holder.bind(currentCover)
    }
    fun setList(currentCategoryList: MutableList<CategoryModel>) {
        asyncListDiffer.submitList(currentCategoryList)
    }
    inner class CategoryViewHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.txtvwSeeAll.setOnClickListener{
                seeAll(asyncListDiffer.currentList[bindingAdapterPosition].id)
            }
        }
        fun bind (currentCategory: CategoryModel){
            val stringResId = CategoryNameSealed.fromName(currentCategory.listName)
            binding.apply {
                txtvwSeeAll.text = "See all"
                txtvwCoverName.text = stringResId?.let { context.getString(it) } ?: ""
                val moviePosterAdapter = MoviePosterAdapter()
                moviePosterAdapter.setMovieList(currentCategory.movieList)
                rvMovies.adapter = moviePosterAdapter
            }

        }
    }

}