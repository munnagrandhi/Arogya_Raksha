package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HospitalEntity
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyGreen
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.TirangaGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HospitalCard(
    hospital: HospitalEntity,
    modifier: Modifier = Modifier,
    onNavigateClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val hospitalTypeLabel = when (hospital.type) {
        "GOVT_APEX" -> "Apex Central Govt Hospital"
        "GOVT_DISTRICT" -> "District Civil / Govt Hospital"
        "PRIVATE_MULTISPECIALITY" -> "Private Super Speciality"
        else -> "Hospital"
    }

    val hospitalTypeColor = when (hospital.type) {
        "GOVT_APEX", "GOVT_DISTRICT" -> TirangaGreen
        else -> Color(0xFF1E40AF)
    }

    val trafficColor = when (hospital.trafficCondition) {
        "CLEAR" -> EmergencyGreen
        "MODERATE" -> EmergencyAmber
        else -> EmergencyRed
    }

    val trafficText = when (hospital.trafficCondition) {
        "CLEAR" -> "Smooth Transit"
        "MODERATE" -> "Moderate Traffic"
        else -> "Congested Route"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hospital_card_${hospital.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Name & Type
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(hospitalTypeColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = "Hospital Icon",
                            tint = hospitalTypeColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = hospital.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = hospitalTypeLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = hospitalTypeColor
                        )
                    }
                }

                // ETA & Distance
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Travel Time",
                            tint = trafficColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${hospital.estimatedTravelMinutes} mins",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = trafficColor
                        )
                    }
                    Text(
                        text = "${hospital.distanceKm} km away",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Address
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Address",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = hospital.address,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Green Corridor Active Alert
            if (hospital.greenCorridorActive) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = "Green Corridor",
                            tint = EmergencyGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "ACTIVE GREEN CORRIDOR SYNCED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF14532D)
                            )
                            if (hospital.greenCorridorRoute.isNotBlank()) {
                                Text(
                                    text = hospital.greenCorridorRoute,
                                    fontSize = 11.sp,
                                    color = Color(0xFF166534)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Live Bed Count Ticker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ICU Beds Available",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${hospital.icuBedsAvailable} / ${hospital.totalIcuBeds}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hospital.icuBedsAvailable > 0) EmergencyGreen else EmergencyRed
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Oxygen Beds",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${hospital.oxygenBedsAvailable} Units",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = trafficColor.copy(alpha = 0.12f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Traffic Route",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = trafficText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = trafficColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Critical Capabilities Chips
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (hospital.isAyushmanBharatEmpanelled) {
                    CapabilityChip(label = "Ayushman Bharat (AB-PMJAY)", bg = Color(0xFFEFF6FF), fg = Color(0xFF1E40AF))
                }
                if (hospital.hasCathLab) {
                    CapabilityChip(label = "Cath Lab", bg = Color(0xFFFEF2F2), fg = Color(0xFF991B1B))
                }
                if (hospital.hasStrokeUnit) {
                    CapabilityChip(label = "Stroke Unit", bg = Color(0xFFFAF5FF), fg = Color(0xFF6B21A8))
                }
                if (hospital.hasTraumaCenter) {
                    CapabilityChip(label = "Trauma Center", bg = Color(0xFFFFF7ED), fg = Color(0xFFC2410C))
                }
                if (hospital.hasAntiSnakeVenom) {
                    CapabilityChip(label = "Anti-Snake Venom (ASV)", bg = Color(0xFFF0FDF4), fg = Color(0xFF15803D))
                }
                if (hospital.hasBurnUnit) {
                    CapabilityChip(label = "Burn Unit", bg = Color(0xFFFEF2F2), fg = Color(0xFFDC2626))
                }
                if (hospital.hasBloodBank) {
                    CapabilityChip(label = "Blood Bank", bg = Color(0xFFF1F5F9), fg = Color(0xFF334155))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Call Hospital Emergency
                Button(
                    onClick = { dialNumber(context, hospital.emergencyPhone) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("call_hospital_${hospital.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Hospital",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Emergency Call", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Quick Navigation / GPS
                OutlinedButton(
                    onClick = {
                        onNavigateClick()
                        openMapsNavigation(context, hospital.latitude, hospital.longitude, hospital.name)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("navigate_hospital_${hospital.id}"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Quick Navigation",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Directions", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CapabilityChip(label: String, bg: Color, fg: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bg
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = fg,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

fun openMapsNavigation(context: Context, lat: Double, lng: Double, label: String) {
    try {
        val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(label)})")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        context.startActivity(mapIntent)
    } catch (_: Exception) {
        // Fallback to web browser maps
        try {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
            )
            context.startActivity(webIntent)
        } catch (_: Exception) {}
    }
}
