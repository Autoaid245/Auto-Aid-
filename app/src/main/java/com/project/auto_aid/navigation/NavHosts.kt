package com.project.auto_aid.navigation

import AmbulanceActiveScreen
import TowingScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.auto_aid.model.OnBoardScreen
import com.project.auto_aid.model.SplashScreen
import com.project.auto_aid.screens.*
import com.project.auto_aid.screens.ambulance.AmbulanceHistoryScreen
import com.project.auto_aid.screens.ambulance.AmbulanceRequestScreen
import com.project.auto_aid.screens.ambulance.AmbulanceScreen
import com.project.auto_aid.screens.fuel.FuelActiveScreen
import com.project.auto_aid.screens.fuel.FuelHistoryScreen
import com.project.auto_aid.screens.fuel.FuelRequestScreen
import com.project.auto_aid.screens.fuel.FuelScreen
import com.project.auto_aid.screens.garage.*
import com.project.auto_aid.screens.towing.*
import com.project.auto_aid.selectDocument.SelectDocumentScreen
import com.project.auto_aid.settings.IDVerificationScreen
import com.project.auto_aid.settings.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {

        // ================= SPLASH & ONBOARD =================
        composable(Routes.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(Routes.OnBoardScreen.route) {
            OnBoardScreen(navController)
        }

        // ================= AUTH =================
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

        // ================= MAIN =================
        composable(Routes.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(Routes.SettingsScreen.route) {
            SettingsScreen(navController)
        }

        composable(Routes.UserInfoScreen.route) {
            UserInfoScreen(navController)
        }

        composable(Routes.IDVerificationScreen.route) {
            IDVerificationScreen(navController)
        }

        composable("select_document_screen") {
            SelectDocumentScreen(navController)
        }

        // ================= GARAGE FLOW =================
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

        // ================= 🚨 TOWING FLOW =================
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
        // ================= ⛽ FUEL FLOW =================
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

    }

}
