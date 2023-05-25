package com.example.cameraapp.view.ui.navigation

import com.example.cameraapp.R

sealed class NavigationItem(var title:String, var icon:Int, var screen_route:String) {
    object Gallery : NavigationItem("Gallery", R.drawable.gallery_icon,"home")
    object Camera: NavigationItem("Camera",R.drawable.camera_icon,"camera")
}
