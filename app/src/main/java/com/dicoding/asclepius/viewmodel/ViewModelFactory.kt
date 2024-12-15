package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.helper.Injection

class ViewModelFactory(
    private val historyRepository: HistoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(historyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class data : ${modelClass.name} please create new ViewModel at factory")
        }
    }

    companion object {
        @Volatile
        private var instance : ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                val historyRepository = Injection.provideRepository(context)
                instance ?:ViewModelFactory(historyRepository)
            }.also { instance =  it }
    }
}