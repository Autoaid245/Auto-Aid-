@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.project.auto_aid.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun OnBoardScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val pages = listOf(
        OnBoardModel(
            title = "Instant Vehicle Assistance",
            description = "Get roadside help, towing, and emergency support anytime, anywhere with AutoAid.",
            imageRes = R.drawable.logo10,
            buttonText = "Next"
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

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* ---------- PAGER ---------- */
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->

            val model = pages[page]

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

        /* ---------- DOT INDICATOR ---------- */
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(pages.size) { index ->
                val color =
                    if (pagerState.currentPage == index)
                        Color(0xFF0A9AD9)
                    else
                        Color.Gray

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        /* ---------- BUTTON ---------- */
        AuthenticationButton(
            title = pages[pagerState.currentPage].buttonText,
            onClick = {
                scope.launch {

                    if (pagerState.currentPage < pages.lastIndex) {

                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )

                    } else {

                        navController.navigate(Routes.ConsentScreen.route) {
                            popUpTo(Routes.OnBoardScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AuthenticationButton(
    title: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(50.dp),
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

@Preview(showBackground = true)
@Composable
fun OnBoardScreenPreview() {
    OnBoardScreen(navController = rememberNavController())
}