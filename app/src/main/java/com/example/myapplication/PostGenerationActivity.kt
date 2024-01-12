package com.example.myapplication

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Bundle

class PostGenerationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        val downloadButton = findViewById<Button>(R.id.downloadButton)
        val shareButton = findViewById<Button>(R.id.shareButton)

        downloadButton.setOnClickListener {
            val imageURL = ""
            downloadImage(imageURL)
        }

        shareButton.setOnClickListener {
            val imageUrl = ""
            shareImage(imageUrl)
        }
    }

    private fun downloadImage(imageUrl: String) {
        val request = DownloadManager.Request(Uri.parse(imageUrl))
        request.setTitle("Image Download")
        // Set the description of the download notification
        request.setDescription("Downloading image...")
        // Set the destination directory for the downloaded file
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "downloaded_image.jpg"
        )

        // Get the download service and enqueue the request
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun shareImage(imageURL: String) {
            val imageUri = Uri.parse(imageURL)

            // Create an intent to share the image
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            // Start the activity to share the image
            startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

}