package com.elmaddinasger.movieapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elmaddinasger.movieapp.models.CategoryModel
import com.elmaddinasger.movieapp.models.Genre

class SharedViewModel: ViewModel() {

    private val _genreList =  MutableLiveData<List<Genre>>()
    val genreList: LiveData<List<Genre>> get() = _genreList

    private val _categoryList = MutableLiveData<MutableList<CategoryModel>>(mutableListOf())
    val categoryList: MutableLiveData<MutableList<CategoryModel>> get() = _categoryList


    private val _completedRequests = MutableLiveData(0)
    val completedRequests: LiveData<Int> get() = _completedRequests

    fun addCategory(movieListModel: CategoryModel) {
        val currentList = _categoryList.value ?: mutableListOf()
        currentList.add(movieListModel)
        _categoryList.value = currentList
    }

    fun setGenreList(genreList: List<Genre>) {
        _genreList.value = genreList
    }

    fun incrementRequestCount() {
        _completedRequests.value = (_completedRequests.value ?: 0) + 1
    }
}