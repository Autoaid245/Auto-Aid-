package com.project.auto_aid.navigation

sealed class Routes(val route: String) {

<<<<<<< HEAD
    // Splash & Onboard
    object SplashScreen : Routes("splash")
    object OnBoardScreen : Routes("onboard")

    // Auth
=======
    /* ---------- Splash & Onboard ---------- */
    object SplashScreen : Routes("splash")
    object OnBoardScreen : Routes("onboard")
    object ConsentScreen : Routes("consent_screen")

    /* ---------- Auth ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    object LoginScreen : Routes("login")
    object SignupScreen : Routes("signup")
    object ForgotPasswordScreen : Routes("forgot_password")
    object VerifyCodeScreen : Routes("verify_code")
    object ResetPasswordScreen : Routes("reset_password")

<<<<<<< HEAD
    // Main
=======
    /* ---------- Main ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    object HomeScreen : Routes("home")
    object SettingsScreen : Routes("settings")
    object UserInfoScreen : Routes("user_info")

<<<<<<< HEAD
    // Garage
=======
    /* ---------- Garage (User) ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    object GarageScreen : Routes("garage")
    object GarageRequestScreen : Routes("garage_request")
    object RequestProcessingScreen : Routes("request_processing")
    object MechanicAssignedScreen : Routes("mechanic_assigned")
    object HelpCompletedScreen : Routes("help_completed")

<<<<<<< HEAD
    // Towing
=======
    /* ---------- Towing (User) ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    object TowingScreen : Routes("towing")
    object TowingRequestScreen : Routes("towing_request")
    object TowingActiveScreen : Routes("towing_active")
    object TowingHistoryScreen : Routes("towing_history")

<<<<<<< HEAD
    // Fuel
=======
    /* ---------- Fuel (User) ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    object FuelScreen : Routes("fuel")
    object FuelRequestScreen : Routes("fuel_request")
    object FuelActiveScreen : Routes("fuel_active")
    object FuelHistoryScreen : Routes("fuel_history")

<<<<<<< HEAD
    // Ambulance
=======
    /* ---------- Ambulance (User) ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    object AmbulanceScreen : Routes("ambulance")
    object AmbulanceRequestScreen : Routes("ambulance_request")
    object AmbulanceStatusScreen : Routes("ambulance_status")
    object AmbulanceHistoryScreen : Routes("ambulance_history")

<<<<<<< HEAD
    // Legal & Info
=======
    /* ---------- Legal & Info ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    object AboutUsScreen : Routes("about_us")
    object PrivacyPolicyScreen : Routes("privacy_policy")
    object PromotionScreen : Routes("promotion")
    object PayoutInformationScreen : Routes("payout_information")

<<<<<<< HEAD
    object TermsAndConditionsScreen : Routes("terms_and_conditions") {
        const val routeWithArgs = "terms_and_conditions?fromSignup={fromSignup}"
=======
    /* ---------- PROVIDER ---------- */
    object ProviderDashboard : Routes("provider_dashboard")

    object EditProviderProfile : Routes("edit_provider_profile")

    object ProviderActiveJob : Routes("provider_active_job/{requestId}") {
        fun createRoute(requestId: String) =
            "provider_active_job/$requestId"
    }

    object ProviderMapScreen : Routes("provider_map/{requestId}") {
        fun createRoute(requestId: String) =
            "provider_map/$requestId"
    }

    /* ---------- Terms & Conditions ---------- */
    object TermsAndConditionsScreen : Routes("terms_and_conditions") {
        const val routeWithArgs =
            "terms_and_conditions?fromSignup={fromSignup}"
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    }
}