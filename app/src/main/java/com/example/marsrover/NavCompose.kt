package com.example.marsrover

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.marsrover.nav.Action
import com.example.marsrover.nav.Destinations.Home
import com.example.marsrover.nav.Destinations.Manifest
import com.example.marsrover.nav.Destinations.Photo
import com.example.marsrover.nav.Destinations.Saved
import com.example.marsrover.nav.Screen
import com.example.marsrover.ui.theme.MarsRoverTheme
import com.example.marsrover.ui.view.ManifestScreen
import com.example.marsrover.ui.view.PhotoListSavedScreen
import com.example.marsrover.ui.view.PhotoScreen
import com.example.marsrover.ui.view.RoverList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavCompose() {

    val items = listOf(
        Screen.Home,
        Screen.Saved
    )

    val navController = rememberNavController()
    val actions = remember(navController) { Action(navController) }

    MarsRoverTheme {
        Scaffold(bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.drawableId),
                                contentDescription = stringResource(id = screen.resourceId)
                            )
                        },
                        label = {
                            Text(text = stringResource(id = screen.resourceId))
                        },
                        selected = currentDestination?.hierarchy?.any {
                            if(it.route?.contains("manifest") == true || it.route?.contains("photo") == true) {
                                screen.route == "home"
                            } else {
                                it.route == screen.route
                            }
                        } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

            }
        }
        ) { innerPadding ->

            val modifier = Modifier.padding(innerPadding)
            NavHost(navController = navController, startDestination = Home) {
                composable(Home) {
                    RoverList(
                        modifier = modifier
                    ) { roverName ->
                        actions.manifest(roverName)
                    }
                }
                composable(Manifest) { backStackEntry ->
                    ManifestScreen(
                        modifier = modifier,
                        roverName = backStackEntry.arguments?.getString("roverName"),
                        marsRoverManifestViewModel = hiltViewModel(),
                        onClick = { roverName, sol ->
                            actions.photo(roverName, sol)
                        }
                    )
                }
                composable(Photo) { backStackEntry ->
                    PhotoScreen(
                        modifier = modifier,
                        roverName = backStackEntry.arguments?.getString("roverName"),
                        sol = backStackEntry.arguments?.getString("sol"),
                        marsRoverPhotoViewModel = hiltViewModel()
                    )
                }
                composable(Saved) {
                    PhotoListSavedScreen(
                        modifier = modifier,
                        marsRoverSavedViewModel = hiltViewModel()
                    )
                }
            }
        }
    }
}