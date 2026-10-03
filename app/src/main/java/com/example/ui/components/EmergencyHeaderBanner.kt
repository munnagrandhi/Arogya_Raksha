package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TrafficAlertEntity
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyGreen
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.TirangaGreen
import com.example.ui.theme.TirangaSaffron

@Composable
fun EmergencyHeaderBanner(
    activeAlerts: List<TrafficAlertEntity>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("emergency_header_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F2B48)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Title row with Indian National Flag subtle accents
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EmergencyRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = "Emergency Medical Response",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "NATIONAL EMERGENCY HOTLINES",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF93C5FD),
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Instant 24x7 Medical Response (India)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                // Indian Tri-Color Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp, 12.dp).background(TirangaSaffron, RoundedCornerShape(1.dp)))
                    Box(modifier = Modifier.size(6.dp, 12.dp).background(Color.White, RoundedCornerShape(1.dp)))
                    Box(modifier = Modifier.size(6.dp, 12.dp).background(TirangaGreen, RoundedCornerShape(1.dp)))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Hotlines buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EmergencyDialChip(
                    number = "108",
                    serviceName = "Ambulance",
                    accentColor = EmergencyRed,
                    modifier = Modifier.weight(1f),
                    onDial = { dialNumber(context, "108") }
                )
                EmergencyDialChip(
                    number = "112",
                    serviceName = "All Emergency",
                    accentColor = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f),
                    onDial = { dialNumber(context, "112") }
                )
                EmergencyDialChip(
                    number = "102",
                    serviceName = "Janani / Child",
                    accentColor = Color(0xFFD97706),
                    modifier = Modifier.weight(1f),
                    onDial = { dialNumber(context, "102") }
                )
                EmergencyDialChip(
                    number = "104",
                    serviceName = "Health Help",
                    accentColor = Color(0xFF0D9488),
                    modifier = Modifier.weight(1f),
                    onDial = { dialNumber(context, "104") }
                )
            }

            // Green Corridor / Traffic live alert banner if present
            val greenCorridors = activeAlerts.filter { it.alertType == "GREEN_CORRIDOR" }
            if (greenCorridors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                val firstAlert = greenCorridors.first()
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF064E3B),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(EmergencyGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = "Active Green Corridor",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "GREEN CORRIDOR IN PROGRESS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6EE7B7)
                            )
                            Text(
                                text = firstAlert.routeDescription,
                                fontSize = 12.sp,
                                color = Color.White,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmergencyDialChip(
    number: String,
    serviceName: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onDial: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onDial() }
            .testTag("dial_chip_$number"),
        color = Color(0xFF1E3A5F),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call $number",
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = number,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Text(
                text = serviceName,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFCBD5E1),
                maxLines = 1
            )
        }
    }
}

fun dialNumber(context: Context, number: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}
