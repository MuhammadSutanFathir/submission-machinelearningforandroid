// MainActivity.kt
package com.dicoding.asclepius.view

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.viewmodel.MainViewModel
import com.dicoding.asclepius.viewmodel.ViewModelFactory
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    } // Initialize ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.accent)))

        showImage()
        binding.newsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, NewsActivity::class.java))
        }
        binding.historyButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
        }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            viewModel.croppedImageUri.value?.let {
                analyzeImage(it)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    private fun startGallery() {
        binding.previewImageView.setImageResource(R.drawable.ic_place_holder)
        viewModel.setCroppedImageUri(null)
        Log.d("Image Reset", "croppedImageUri has been reset to null")
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCrop(uri: Uri) {
        val timestamp = System.currentTimeMillis()
        val destinationUri = Uri.fromFile(File(cacheDir, "UCropImage_$timestamp"))

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .start(this@MainActivity)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            if (resultUri != null) {
                viewModel.setCroppedImageUri(resultUri) // Store the cropped URI in ViewModel
            } else {
                showToast("Failed to crop image")
            }
        }
    }

    private fun analyzeImage(uri: Uri) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        startActivity(intent)
    }

    private fun showImage() {
        viewModel.croppedImageUri.observe(this) { uri ->
            uri?.let {
                binding.previewImageView.setImageURI(it)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
