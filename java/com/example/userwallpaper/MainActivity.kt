package com.example.userwallpaper

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.userwallpaper.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            setImageAsWallpaper(it)
        }
    }

    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Toast.makeText(this, "لا يمكن تعيين فيديو كخلفية مباشرة بدون Live Wallpaper.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.btnPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnPickVideo.setOnClickListener {
            pickVideo.launch("video/*")
        }
    }

    private fun setImageAsWallpaper(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val wallpaperManager = WallpaperManager.getInstance(this)

        try {
            wallpaperManager.setBitmap(bitmap)
            Toast.makeText(this, "تم تعيين الصورة كخلفية!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "فشل في تعيين الخلفية", Toast.LENGTH_SHORT).show()
        }
    }
}
