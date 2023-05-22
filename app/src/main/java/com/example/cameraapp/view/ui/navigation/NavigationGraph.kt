package com.example.cameraapp.view.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cameraapp.view.ui.screens.camera.CameraScreen
import com.example.cameraapp.view.ui.screens.gallery.GalleryScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Gallery.screen_route) {
        composable(NavigationItem.Gallery.screen_route) {
            GalleryScreen()
        }
        composable(NavigationItem.Camera.screen_route) {
            CameraScreen()
        }
    }
}
