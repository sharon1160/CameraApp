package com.example.cameraapp.viewmodel

import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {
    fun loadLocation(photoUri: Uri): List<Float>? {
        val exifInterface = photoUri.path?.let { ExifInterface(it) }
        val latLong = FloatArray(2)
        return if (exifInterface?.getLatLong(latLong) == true) {
            val latitude = latLong[0]
            val longitude = latLong[1]
            listOf(latitude, longitude)
        } else {
            Log.e("Error", "No found lat and long of the image")
            null
        }
    }
}
