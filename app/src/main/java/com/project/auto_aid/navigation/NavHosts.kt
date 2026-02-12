package com.project.auto_aid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

// Splash & Onboard
import com.project.auto_aid.model.SplashScreen
import com.project.auto_aid.model.OnBoardScreen
<<<<<<< HEAD
=======
import com.project.auto_aid.settings.ConsentScreen
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0

// Auth
import com.project.auto_aid.screens.LoginScreen
import com.project.auto_aid.screens.SignupScreen
import com.project.auto_aid.screens.ForgotPasswordScreen
import com.project.auto_aid.screens.VerifyCodeScreen
import com.project.auto_aid.screens.ResetPasswordScreen
<<<<<<< HEAD
import com.project.auto_aid.settings.TermsAndConditionsScreen
=======
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0

// Main
import com.project.auto_aid.screens.HomeScreen

<<<<<<< HEAD
// Settings / Legal ✅ ONLY THESE
=======
// Settings / Legal
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
import com.project.auto_aid.settings.SettingsScreen
import com.project.auto_aid.settings.IdentityVerificationScreen
import com.project.auto_aid.settings.AboutUsScreen
import com.project.auto_aid.settings.PrivacyPolicyScreen
import com.project.auto_aid.settings.PromotionScreen
import com.project.auto_aid.settings.PayoutInformationScreen
<<<<<<< HEAD

// Garage
import com.project.auto_aid.screens.garage.*

// Towing
import com.project.auto_aid.screens.towing.*

// Fuel
import com.project.auto_aid.screens.fuel.*

// Ambulance
import com.project.auto_aid.screens.ambulance.*

=======
import com.project.auto_aid.settings.TermsAndConditionsScreen

// Garage (USER)
import com.project.auto_aid.screens.garage.*

// Towing (USER)
import com.project.auto_aid.screens.towing.*

// Fuel (USER)
import com.project.auto_aid.screens.fuel.*

// Ambulance (USER)
import com.project.auto_aid.screens.ambulance.*

