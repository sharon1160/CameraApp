package com.example.cameraapp.view.ui.screens.detail

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cameraapp.R
import com.example.cameraapp.viewmodel.DetailViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun DetailScreen(detailViewModel: DetailViewModel, navController: NavHostController, Image: Uri) {
    Scaffold() {
        PhotoMap(detailViewModel.loadLocation(Image), )
        BackButton(navController)
    }
}

@Composable
fun BackButton(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(onClick = { navController.navigate(route = "home") }) {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = null,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
            )
        }
    }
}

@Composable
fun PhotoMap(LatLong: List<Float>?) {
    if (LatLong != null) {
        val marker = LatLng(LatLong[0].toDouble(), LatLong[1].toDouble())
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(marker, 10f)
        }
        GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
            Marker(position = marker)
        }
    } else {
        GoogleMap(modifier = Modifier.fillMaxSize())
    }
}
