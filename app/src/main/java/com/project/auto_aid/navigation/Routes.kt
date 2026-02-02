package com.project.auto_aid.navigation

sealed class Routes(val route: String) {

    // ================= SPLASH & ONBOARDING =================
    object SplashScreen : Routes("splash")
    object OnBoardScreen : Routes("onboard_screen")

    // ================= AUTH =================
    object LoginScreen : Routes("login_screen")
    object SignupScreen : Routes("signup_screen")
    object ForgotPasswordScreen : Routes("forgot_password")
    object VerifyCodeScreen : Routes("verify_code")
    object ResetPasswordScreen : Routes("reset_password")

    // ================= MAIN APP =================
    object HomeScreen : Routes("home")
    object SettingsScreen : Routes("settings_screen")
    object UserInfoScreen : Routes("user_info_screen")
    object IDVerificationScreen : Routes("id_verification_screen")

    // ================= GARAGE FLOW =================
    object GarageScreen : Routes("garage")
    object GarageRequestScreen : Routes("garage_request")

    // ⏳ Searching for mechanic
    object RequestProcessingScreen : Routes("request_processing")

    // 👨‍🔧 Mechanic assigned
    object MechanicAssignedScreen : Routes("mechanic_assigned")

    // ✅ Help completed
    object HelpCompletedScreen : Routes("help_completed")

    // ================= 🚨 TOWING FLOW =================
    object TowingScreen : Routes("towing")
    object TowingRequestScreen : Routes("towing_request")
    object TowingHistoryScreen : Routes("towing_history")
    object TowingActiveScreen : Routes("towing_active")

    // ================= ⛽ FUEL FLOW =================
    object FuelScreen : Routes("fuel")
    object FuelRequestScreen : Routes("fuel_request")
    object FuelActiveScreen : Routes("fuel_active")
    object FuelHistoryScreen : Routes("fuel_history")

    // ================= 🚑 AMBULANCE =================
    object AmbulanceScreen : Routes("ambulance")
    object AmbulanceRequestScreen : Routes("ambulance_request")
    object AmbulanceStatusScreen : Routes("ambulance_status")
    object AmbulanceHistoryScreen : Routes("ambulance_history")
}
