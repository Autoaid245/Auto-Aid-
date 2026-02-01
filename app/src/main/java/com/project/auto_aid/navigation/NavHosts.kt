package com.project.auto_aid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.auto_aid.model.OnBoardScreen
import com.project.auto_aid.model.SplashScreen
import com.project.auto_aid.screens.ForgotPasswordScreen
import com.project.auto_aid.screens.HomeScreen
import com.project.auto_aid.screens.LoginScreen
import com.project.auto_aid.screens.ResetPasswordScreen
import com.project.auto_aid.screens.SignupScreen
import com.project.auto_aid.screens.UserInfoScreen
import com.project.auto_aid.screens.VerifyCodeScreen
import com.project.auto_aid.selectDocument.SelectDocumentScreen
import com.project.auto_aid.settings.IDVerificationScreen
import com.project.auto_aid.settings.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {

        composable(Routes.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(Routes.OnBoardScreen.route) {
            OnBoardScreen(navController)
        }

        composable(Routes.LoginScreen.route) {
            LoginScreen(navController)
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


        composable(Routes.SignupScreen.route) {
            SignupScreen(navController)
        }

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



    }
}
