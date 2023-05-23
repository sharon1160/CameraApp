package com.example.cameraapp.viewmodel

import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import com.example.cameraapp.model.Photo
import com.example.cameraapp.view.ui.screens.gallery.GalleryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class GalleryViewModel: ViewModel() {

    private val photosList = mutableListOf<Photo>()

    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState = _uiState.asStateFlow()

    private fun loadPhotosPath(): List<String> {
        val folder = Environment.getExternalStorageDirectory()
        var pathsList: List<String>? =
            File(folder, "Pictures/CameraX-Image/").listFiles()?.map { it.path }?.toTypedArray()
                ?.reversedArray()?.toList()

        pathsList = pathsList ?: emptyList()
        return pathsList
    }

    fun loadPhotos() {
        val pathsList: List<String> = loadPhotosPath()
        pathsList.forEach {
            val photo = Photo(image = Uri.parse(it), location = "No location")
            photosList.add(photo)
        }
        _uiState.update {
            it.copy(photosList = photosList)
        }
    }
}