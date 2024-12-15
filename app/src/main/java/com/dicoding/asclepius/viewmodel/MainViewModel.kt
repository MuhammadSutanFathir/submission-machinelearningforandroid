package com.dicoding.asclepius.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class MainViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listNews = MutableLiveData<List<ArticlesItem>>()
    val listNews: LiveData<List<ArticlesItem>> = _listNews

    private val _croppedImageUri = MutableLiveData<Uri?>()
    val croppedImageUri: LiveData<Uri?> = _croppedImageUri

    private val _listHistory = MutableLiveData<List<HistoryEntity>>()
    val listHistory: LiveData<List<HistoryEntity>> = _listHistory


    fun setCroppedImageUri(uri: Uri?) {
        _croppedImageUri.value = uri
    }


    init {
        findNews()
    }

    private fun findNews() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getNews(BuildConfig.API_KEY)
                val filteredArticles = response.articles.filter { article ->
                    article.title != "[Removed]" && article.description != "[Removed]"
                }
                _listNews.value = filteredArticles
            } catch (e: Exception) {
                Log.d("MainViewModel", "findNews: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveImageDetectionHistory(history: HistoryEntity) {
        viewModelScope.launch {
            historyRepository.insertHistory(history)
        }
    }

    fun getAllImageDetectionHistory() {
        viewModelScope.launch {
            historyRepository.getAllHistory().observeForever { histories ->
                _listHistory.value = histories
            }
        }
    }
}