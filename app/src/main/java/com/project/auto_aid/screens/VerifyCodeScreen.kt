package com.project.auto_aid.screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.auto_aid.navigation.Routes

@Composable
fun VerifyCodeScreen(navController: NavController) {

    var code by remember { mutableStateOf("") }
    val context = LocalContext.current
    val isCodeValid = code.length == 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {

        // 🔙 Back
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { navController.navigateUp() }
        ) {
            Text("", fontSize = 20.sp, color = Color(0xFF0A9AD9))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Back",
                color = Color(0xFF0A9AD9),
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(100.dp))

        // Title
        Text(
            text = "Verify your Identity",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Please enter the 6-digit verification code sent to your email or phone.",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Verification Code Input
        OutlinedTextField(
            value = code,
            onValueChange = {
                if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                    code = it
                }
            },
            label = { Text("Verification Code") },
            placeholder = { Text("------") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = VisualTransformation.None,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = " sent to your Email ",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Verify Button
        Button(
            onClick = {
                if (isCodeValid) {
                    navController.navigate(Routes.ResetPasswordScreen.route)
                } else {
                    Toast.makeText(
                        context,
                        "Enter a valid 6-digit code",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = isCodeValid,
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0A9AD9),
                contentColor = Color.White
            )
        ) {
            Text("Verify", fontSize = 18.sp)
        }




        Spacer(modifier = Modifier.height(20.dp))

        // Resend Code
        Text(
            text = "Resend Code",
            color = Color(0xFF0A9AD9),
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    Toast.makeText(
                        context,
                        "Verification code resent",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyCodePreview() {
    VerifyCodeScreen(rememberNavController())
}
