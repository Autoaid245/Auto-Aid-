package com.project.auto_aid.Components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.auto_aid.navigation.Routes
import com.project.auto_aid.screens.HomeScreen
import com.project.auto_aid.screens.garage.GarageScreen
import com.project.auto_aid.screens.garage.GarageRequestScreen
import com.project.auto_aid.screens.garage.RequestProcessingScreen
import com.project.auto_aid.screens.garage.MechanicAssignedScreen
import com.project.auto_aid.screens.garage.HelpCompletedScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen.route
    ) {

        // 🏠 HOME
        composable(Routes.HomeScreen.route) {
            HomeScreen(navController)
        }

        // 🚗 GARAGE MAIN
        composable(Routes.GarageScreen.route) {
            GarageScreen(navController)
        }

        // 📝 REQUEST GARAGE HELP
        composable(Routes.GarageRequestScreen.route) {
            GarageRequestScreen(navController)
        }

        // ⏳ SEARCHING MECHANIC
        composable(Routes.RequestProcessingScreen.route) {
            RequestProcessingScreen(navController)
        }

        // 👨‍🔧 MECHANIC ASSIGNED
        composable(Routes.MechanicAssignedScreen.route) {
            MechanicAssignedScreen(navController)
        }

        // ✅ HELP COMPLETED
        composable(Routes.HelpCompletedScreen.route) {
            HelpCompletedScreen(navController)
        }
    }
}
