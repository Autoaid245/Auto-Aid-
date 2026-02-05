package com.project.auto_aid.navigation

sealed class Routes(val route: String) {

    // ================= SPLASH & ONBOARD =================
    object SplashScreen : Routes("splash")
    object OnBoardScreen : Routes("onboard_screen")

    // ================= AUTH =================
    object LoginScreen : Routes("login_screen")
    object SignupScreen : Routes("signup_screen")
    object ForgotPasswordScreen : Routes("forgot_password")
    object VerifyCodeScreen : Routes("verify_code")
    object ResetPasswordScreen : Routes("reset_password")

    // ================= MAIN =================
    object HomeScreen : Routes("home")
    object SettingsScreen : Routes("settings_screen")

    // ✅ PROFILE (INCLUDES ID VERIFICATION FLOW)
    object UserInfoScreen : Routes("user_info_screen")

    // ================= GARAGE =================
    object GarageScreen : Routes("garage")
    object GarageRequestScreen : Routes("garage_request")
    object RequestProcessingScreen : Routes("request_processing")
    object MechanicAssignedScreen : Routes("mechanic_assigned")
    object HelpCompletedScreen : Routes("help_completed")

    // ================= TOWING =================
    object TowingScreen : Routes("towing")
    object TowingRequestScreen : Routes("towing_request")
    object TowingHistoryScreen : Routes("towing_history")
    object TowingActiveScreen : Routes("towing_active")

    // ================= FUEL =================
    object FuelScreen : Routes("fuel")
    object FuelRequestScreen : Routes("fuel_request")
    object FuelActiveScreen : Routes("fuel_active")
    object FuelHistoryScreen : Routes("fuel_history")

    // ================= AMBULANCE =================
    object AmbulanceScreen : Routes("ambulance")
    object AmbulanceRequestScreen : Routes("ambulance_request")
    object AmbulanceStatusScreen : Routes("ambulance_status")
    object AmbulanceHistoryScreen : Routes("ambulance_history")
    object PromotionScreen : Routes("promotion_screen")
    object PayoutInformationScreen : Routes("payout_information")
    object AboutUsScreen : Routes("about_us_screen")
}