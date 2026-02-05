package com.project.auto_aid.navigation

import AmbulanceActiveScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

// ================= SPLASH & ONBOARD =================
import com.project.auto_aid.model.SplashScreen
import com.project.auto_aid.model.OnBoardScreen

// ================= AUTH =================
import com.project.auto_aid.screens.*

// ================= SETTINGS =================
import com.project.auto_aid.settings.SettingsScreen

// ================= GARAGE =================
import com.project.auto_aid.screens.garage.*

// ================= TOWING =================
import com.project.auto_aid.screens.towing.*

// ================= FUEL =================
import com.project.auto_aid.screens.fuel.*

// ================= AMBULANCE =================
import com.project.auto_aid.screens.ambulance.*
import com.project.auto_aid.settings.AboutUsScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {

        composable(Routes.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(Routes.OnBoardScreen.route) {
            OnBoardScreen(navController)
        }

        composable(Routes.LoginScreen.route) {
            LoginScreen(navController)
        }

        composable(Routes.SignupScreen.route) {
            SignupScreen(navController)
        }

        composable(Routes.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController)
        }

        composable(Routes.VerifyCodeScreen.route) {
            VerifyCodeScreen(navController)
        }

        composable(Routes.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController)
        }

        composable(Routes.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(Routes.SettingsScreen.route) {
            SettingsScreen(navController)
        }

        composable(Routes.UserInfoScreen.route) {
            UserInfoScreen(navController)
        }

        composable(Routes.GarageScreen.route) {
            GarageScreen(navController)
        }

        composable(Routes.GarageRequestScreen.route) {
            GarageRequestScreen(navController)
        }

        composable(Routes.RequestProcessingScreen.route) {
            RequestProcessingScreen(navController)
        }

        composable(Routes.MechanicAssignedScreen.route) {
            MechanicAssignedScreen(navController)
        }

        composable(Routes.HelpCompletedScreen.route) {
            HelpCompletedScreen(navController)
        }

        composable(Routes.TowingScreen.route) {
            TowingScreen(navController)
        }

        composable(Routes.TowingRequestScreen.route) {
            TowingRequestScreen(navController)
        }

        composable(Routes.TowingHistoryScreen.route) {
            TowingHistoryScreen(navController)
        }

        composable(Routes.TowingActiveScreen.route) {
            TowingActiveScreen(navController)
        }

        composable(Routes.FuelScreen.route) {
            FuelScreen(navController)
        }

        composable(Routes.FuelRequestScreen.route) {
            FuelRequestScreen(navController)
        }

        composable(Routes.FuelActiveScreen.route) {
            FuelActiveScreen(navController)
        }

        composable(Routes.FuelHistoryScreen.route) {
            FuelHistoryScreen(navController)
        }

        composable(Routes.AmbulanceScreen.route) {
            AmbulanceScreen(navController)
        }

        composable(Routes.AmbulanceRequestScreen.route) {
            AmbulanceRequestScreen(navController)
        }

        composable(Routes.AmbulanceStatusScreen.route) {
            AmbulanceActiveScreen(navController)
        }

        composable(Routes.AmbulanceHistoryScreen.route) {
            AmbulanceHistoryScreen(navController)
        }
        composable(Routes.PromotionScreen.route) {
            PromotionScreen(navController)
        }
        composable(Routes.PayoutInformationScreen.route) {
            PayoutInformationScreen(navController)
        }
        composable(Routes.AboutUsScreen.route) {
            AboutUsScreen(navController)
        }
    }
}

@Composable
fun TowingScreen(x0: NavHostController) {
    TODO("Not yet implemented")
}