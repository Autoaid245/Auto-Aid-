package com.project.auto_aid.screens


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes

@Composable
fun ForgotPasswordScreen(navController: NavController) {

    var input by remember { mutableStateOf("") }
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(90.dp))

        Box(
            modifier = Modifier
                .offset(y = (-20).dp)
                .size(110.dp)
                .border(
                    width = 7.dp,
                    color = Color(0xFF0A9AD9),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        )
        {
            // Icon (top)
            Image(
                painter = painterResource(id = R.drawable.logo01),
                contentDescription = null,
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(25.dp))
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Forgot Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your email or phone number to receive a  verification code.",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input field
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Email or Phone Number") },
            placeholder = { Text("") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Send Code Button
        Button(
            onClick = {
                if (input.isBlank()) {
                    Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (!input.contains("@")) {
                    Toast.makeText(context, "Enter a valid email (phone OTP not done yet)", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                Firebase.auth.sendPasswordResetEmail(input.trim())
                    .addOnSuccessListener {
                        Toast.makeText(context, "Reset link sent. Check your email.", Toast.LENGTH_LONG).show()

                        // ✅ Go to OTP screen (we’ll treat it as “email confirmation screen”)
                        navController.navigate(Routes.VerifyCodeScreen.route)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, it.message ?: "Failed to send email", Toast.LENGTH_LONG).show()
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0A9AD9)
            )
        ) {
            Text("Send Code", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(52.dp))

        // Back to Login
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                navController.navigateUp()
            }
        ) {
            Text("", fontSize = 28.sp)
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Back to Login",
                fontSize = 20.sp,
                color = Color(0xFF0A9AD9),
                fontWeight = FontWeight.Bold

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    ForgotPasswordScreen(rememberNavController())
}
