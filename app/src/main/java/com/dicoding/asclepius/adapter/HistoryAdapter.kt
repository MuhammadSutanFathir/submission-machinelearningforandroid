package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import java.text.NumberFormat

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(diffCallback) {
    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (history: HistoryEntity) {
            binding.resultLabel.text = history.label
            val formattedPercentage =
                NumberFormat.getPercentInstance().format(history.confidenceScore.toDouble())
            val resultText =
                "Confidence Score: $formattedPercentage"
            binding.resultPrediction.text = resultText
            Glide.with(itemView.context)
                .load(history.imageUri)
                .into(binding.resultImage)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}