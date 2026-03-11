package com.project.auto_aid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

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
import com.project.auto_aid.screens.NotificationScreen
import com.project.auto_aid.screens.MaintenanceScreen

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
import com.project.auto_aid.provider.ui.ProviderRequestDetailsScreen
import com.project.auto_aid.provider.ui.ProviderNotificationsScreen
import com.project.auto_aid.provider.ui.ProviderMapHomeScreen
import com.project.auto_aid.provider.ui.ProviderChatListScreen
import com.project.auto_aid.provider.ui.ProviderChatThreadScreen
import com.project.auto_aid.provider.ui.ProviderProfileScreen

/* ---------- Location Picker Screen ---------- */
import com.project.auto_aid.screens.location.LocationPickerScreen

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

        composable(
            route = Routes.VerifyCodeScreen.route,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { entry ->
            val email = entry.arguments?.getString("email") ?: ""
            VerifyCodeScreen(navController = navController, email = email)
        }

        composable(Routes.ResetPasswordScreen.route) { ResetPasswordScreen(navController) }

        /* ---------- Maintenance ---------- */
        composable(
            route = Routes.MaintenanceScreen.route,
            arguments = listOf(
                navArgument("message") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "AutoAid is currently under maintenance. Please try again later."
                }
            )
        ) { entry ->
            val message = entry.arguments?.getString("message")
                ?: "AutoAid is currently under maintenance. Please try again later."

            MaintenanceScreen(
                navController = navController,
                message = message
            )
        }

        /* ---------- Main ---------- */
        composable(Routes.HomeScreen.route) { HomeScreen(navController) }
        composable(Routes.NotificationScreen.route) { NotificationScreen(navController) }

        /* ---------- Location Picker ---------- */
        composable(
            route = Routes.LocationPicker.route,
            arguments = listOf(
                navArgument("lat") {
                    type = NavType.StringType
                    defaultValue = "0.0"
                },
                navArgument("lng") {
                    type = NavType.StringType
                    defaultValue = "0.0"
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern =
                        "android-app://androidx.navigation/location_picker?lat={lat}&lng={lng}"
                }
            )
        ) { entry ->
            val lat = entry.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
            val lng = entry.arguments?.getString("lng")?.toDoubleOrNull() ?: 0.0

            LocationPickerScreen(
                navController = navController,
                initialLat = lat,
                initialLng = lng
            )
        }

        /* ---------- Settings ---------- */
        composable(Routes.SettingsScreen.route) { SettingsScreen(navController) }
        composable(Routes.UserInfoScreen.route) { IdentityVerificationScreen(navController) }

        /* ---------- Garage ---------- */
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

        /* ---------- Towing ---------- */
        composable(Routes.TowingScreen.route) {
            TowingScreen(navController)
        }

        composable(Routes.TowingProvidersScreen.route) {
            AvailableProvidersScreen(navController)
        }

        composable(
            route = Routes.TowingRequestScreen.route,
            arguments = listOf(
                navArgument("providerId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { entry ->
            val providerId = entry.arguments?.getString("providerId")
            TowingRequestScreen(
                navController = navController,
                providerId = providerId
            )
        }

        composable(
            route = Routes.TowingActiveScreen.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { entry ->
            val requestId = entry.arguments?.getString("requestId") ?: ""
            TowingActiveScreen(
                navController = navController,
                requestId = requestId
            )
        }

        composable(Routes.TowingHistoryScreen.route) {
            TowingHistoryScreen(navController)
        }

        /* ---------- Fuel ---------- */
        composable(Routes.FuelScreen.route) {
            FuelScreen(navController)
        }

        composable(Routes.FuelProvidersScreen.route) {
            AvailableFuelProvidersScreen(navController)
        }

        composable(
            route = Routes.FuelRequestScreen.route,
            arguments = listOf(
                navArgument("providerId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { entry ->
            val providerId = entry.arguments?.getString("providerId")
            FuelRequestScreen(
                navController = navController,
                providerId = providerId
            )
        }

        composable(
            route = Routes.FuelActiveScreen.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { entry ->
            val requestId = entry.arguments?.getString("requestId") ?: ""
            FuelActiveScreen(
                navController = navController,
                requestId = requestId
            )
        }

        composable(Routes.FuelHistoryScreen.route) {
            FuelHistoryScreen(navController)
        }

        /* ---------- Ambulance ---------- */
        composable(Routes.AmbulanceScreen.route) {
            AmbulanceScreen(navController)
        }

        composable(
            route = Routes.AmbulanceRequestScreen.route,
            arguments = listOf(
                navArgument("providerId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { entry ->
            val providerId = entry.arguments?.getString("providerId")
            AmbulanceRequestScreen(
                navController = navController,
                providerId = providerId
            )
        }

        composable(
            route = Routes.AmbulanceActiveScreen.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { entry ->
            val requestId = entry.arguments?.getString("requestId") ?: ""
            AmbulanceActiveScreen(
                navController = navController,
                requestId = requestId
            )
        }

        composable(Routes.AmbulanceHistoryScreen.route) {
            AmbulanceHistoryScreen(navController)
        }

        /* ---------- Legal ---------- */
        composable(Routes.AboutUsScreen.route) { AboutUsScreen(navController) }
        composable(Routes.PrivacyPolicyScreen.route) { PrivacyPolicyScreen(navController) }
        composable(Routes.PromotionScreen.route) { PromotionScreen(navController) }
        composable(Routes.PayoutInformationScreen.route) { PayoutInformationScreen(navController) }

        composable(
            route = Routes.TermsAndConditionsScreen.route,
            arguments = listOf(
                navArgument("fromSignup") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { entry ->
            val fromSignup = entry.arguments?.getBoolean("fromSignup") ?: false
            TermsAndConditionsScreen(navController, fromSignup)
        }

        /* ---------- Provider ---------- */
        composable(Routes.ProviderDashboard.route) {
            ProviderDashboardScreen(navController)
        }

        composable(Routes.EditProviderProfile.route) {
            EditProviderProfileScreen(navController)
        }

        composable(Routes.ProviderNotifications.route) {
            ProviderNotificationsScreen(navController)
        }

        composable(Routes.ProviderMapHome.route) {
            ProviderMapHomeScreen(navController)
        }

        composable(Routes.ProviderChatList.route) {
            ProviderChatListScreen(navController)
        }

        composable(Routes.ProviderProfile.route) {
            ProviderProfileScreen(navController)
        }

        composable(
            route = Routes.ProviderChatThread.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { entry ->
            val requestId = entry.arguments?.getString("requestId") ?: ""
            ProviderChatThreadScreen(
                navController = navController,
                requestId = requestId
            )
        }

        composable(
            route = Routes.ProviderActiveJob.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { entry ->
            val requestId = entry.arguments?.getString("requestId") ?: ""
            ProviderActiveJobScreen(requestId, navController)
        }

        composable(
            route = Routes.ProviderMapScreen.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType },
                navArgument("pickupLat") {
                    type = NavType.StringType
                    defaultValue = "0.0"
                },
                navArgument("pickupLng") {
                    type = NavType.StringType
                    defaultValue = "0.0"
                }
            )
        ) { entry ->
            val requestId = entry.arguments?.getString("requestId") ?: ""
            val pickupLat = entry.arguments?.getString("pickupLat")?.toDoubleOrNull() ?: 0.0
            val pickupLng = entry.arguments?.getString("pickupLng")?.toDoubleOrNull() ?: 0.0

            ProviderMapScreen(
                requestId = requestId,
                pickupLat = pickupLat,
                pickupLng = pickupLng
            )
        }

        composable(
            route = Routes.ProviderRequestDetails.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { entry ->
            val requestId = entry.arguments?.getString("requestId") ?: ""
            ProviderRequestDetailsScreen(
                navController = navController,
                requestId = requestId
            )
        }
    }
}