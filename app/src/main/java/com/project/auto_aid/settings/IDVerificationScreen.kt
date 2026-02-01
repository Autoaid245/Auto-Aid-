package com.project.auto_aid.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes

@Composable
fun IDVerificationScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(80.dp))

        // -------- TOP ILLUSTRATION --------
        Image(
            painter = painterResource(id = R.drawable.id), // your illustration
            contentDescription = "Verification illustration",
            modifier = Modifier.height(240.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        // -------- TITLE --------
        Text(
            text = "We will take a minute to verify\nyour identity",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF0A273B)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // -------- SUBTEXT --------
        Text(
            text = "We are required by law to verify your identity\n" +
                    "before you can pay with your cashless wallet\n" +
                    "balance. Your information will be stored\nsecurely.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        // -------- VERIFY IDENTITY BUTTON (GO TO SELECT DOCUMENT SCREEN) --------
        Button(
            onClick = {
                navController.navigate("select_document_screen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0A9AD8),
                contentColor = Color.White
            )
        ) {
            Text("VERIFY IDENTITY", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // -------- NOT NOW BUTTON (GO BACK TO SETTINGS) --------
        Button(
            onClick = {
                navController.navigate(Routes.SettingsScreen.route) {
                    popUpTo(Routes.IDVerificationScreen.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(2.dp, Color(0xFF0A9AD8), RoundedCornerShape(40.dp)),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF0A9AD8)
            )
        ) {
            Text("NOT NOW", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
