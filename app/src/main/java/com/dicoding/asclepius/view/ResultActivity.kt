package com.dicoding.asclepius.view

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.viewmodel.MainViewModel
import com.dicoding.asclepius.viewmodel.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val resultViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Result"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.accent)))

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
            classifyImage(it)
        }

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
    }

    private fun classifyImage(imageUri: Uri) {
        val imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                }

                override fun onResults(results: List<Classifications>?) {
                    results?.let {
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            val label = it[0].categories[0].label
                            val confidenceScore = it[0].categories[0].score


                            val formattedPercentage =
                                NumberFormat.getPercentInstance().format(confidenceScore.toDouble())

                            val resultText =
                                "Prediction: $label \nConfidence Score: $formattedPercentage"
                            binding.resultText.text = resultText
                            val historyEntity = HistoryEntity(
                                id = System.currentTimeMillis().toInt(),
                                imageUri = imageUri.toString(),
                                label = label,
                                confidenceScore = confidenceScore
                            )
                            resultViewModel.saveImageDetectionHistory(historyEntity)
                        } else {
                            Toast.makeText(
                                this@ResultActivity,
                                "No results",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(imageUri)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}
