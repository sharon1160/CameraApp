package com.example.cameraapp.view.ui.screens.gallery

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.cameraapp.R
import com.example.cameraapp.model.Photo
import com.example.cameraapp.view.ui.theme.CameraAppTheme
import com.example.cameraapp.viewmodel.GalleryViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

private const val PACKAGE_NAME = "com.example.cameraapp"
const val URI_RSC = "android.resource://$PACKAGE_NAME/"
val data: List<Photo> = listOf(
    Photo(Uri.parse("$URI_RSC${R.drawable.cat_img}"), "no location"),
    Photo(Uri.parse("$URI_RSC${R.drawable.cat_img}"), "no location"),
    Photo(Uri.parse("$URI_RSC${R.drawable.cat_img}"), "no location"),
    Photo(Uri.parse("$URI_RSC${R.drawable.cat_img}"), "no location"),
)

@Composable
fun GalleryScreen(galleryViewModel: GalleryViewModel) {
    val state by galleryViewModel.uiState.collectAsState()
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.teal_700))
        ) {
            PhotosGrid(state.photosList)
        }
        Permissions(galleryViewModel::loadPhotos)
    }


}

@Composable
fun ImageCard(photo: Photo) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .height(180.dp)
            .width(180.dp)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberAsyncImagePainter(photo.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun PhotosGrid(photosList: List<Photo>) {
    if (photosList.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            photosList.forEachIndexed { index, photo ->
                if (index % 3 == 0) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ImageCard(photo)
                    }
                } else {
                    item(span = { GridItemSpan(1) }) {
                        ImageCard(photo)
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.teal_700))
                .wrapContentSize(Alignment.Center)
        ) {
            androidx.compose.material.Text(
                text = "No photos",
                fontWeight = FontWeight.Bold,
                color = Color.White,
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CameraAppTheme {
        PhotosGrid(data)
    }
}
