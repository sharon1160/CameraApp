package com.example.cameraapp.view.ui.screens.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.cameraapp.view.ui.navigation.NavigationBar
import com.example.cameraapp.view.ui.navigation.NavigationGraph
import com.example.cameraapp.view.ui.theme.CameraAppTheme

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { NavigationBar(navController = navController) }
    ) {
        NavigationGraph(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CameraAppTheme {
        HomeScreen()
    }
}
