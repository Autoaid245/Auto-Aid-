package com.project.auto_aid.Components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.auto_aid.navigation.Routes
import com.project.auto_aid.screens.HomeScreen
import com.project.auto_aid.screens.garage.AvailableGarageProvidersScreen
import com.project.auto_aid.screens.garage.GarageActiveScreen
import com.project.auto_aid.screens.garage.GarageHistoryScreen
import com.project.auto_aid.screens.garage.GarageRequestScreen
import com.project.auto_aid.screens.garage.GarageScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen.route
    ) {

        composable(Routes.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(Routes.GarageScreen.route) {
            GarageScreen(navController)
        }

        composable(Routes.GarageProvidersScreen.route) {
            AvailableGarageProvidersScreen(navController)
        }

        composable(
            route = Routes.GarageRequestScreen.route,
            arguments = listOf(
                navArgument("providerId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { entry ->
            val providerId = entry.arguments?.getString("providerId")
            GarageRequestScreen(
                navController = navController,
                providerId = providerId
            )
        }

        composable(
            route = Routes.GarageActiveScreen.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { entry ->
            val requestId = entry.arguments?.getString("requestId") ?: ""
            GarageActiveScreen(
                navController = navController,
                requestId = requestId
            )
        }

        composable(Routes.GarageHistoryScreen.route) {
            GarageHistoryScreen(navController)
        }
    }
}