// Provider
import com.project.auto_aid.provider.ui.ProviderDashboardScreen
import com.project.auto_aid.provider.ui.ProviderActiveJobScreen
import com.project.auto_aid.provider.ui.ProviderMapScreen
import com.project.auto_aid.provider.ui.EditProviderProfileScreen

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {

<<<<<<< HEAD
        // Splash & Onboard
        composable(Routes.SplashScreen.route) { SplashScreen(navController) }
        composable(Routes.OnBoardScreen.route) { OnBoardScreen(navController) }

        // Auth
        composable(Routes.LoginScreen.route) { LoginScreen(navController) }
        composable(Routes.SignupScreen.route) { SignupScreen(navController) }
        composable(Routes.ForgotPasswordScreen.route) { ForgotPasswordScreen(navController) }
        composable(Routes.VerifyCodeScreen.route) { VerifyCodeScreen(navController) }
        composable(Routes.ResetPasswordScreen.route) { ResetPasswordScreen(navController) }

        // Main
        composable(Routes.HomeScreen.route) { HomeScreen(navController) }

        // Settings
        composable(Routes.SettingsScreen.route) { SettingsScreen(navController) }
=======
        /* ---------------- Splash & Onboard ---------------- */
        composable(Routes.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Routes.ConsentScreen.route) {
            ConsentScreen(navController)
        }
        composable(Routes.OnBoardScreen.route) {
            OnBoardScreen(navController)
        }

        /* ---------------- Auth ---------------- */
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

        /* ---------------- Main ---------------- */
        composable(Routes.HomeScreen.route) {
            HomeScreen(navController)
        }

        /* ---------------- Settings ---------------- */
        composable(Routes.SettingsScreen.route) {
            SettingsScreen(navController)
        }

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        composable(Routes.UserInfoScreen.route) {
            IdentityVerificationScreen(navController)
        }

<<<<<<< HEAD
        // Garage
        composable(Routes.GarageScreen.route) { GarageScreen(navController) }
        composable(Routes.GarageRequestScreen.route) { GarageRequestScreen(navController) }
        composable(Routes.RequestProcessingScreen.route) { RequestProcessingScreen(navController) }
        composable(Routes.MechanicAssignedScreen.route) { MechanicAssignedScreen(navController) }
        composable(Routes.HelpCompletedScreen.route) { HelpCompletedScreen(navController) }

        // Towing
        composable(Routes.TowingScreen.route) { TowingScreen(navController) }
        composable(Routes.TowingRequestScreen.route) { TowingRequestScreen(navController) }
        composable(Routes.TowingActiveScreen.route) { TowingActiveScreen(navController) }
        composable(Routes.TowingHistoryScreen.route) { TowingHistoryScreen(navController) }

        // Fuel
        composable(Routes.FuelScreen.route) { FuelScreen(navController) }
        composable(Routes.FuelRequestScreen.route) { FuelRequestScreen(navController) }
        composable(Routes.FuelActiveScreen.route) { FuelActiveScreen(navController) }
        composable(Routes.FuelHistoryScreen.route) { FuelHistoryScreen(navController) }

        // Ambulance
        composable(Routes.AmbulanceScreen.route) { AmbulanceScreen(navController) }
        composable(Routes.AmbulanceRequestScreen.route) { AmbulanceRequestScreen(navController) }
        composable(Routes.AmbulanceStatusScreen.route) { AmbulanceActiveScreen(navController) }
        composable(Routes.AmbulanceHistoryScreen.route) { AmbulanceHistoryScreen(navController) }

        // Legal & Info
        composable(Routes.AboutUsScreen.route) { AboutUsScreen(navController) }
=======
        /* ---------------- Garage (User) ---------------- */
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

        /* ---------------- Towing (User) ---------------- */
        composable(Routes.TowingScreen.route) {
            TowingScreen(navController)
        }

        composable(Routes.TowingRequestScreen.route) {
            TowingRequestScreen(navController)
        }

        composable(Routes.TowingActiveScreen.route) {
            TowingActiveScreen(navController)
        }

        composable(Routes.TowingHistoryScreen.route) {
            TowingHistoryScreen(navController)
        }

        /* ---------------- Fuel (User) ---------------- */
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

        /* ---------------- Ambulance (User) ---------------- */
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

        /* ---------------- Legal & Info ---------------- */
        composable(Routes.AboutUsScreen.route) {
            AboutUsScreen(navController)
        }
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0

        composable(
            route = Routes.TermsAndConditionsScreen.routeWithArgs,
            arguments = listOf(
                navArgument("fromSignup") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { entry ->
<<<<<<< HEAD
            val fromSignup = entry.arguments?.getBoolean("fromSignup") ?: false
=======
            val fromSignup =
                entry.arguments?.getBoolean("fromSignup") ?: false

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
            TermsAndConditionsScreen(navController, fromSignup)
        }

        composable(Routes.PrivacyPolicyScreen.route) {
            PrivacyPolicyScreen(navController)
        }

        composable(Routes.PromotionScreen.route) {
            PromotionScreen(navController)
        }

        composable(Routes.PayoutInformationScreen.route) {
            PayoutInformationScreen(navController)
        }
<<<<<<< HEAD
=======

        /* ---------------- PROVIDER FLOW ---------------- */
        composable(Routes.ProviderDashboard.route) {
            ProviderDashboardScreen(navController)
        }

        composable(Routes.EditProviderProfile.route) {
            EditProviderProfileScreen(navController)
        }

        composable(
            route = Routes.ProviderActiveJob.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val requestId =
                backStackEntry.arguments?.getString("requestId")!!

            ProviderActiveJobScreen(
                requestId = requestId,
                navController = navController
            )
        }

        composable(
            route = Routes.ProviderMapScreen.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val requestId =
                backStackEntry.arguments?.getString("requestId")!!

            ProviderMapScreen(
                requestId = requestId
            )
        }
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    }
}