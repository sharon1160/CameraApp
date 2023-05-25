package com.example.cameraapp.view.ui.screens.gallery

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cameraapp.model.Photo
import com.example.cameraapp.viewmodel.GalleryViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun GalleryScreen(galleryViewModel: GalleryViewModel, navController: NavHostController) {
    val state by galleryViewModel.uiState.collectAsState()
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            PhotosGrid(state.photosList, navController)
        }
        Permissions(galleryViewModel::loadPhotos)
    }


}

@Composable
fun ImageCard(photo: Photo, navController: NavHostController) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .height(180.dp)
            .width(180.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navController.navigate(
                        route = "detail/${Uri.encode(photo.image.toString())}"
                    )
                },
            painter = rememberAsyncImagePainter(photo.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun PhotosGrid(photosList: List<Photo>, navController: NavHostController) {
    if (photosList.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start= 8.dp, end=8.dp, top= 8.dp, bottom = 100.dp)
        ) {
            photosList.forEachIndexed { index, photo ->
                if (index % 3 == 0) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ImageCard(photo, navController)
                    }
                } else {
                    item(span = { GridItemSpan(1) }) {
                        ImageCard(photo, navController)
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .wrapContentSize(Alignment.Center)
        ) {
            androidx.compose.material.Text(
                text = "No photos",
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permissions(loadPhotos: () -> Unit) {
    var show by rememberSaveable { mutableStateOf(false) }
    var rationalShow by rememberSaveable { mutableStateOf(false) }

    val permissions: List<String> = listOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val permissionsState = rememberMultiplePermissionsState(permissions = permissions)
    LaunchedEffect(key1 = Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (permissionsState.allPermissionsGranted) {
        loadPhotos()
    } else if (permissionsState.shouldShowRationale && !rationalShow) {
        show = true
        rationalShow = true
    } else {
        Log.d("DEBUG", "Access denied")
    }

    RationaleDialog(
        show = show,
        onConfirm = { Log.d("onConfirm", "Confirmed") },
        onDismiss = { show = false })
}

@Composable
fun RationaleDialog(show: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = "CONFIRM")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "DISMISS")
                }
            },
            title = { Text(text = "Allow Permissions") },
            text = {
                Text(
                    text = "We need permission to read your storage and camera. Without " +
                            "these permissions we are unable to take and see photos."
                )
            }
        )
    }
}
