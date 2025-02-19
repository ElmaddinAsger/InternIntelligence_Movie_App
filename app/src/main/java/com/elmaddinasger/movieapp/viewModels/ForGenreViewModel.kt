package com.elmaddinasger.movieapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elmaddinasger.movieapp.models.CategoryModel

class ForGenreViewModel : ViewModel() {


    private val _categoryListForGenre = MutableLiveData<MutableList<CategoryModel>>(mutableListOf())
    val categoryListForGenre: MutableLiveData<MutableList<CategoryModel>> get() = _categoryListForGenre

    fun addCategoryForGenre (newCategory: CategoryModel) {

        val currentList = _categoryListForGenre.value ?: mutableListOf()
        currentList.add(newCategory)
        _categoryListForGenre.value = currentList
    }




}