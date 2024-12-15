package com.dicoding.asclepius.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.viewmodel.MainViewModel
import com.dicoding.asclepius.viewmodel.ViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val historyViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressIndicator.visibility = View.VISIBLE
        } else {
            binding.progressIndicator.visibility = View.GONE
        }
    }
    private fun setEventData(data: List<HistoryEntity>) {
        val adapter = HistoryAdapter()
        adapter.submitList(data)

        binding.rvHistory.adapter = adapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "History"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.accent)))

        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager

        historyViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        historyViewModel.getAllImageDetectionHistory()
        historyViewModel.listHistory.observe(this) {
            Log.d("HistoryActivity", "Histories: $it")
            setEventData(it)
        }
    }
}