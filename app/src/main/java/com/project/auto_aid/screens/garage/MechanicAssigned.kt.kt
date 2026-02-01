package com.project.auto_aid.screens.garage

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@Composable
fun MechanicAssignedScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Mechanic Assigned ✅", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Text("Name: John Kato")
        Text("Arrival Time: 15 minutes")

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                navController.navigate(Routes.HelpCompletedScreen.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mark Help Completed")
        }
    }
}
