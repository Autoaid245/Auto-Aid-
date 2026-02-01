package com.project.auto_aid.navigation

sealed class Routes(val route: String) {

    object ForgotPasswordScreen : Routes("forgot_password")

    object VerifyCodeScreen : Routes("verify_code")

    object ResetPasswordScreen : Routes("reset_password")


    object SplashScreen : Routes("splash")

    object OnBoardScreen : Routes("onboard_screen")
    object LoginScreen : Routes("login_screen")
    object SignupScreen : Routes("signup_screen")
    object HomeScreen : Routes("home_screen")

    object SettingsScreen : Routes("settings_screen")

    object UserInfoScreen : Routes("user_info_screen")

    object IDVerificationScreen : Routes("id_verification_screen")
}
