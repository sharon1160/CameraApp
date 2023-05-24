package com.example.cameraapp.view.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cameraapp.view.ui.screens.camera.CameraScreen
import com.example.cameraapp.view.ui.screens.detail.DetailScreen
import com.example.cameraapp.view.ui.screens.gallery.GalleryScreen
import com.example.cameraapp.viewmodel.CameraViewModel
import com.example.cameraapp.viewmodel.DetailViewModel
import com.example.cameraapp.viewmodel.GalleryViewModel

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Gallery.screen_route) {
        composable(NavigationItem.Gallery.screen_route) {
            val galleryViewModel = GalleryViewModel()
            GalleryScreen(galleryViewModel, navController)
        }
        composable(NavigationItem.Camera.screen_route) {
            val viewModelStoreOwner = navController.getViewModelStoreOwner(navController.graph.id)
            val cameraViewModel =
                ViewModelProvider(viewModelStoreOwner)[CameraViewModel::class.java]
            CameraScreen(cameraViewModel)
        }
        composable(
            route = "detail/{imageUri}",
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) {
            val detailViewModel = DetailViewModel()
            val imageUri =Uri.parse(Uri.decode(it.arguments?.getString("imageUri")))
            DetailScreen(detailViewModel, navController, imageUri)
        }
    }
}
