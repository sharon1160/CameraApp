package com.example.cameraapp.view.ui.navigation

import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cameraapp.R

@Composable
fun NavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Gallery,
        NavigationItem.Camera
    )
    BottomNavigation (
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title,
                    fontSize = 9.sp) },
                selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
