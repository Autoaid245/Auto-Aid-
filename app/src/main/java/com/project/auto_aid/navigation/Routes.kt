package com.project.auto_aid.navigation

sealed class Routes(val route: String) {
    
    /* Splash & Onboard */
    object SplashScreen : Routes("splash")
    object OnBoardScreen : Routes("onboard")
    object ConsentScreen : Routes("consent_screen")

    /* Auth */
    object LoginScreen : Routes("login")
    object SignupScreen : Routes("signup")
    object ForgotPasswordScreen : Routes("forgot_password")
    object VerifyCodeScreen : Routes("verify_code")
    object ResetPasswordScreen : Routes("reset_password")

    /* Main */
    object HomeScreen : Routes("home")
    object SettingsScreen : Routes("settings")
    object UserInfoScreen : Routes("user_info")

    /* Garage */
    object GarageScreen : Routes("garage")
    object GarageRequestScreen : Routes("garage_request")
    object RequestProcessingScreen : Routes("request_processing")
    object MechanicAssignedScreen : Routes("mechanic_assigned")
    object HelpCompletedScreen : Routes("help_completed")

    /* Towing */
    object TowingScreen : Routes("towing")
    object TowingRequestScreen : Routes("towing_request")
    object TowingActiveScreen : Routes("towing_active")
    object TowingHistoryScreen : Routes("towing_history")

    /* Fuel */
    object FuelScreen : Routes("fuel")
    object FuelRequestScreen : Routes("fuel_request")
    object FuelActiveScreen : Routes("fuel_active")
    object FuelHistoryScreen : Routes("fuel_history")

    /* Ambulance */
    object AmbulanceScreen : Routes("ambulance")
    object AmbulanceRequestScreen : Routes("ambulance_request")
    object AmbulanceStatusScreen : Routes("ambulance_status")
    object AmbulanceHistoryScreen : Routes("ambulance_history")

    /* Legal */
    object AboutUsScreen : Routes("about_us")
    object PrivacyPolicyScreen : Routes("privacy_policy")
    object PromotionScreen : Routes("promotion")
    object PayoutInformationScreen : Routes("payout_information")
    object TermsAndConditionsScreen : Routes("terms_and_conditions?fromSignup={fromSignup}")

    /* Provider */
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

}
