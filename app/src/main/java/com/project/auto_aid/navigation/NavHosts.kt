package com.project.auto_aid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

/* ---------- Splash & Onboard ---------- */
import com.project.auto_aid.model.SplashScreen
import com.project.auto_aid.model.OnBoardScreen
import com.project.auto_aid.settings.ConsentScreen

/* ---------- Auth ---------- */
import com.project.auto_aid.screens.LoginScreen
import com.project.auto_aid.screens.SignupScreen
import com.project.auto_aid.screens.ForgotPasswordScreen
import com.project.auto_aid.screens.VerifyCodeScreen
import com.project.auto_aid.screens.ResetPasswordScreen
import com.project.auto_aid.settings.TermsAndConditionsScreen

/* ---------- Main ---------- */
import com.project.auto_aid.screens.HomeScreen

/* ---------- Settings ---------- */
import com.project.auto_aid.settings.SettingsScreen
import com.project.auto_aid.settings.IdentityVerificationScreen
import com.project.auto_aid.settings.AboutUsScreen
import com.project.auto_aid.settings.PrivacyPolicyScreen
import com.project.auto_aid.settings.PromotionScreen
import com.project.auto_aid.settings.PayoutInformationScreen

/* ---------- User Features ---------- */
import com.project.auto_aid.screens.garage.*
import com.project.auto_aid.screens.towing.*
import com.project.auto_aid.screens.fuel.*
import com.project.auto_aid.screens.ambulance.*

/* ---------- Provider ---------- */
import com.project.auto_aid.provider.ui.ProviderDashboardScreen
import com.project.auto_aid.provider.ui.ProviderActiveJobScreen
import com.project.auto_aid.provider.ui.ProviderMapScreen
import com.project.auto_aid.provider.ui.EditProviderProfileScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {

        /* ---------- Splash & Onboard ---------- */
        composable(Routes.SplashScreen.route) { SplashScreen(navController) }
        composable(Routes.OnBoardScreen.route) { OnBoardScreen(navController) }
        composable(Routes.ConsentScreen.route) { ConsentScreen(navController) }

        /* ---------- Auth ---------- */
        composable(Routes.LoginScreen.route) { LoginScreen(navController) }
        composable(Routes.SignupScreen.route) { SignupScreen(navController) }
        composable(Routes.ForgotPasswordScreen.route) { ForgotPasswordScreen(navController) }
        composable(Routes.VerifyCodeScreen.route) { VerifyCodeScreen(navController) }
        composable(Routes.ResetPasswordScreen.route) { ResetPasswordScreen(navController) }

        /* ---------- Main ---------- */
        composable(Routes.HomeScreen.route) { HomeScreen(navController) }

        /* ---------- Settings ---------- */
        composable(Routes.SettingsScreen.route) { SettingsScreen(navController) }
        composable(Routes.UserInfoScreen.route) { IdentityVerificationScreen(navController) }

        /* ---------- Garage ---------- */
        composable(Routes.GarageScreen.route) { GarageScreen(navController) }
        composable(Routes.GarageRequestScreen.route) { GarageRequestScreen(navController) }
        composable(Routes.RequestProcessingScreen.route) { RequestProcessingScreen(navController) }
        composable(Routes.MechanicAssignedScreen.route) { MechanicAssignedScreen(navController) }
        composable(Routes.HelpCompletedScreen.route) { HelpCompletedScreen(navController) }

        /* ---------- Towing ---------- */
        composable(Routes.TowingScreen.route) { TowingScreen(navController) }
        composable(Routes.TowingRequestScreen.route) { TowingRequestScreen(navController) }
        composable(Routes.TowingActiveScreen.route) { TowingActiveScreen(navController) }
        composable(Routes.TowingHistoryScreen.route) { TowingHistoryScreen(navController) }

        /* ---------- Fuel ---------- */
        composable(Routes.FuelScreen.route) { FuelScreen(navController) }
        composable(Routes.FuelRequestScreen.route) { FuelRequestScreen(navController) }
        composable(Routes.FuelActiveScreen.route) { FuelActiveScreen(navController) }
        composable(Routes.FuelHistoryScreen.route) { FuelHistoryScreen(navController) }

        /* ---------- Ambulance ---------- */
        composable(Routes.AmbulanceScreen.route) { AmbulanceScreen(navController) }
        composable(Routes.AmbulanceRequestScreen.route) { AmbulanceRequestScreen(navController) }
        composable(Routes.AmbulanceStatusScreen.route) { AmbulanceActiveScreen(navController) }
        composable(Routes.AmbulanceHistoryScreen.route) { AmbulanceHistoryScreen(navController) }

        /* ---------- Legal ---------- */
        composable(Routes.AboutUsScreen.route) { AboutUsScreen(navController) }
        composable(Routes.PrivacyPolicyScreen.route) { PrivacyPolicyScreen(navController) }
        composable(Routes.PromotionScreen.route) { PromotionScreen(navController) }
        composable(Routes.PayoutInformationScreen.route) { PayoutInformationScreen(navController) }

        composable(
            route = Routes.TermsAndConditionsScreen.route,
            arguments = listOf(navArgument("fromSignup") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) { entry ->
            val fromSignup = entry.arguments?.getBoolean("fromSignup") ?: false
            TermsAndConditionsScreen(navController, fromSignup)
        }

        /* ---------- PROVIDER ---------- */
        composable(Routes.ProviderDashboard.route) {
            ProviderDashboardScreen(navController)
        }

        composable(Routes.EditProviderProfile.route) {
            EditProviderProfileScreen(navController)
        }

        composable(
            route = Routes.ProviderActiveJob.route,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId")!!
            ProviderActiveJobScreen(requestId, navController)
        }

        composable(
            route = Routes.ProviderMapScreen.route,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId")!!
            ProviderMapScreen(requestId)
        }
    }

}
