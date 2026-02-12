@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.project.auto_aid.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
<<<<<<< HEAD
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import com.project.auto_aid.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
=======
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
<<<<<<< HEAD
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun OnBoardScreen(navController: NavHostController, modifier: Modifier = Modifier) {
=======
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun OnBoardScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0

    val pages = listOf(
        OnBoardModel(
            title = "Instant Vehicle Assistance",
            description = "Get roadside help, towing, and emergency support anytime, anywhere with AutoAid.",
            imageRes = R.drawable.logo10,
<<<<<<< HEAD
            buttonText = "Skip"
=======
            buttonText = "Next"   // ✅ changed
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        ),
        OnBoardModel(
            title = "Track & Manage Repairs",
            description = "Keep track of your vehicle services, repairs, and maintenance history all in one place.",
            imageRes = R.drawable.logo11,
            buttonText = "Next"
        ),
        OnBoardModel(
            title = "Smart & User-Friendly!",
            description = "Easily request assistance, track your requests, and stay updated with a simple, intuitive interface.",
            imageRes = R.drawable.fuel,
            buttonText = "Get Started"
        )
    )

<<<<<<< HEAD
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
=======
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
<<<<<<< HEAD
=======

        /* ---------- PAGER ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
<<<<<<< HEAD
            val model = pages[page]
=======

            val model = pages[page]

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                Image(
                    painter = painterResource(id = model.imageRes),
                    contentDescription = model.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.65f)
                        .clip(RoundedCornerShape(37.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = model.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color(0xFF0A9AD9)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = model.description,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

<<<<<<< HEAD
        // Dots indicator
=======
        /* ---------- DOT INDICATOR ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(pages.size) { index ->
                val color =
<<<<<<< HEAD
                    if (pagerState.currentPage == index) Color(0xFF0A9AD9) else Color.Gray
=======
                    if (pagerState.currentPage == index)
                        Color(0xFF0A9AD9)
                    else
                        Color.Gray

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                )
            }
        }

<<<<<<< HEAD
        Spacer(modifier = Modifier.height(1.dp))

=======
        Spacer(modifier = Modifier.height(12.dp))

        /* ---------- BUTTON ---------- */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        AuthenticationButton(
            title = pages[pagerState.currentPage].buttonText,
            onClick = {
                scope.launch {
<<<<<<< HEAD
                    when (pagerState.currentPage) {
                        0, 1 -> pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        pages.lastIndex ->
                            navController.navigate(Routes.LoginScreen.route)
=======

                    // If NOT last page → move forward
                    if (pagerState.currentPage < pages.lastIndex) {

                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )

                    } else {

                        // ✅ Only on LAST page → go to Consent
                        navController.navigate(Routes.ConsentScreen.route) {
                            popUpTo(Routes.OnBoardScreen.route) {
                                inclusive = true
                            }
                        }
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                    }
                }
            }
        )
<<<<<<< HEAD
=======

        Spacer(modifier = Modifier.height(24.dp))
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    }
}

@Composable
<<<<<<< HEAD
fun AuthenticationButton(title: String, onClick: () -> Unit) {
=======
fun AuthenticationButton(
    title: String,
    onClick: () -> Unit
) {
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
<<<<<<< HEAD
            .padding(29.dp)
            .height(50.dp)
            .padding(horizontal = 16.dp),
=======
            .padding(horizontal = 24.dp)
            .height(50.dp),
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0A9AD9),
            contentColor = Color.White
        )
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

<<<<<<< HEAD
@Preview(showBackground = true, device = "spec:width=360dp,height=640dp")
@Composable
fun OnBoardScreenPreview() {
    val navController = rememberNavController()
    OnBoardScreen(navController = navController)
=======
@Preview(showBackground = true)
@Composable
fun OnBoardScreenPreview() {
    OnBoardScreen(navController = rememberNavController())
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
}