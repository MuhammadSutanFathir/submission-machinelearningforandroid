package com.dicoding.asclepius.view


import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.viewmodel.MainViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.viewmodel.ViewModelFactory

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val newsViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressIndicator.visibility = View.VISIBLE
        } else {
            binding.progressIndicator.visibility = View.GONE
        }
    }
    private fun setEventData(data: List<ArticlesItem>) {
        val adapter = NewsAdapter()
        adapter.submitList(data)

        binding.rvNews.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "News"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.accent)))

        val layoutManager = LinearLayoutManager(this)
        binding.rvNews.layoutManager = layoutManager

        newsViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        newsViewModel.listNews.observe(this) {
            setEventData(it)
        }
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
