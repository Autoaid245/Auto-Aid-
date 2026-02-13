package com.project.auto_aid.provider.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.project.auto_aid.provider.model.Provider

/* ---------- COLORS ---------- */
val BackgroundSoft = Color(0xFFF6F8FB)
val TextPrimary = Color(0xFF1A1A1A)
val TextSecondary = Color(0xFF7A7A7A)

@Composable
fun ProviderProfileCard(
    provider: Provider,
    onOnlineChange: (Boolean) -> Unit = {},
    onEditProfile: () -> Unit = {},
    onChangeProfileImage: () -> Unit = {}
) {

    var isOnline by remember { mutableStateOf(true) }

    val serviceColor = when (provider.providerType.lowercase()) {
        "garage" -> Color(0xFF0A9AD9)
        "towing" -> Color(0xFFFF9800)
        "fuel" -> Color(0xFF4CAF50)
        "ambulance" -> Color(0xFFE53935)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {

            /* ---------- HEADER ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                serviceColor.copy(alpha = 0.20f),
                                serviceColor.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(16.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    /* PROFILE IMAGE */
                    Box(contentAlignment = Alignment.BottomEnd) {

                        if (provider.profileImageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = provider.profileImageUrl,
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(74.dp)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, serviceColor.copy(.7f), CircleShape)
                                    .background(Color.White, CircleShape)
                                    .clickable { onChangeProfileImage() }
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(74.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                                    .clickable { onChangeProfileImage() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(40.dp))
                            }
                        }

                        /* CAMERA ICON */
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(serviceColor)
                                .clickable { onChangeProfileImage() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {

                        /* NAME + VERIFIED */
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                provider.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.2.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(Modifier.width(6.dp))

                            VerifiedBadge()
                        }

                        Text(
                            provider.phone,
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            }

            /* ---------- STATUS ---------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AssistChip(
                    onClick = {},
                    label = { Text(provider.providerType.uppercase()) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = serviceColor.copy(.15f),
                        labelColor = serviceColor
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                if (isOnline) "ONLINE" else "OFFLINE",
                                color = if (isOnline) Color(0xFF4CAF50) else Color.Gray
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor =
                                if (isOnline) Color(0xFF4CAF50).copy(.15f)
                                else Color.LightGray
                        )
                    )

                    Spacer(Modifier.width(6.dp))

                    Switch(
                        checked = isOnline,
                        onCheckedChange = {
                            isOnline = it
                            onOnlineChange(it)
                        }
                    )
                }
            }

            Divider()

            /* ---------- STATS ---------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard("Rating", "⭐ ${provider.rating}")
                StatCard("Today", "UGX 45K")
                StatCard("Week", "UGX 280K")
            }
        }
    }
}

/* ---------- VERIFIED BADGE ---------- */
@Composable
fun VerifiedBadge() {
    Row(
        modifier = Modifier
            .background(Color(0xFFE8F5E9), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("✔", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(3.dp))
        Text("Verified", color = Color(0xFF2E7D32), style = MaterialTheme.typography.labelSmall)
    }
}

/* ---------- MINI STAT CARD ---------- */
@Composable
private fun StatCard(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundSoft)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}