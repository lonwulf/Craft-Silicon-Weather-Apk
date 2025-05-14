package com.lonwulf.craft_silicon.weatherapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lonwulf.craft_silicon.weatherapp.ui.viewmodel.SharedViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel

interface NavComposable {
    @Composable
    fun Composable(
        navHostController: NavHostController,
        snackbarHostState: SnackbarHostState)
}

@Composable
fun NavGraph(
    navHostController: NavHostController,
    composables: Map<String, NavComposable>,
    snackbarHostState: SnackbarHostState) {
    NavHost(
        navController = navHostController, startDestination = TopLevelDestinations.HomeScreen.route
    ) {

        composables.forEach { (route, composable) ->
            composable(route = route) { entry ->
                composable.Composable(
                    navHostController, snackbarHostState
                )
            }
        }
    }
}
