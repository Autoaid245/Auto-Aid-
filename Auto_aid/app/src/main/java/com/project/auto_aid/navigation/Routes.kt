package com.project.auto_aid.navigation

import android.net.Uri

sealed class Routes(val route: String) {

    /* =========================
       Splash & Onboard
       ========================= */
    object SplashScreen : Routes("splash")
    object OnBoardScreen : Routes("onboard")
    object ConsentScreen : Routes("consent_screen")

    /* =========================
       Authentication
       ========================= */
    object LoginScreen : Routes("login")
    object SignupScreen : Routes("signup")
    object ForgotPasswordScreen : Routes("forgot_password")

    object VerifyCodeScreen : Routes("verify_code/{email}") {
        fun createRoute(email: String): String =
            "verify_code/${Uri.encode(email)}"
    }

    object ResetPasswordScreen : Routes("reset_password")

    /* =========================
       Maintenance
       ========================= */
    object MaintenanceScreen : Routes("maintenance?message={message}") {
        fun createRoute(
            message: String = "AutoAid is currently under maintenance. Please try again later."
        ): String {
            return "maintenance?message=${Uri.encode(message)}"
        }
    }

    /* =========================
       Main
       ========================= */
    object HomeScreen : Routes("home")
    object SettingsScreen : Routes("settings")
    object UserInfoScreen : Routes("user_info")
    object NotificationScreen : Routes("notifications")

    /* =========================
       Garage
       ========================= */
    object GarageScreen : Routes("garage")

    object GarageProvidersScreen : Routes("garage_providers")

    object GarageRequestScreen : Routes("garage_request?providerId={providerId}") {
        fun createRoute(providerId: String? = null): String {
            return if (providerId.isNullOrBlank()) {
                "garage_request"
            } else {
                "garage_request?providerId=${Uri.encode(providerId)}"
            }
        }
    }

    object GarageActiveScreen : Routes("garage_active/{requestId}") {
        fun createRoute(requestId: String): String =
            "garage_active/${Uri.encode(requestId)}"
    }

    object GarageHistoryScreen : Routes("garage_history")

    /* =========================
       TOWING
       ========================= */
    object TowingScreen : Routes("towing")
    object TowingProvidersScreen : Routes("towing_providers")

    object TowingRequestScreen : Routes("towing_request?providerId={providerId}") {
        fun createRoute(providerId: String? = null): String {
            return if (providerId.isNullOrBlank()) {
                "towing_request"
            } else {
                "towing_request?providerId=${Uri.encode(providerId)}"
            }
        }
    }

    object TowingActiveScreen : Routes("towing_active/{requestId}") {
        fun createRoute(requestId: String): String =
            "towing_active/${Uri.encode(requestId)}"
    }

    object TowingHistoryScreen : Routes("towing_history")

    /* =========================
       Fuel
       ========================= */
    object FuelScreen : Routes("fuel")
    object FuelProvidersScreen : Routes("fuel_providers")

    object FuelRequestScreen : Routes("fuel_request?providerId={providerId}") {
        fun createRoute(providerId: String? = null): String {
            return if (providerId.isNullOrBlank()) {
                "fuel_request"
            } else {
                "fuel_request?providerId=${Uri.encode(providerId)}"
            }
        }
    }

    object FuelActiveScreen : Routes("fuel_active/{requestId}") {
        fun createRoute(requestId: String): String =
            "fuel_active/${Uri.encode(requestId)}"
    }

    object FuelHistoryScreen : Routes("fuel_history")

    /* =========================
       Ambulance
       ========================= */
    object AmbulanceScreen : Routes("ambulance")
    object AmbulanceProvidersScreen : Routes("ambulance_providers")

    object AmbulanceRequestScreen : Routes("ambulance_request?providerId={providerId}") {
        fun createRoute(providerId: String? = null): String {
            return if (providerId.isNullOrBlank()) {
                "ambulance_request"
            } else {
                "ambulance_request?providerId=${Uri.encode(providerId)}"
            }
        }
    }

    object AmbulanceActiveScreen : Routes("ambulance_active/{requestId}") {
        fun createRoute(requestId: String): String =
            "ambulance_active/${Uri.encode(requestId)}"
    }

    object AmbulanceHistoryScreen : Routes("ambulance_history")

    /* =========================
       Legal
       ========================= */
    object AboutUsScreen : Routes("about_us")
    object PrivacyPolicyScreen : Routes("privacy_policy")
    object PromotionScreen : Routes("promotion")
    object PayoutInformationScreen : Routes("payout_information")

    object TermsAndConditionsScreen : Routes("terms_and_conditions?fromSignup={fromSignup}") {
        fun createRoute(fromSignup: Boolean = false): String =
            "terms_and_conditions?fromSignup=$fromSignup"
    }

    /* =========================
       Provider Core
       ========================= */
    object ProviderDashboard : Routes("provider_dashboard")
    object EditProviderProfile : Routes("edit_provider_profile")

    object ProviderActiveJob : Routes("provider_active_job/{requestId}") {
        fun createRoute(requestId: String): String =
            "provider_active_job/${Uri.encode(requestId)}"
    }

    object ProviderMapScreen :
        Routes("provider_map/{requestId}?pickupLat={pickupLat}&pickupLng={pickupLng}") {

        fun createRoute(requestId: String, pickupLat: Double, pickupLng: Double): String {
            return "provider_map/${Uri.encode(requestId)}?pickupLat=$pickupLat&pickupLng=$pickupLng"
        }
    }

    object ProviderRequestDetails : Routes("provider_request_details/{requestId}") {
        fun createRoute(requestId: String): String =
            "provider_request_details/${Uri.encode(requestId)}"
    }

    /* =========================
       Provider Dashboard Navigation
       ========================= */
    object ProviderNotifications : Routes("provider_notifications")
    object ProviderMapHome : Routes("provider_map_home")
    object ProviderChatList : Routes("provider_chat_list")

    object ProviderChatThread : Routes("provider_chat_thread/{requestId}") {
        fun createRoute(requestId: String): String =
            "provider_chat_thread/${Uri.encode(requestId)}"
    }

    object ProviderProfile : Routes("provider_profile")

    /* =========================
       Location Picker
       ========================= */
    object LocationPicker : Routes("location_picker?lat={lat}&lng={lng}") {
        fun createRoute(lat: Double? = null, lng: Double? = null): String {
            return if (lat == null || lng == null) {
                "location_picker"
            } else {
                "location_picker?lat=$lat&lng=$lng"
            }
        }
    }
}