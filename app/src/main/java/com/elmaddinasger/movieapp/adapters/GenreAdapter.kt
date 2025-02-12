package com.elmaddinasger.movieapp.adapters

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmaddinasger.movieapp.R
import com.elmaddinasger.movieapp.databinding.ItemGenreBinding
import com.elmaddinasger.movieapp.models.Genre

class GenreAdapter(private val categories: List<Genre>,
                   private val onItemClick: (Int) -> Unit
): RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding =ItemGenreBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GenreViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.binding.btnCategory.text = categories[position].name
        holder.bind()
    }

    inner class GenreViewHolder (val binding: ItemGenreBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.btnCategory.setOnClickListener {
                val previousSelectedPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedPosition)
                onItemClick(categories[selectedPosition].id)
            }
        }



        fun bind() {
            if (selectedPosition == bindingAdapterPosition) {
                binding.btnCategory.setBackgroundResource(R.drawable.active_button)
            } else {
                binding.btnCategory.setBackgroundResource(R.drawable.inactive_button)
            }
        }
    }
}