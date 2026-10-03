package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DoctorEntity
import com.example.data.model.HospitalEntity
import com.example.data.model.TrafficAlertEntity
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyGreen
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.TirangaGreen
import com.example.ui.theme.TirangaSaffron

@Composable
fun GovtOfficerScreen(
    hospitals: List<HospitalEntity>,
    doctors: List<DoctorEntity>,
    trafficAlerts: List<TrafficAlertEntity>,
    onToggleGreenCorridor: (hospitalId: Long, active: Boolean, route: String) -> Unit,
    onUpdateBeds: (hospitalId: Long, icuBeds: Int, oxygenBeds: Int) -> Unit,
    onBroadcastAlert: (title: String, route: String, type: String, hospital: String, officer: String) -> Unit,
    onVerifyDoctor: (doctorId: Long, isVerified: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showBroadcastDialog by remember { mutableStateOf(false) }
    var newAlertTitle by remember { mutableStateOf("") }
    var newAlertRoute by remember { mutableStateOf("") }
    var newAlertType by remember { mutableStateOf("GREEN_CORRIDOR") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("govt_officer_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Govt Officer Badge Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF072448))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(TirangaGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Govt Officer",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "DISTRICT HEALTH & TRAFFIC LIAISON",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF93C5FD),
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "Emergency Response Command Room",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Tricolor accent
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Box(modifier = Modifier.size(6.dp, 16.dp).background(TirangaSaffron, RoundedCornerShape(2.dp)))
                            Box(modifier = Modifier.size(6.dp, 16.dp).background(Color.White, RoundedCornerShape(2.dp)))
                            Box(modifier = Modifier.size(6.dp, 16.dp).background(TirangaGreen, RoundedCornerShape(2.dp)))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Authorized Officer: Dr. A. K. Sharma (CMO) & Insp. S. K. Yadav (Traffic)",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        // Section 1: GREEN CORRIDOR & TRAFFIC DISPATCH
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🚨 Emergency Green Corridor Activation",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Police priority corridor for 108 emergency ambulance transit",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Hospital Green Corridor controls
        items(hospitals, key = { "gc_hosp_${it.id}" }) { hospital ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = hospital.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "ICU Beds: ${hospital.icuBedsAvailable}/${hospital.totalIcuBeds} • Route: ${hospital.trafficCondition}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (hospital.greenCorridorActive) {
                            Text(
                                text = "● Green Corridor ACTIVE: ${hospital.greenCorridorRoute.ifBlank { "Priority Lane Cleared" }}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmergencyGreen
                            )
                        }
                    }

                    Switch(
                        checked = hospital.greenCorridorActive,
                        onCheckedChange = { isActive ->
                            val defaultRoute = "Cleared priority transit lane on arterial highway to ${hospital.name}"
                            onToggleGreenCorridor(hospital.id, isActive, if (isActive) defaultRoute else "")
                            Toast.makeText(
                                context,
                                if (isActive) "Green Corridor ACTIVATED for ${hospital.name}" else "Green Corridor DEACTIVATED",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.testTag("toggle_gc_${hospital.id}")
                    )
                }
            }
        }

        // Section 2: BROADCAST TRAFFIC ALERT
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Broadcast Emergency Traffic Update",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534)
                    )
                    Text(
                        text = "Alert ambulance drivers and citizens about bottlenecks or cleared lanes",
                        fontSize = 11.sp,
                        color = Color(0xFF14532D)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newAlertTitle,
                        onValueChange = { newAlertTitle = it },
                        label = { Text("Alert Title (e.g. Ring Road Heavy Traffic / Clearance)") },
                        modifier = Modifier.fillMaxWidth().testTag("officer_alert_title"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newAlertRoute,
                        onValueChange = { newAlertRoute = it },
                        label = { Text("Route Instructions (e.g. Ambulances use flyover slip lane)") },
                        modifier = Modifier.fillMaxWidth().testTag("officer_alert_route"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (newAlertTitle.isBlank()) {
                                Toast.makeText(context, "Please enter alert title", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            onBroadcastAlert(
                                newAlertTitle,
                                newAlertRoute,
                                newAlertType,
                                "District Arterial Roads",
                                "Traffic Liaison Control Room"
                            )
                            newAlertTitle = ""
                            newAlertRoute = ""
                            Toast.makeText(context, "Emergency traffic alert published!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth().testTag("officer_broadcast_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Navigation, contentDescription = "Broadcast")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Publish Live Traffic Alert", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section 3: NMC DOCTOR CREDENTIAL AUDIT
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🩺 Doctor NMC Verification & Audit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Verify registered doctors against National Medical Commission records",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(doctors, key = { "audit_doc_${it.id}" }) { doc ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = doc.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "NMC Reg: ${doc.nmcRegNumber} • ${doc.specialty}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${doc.hospitalName} ${if (doc.hasPrivateClinic) "+ Private Clinic" else ""}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            val newVerification = !doc.isVerifiedByGovt
                            onVerifyDoctor(doc.id, newVerification)
                            Toast.makeText(
                                context,
                                if (newVerification) "Doctor ${doc.name} Verified" else "Doctor ${doc.name} Unverified",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("verify_btn_${doc.id}")
                    ) {
                        Icon(
                            imageVector = if (doc.isVerifiedByGovt) Icons.Default.Verified else Icons.Default.Warning,
                            contentDescription = "Status",
                            tint = if (doc.isVerifiedByGovt) TirangaGreen else EmergencyAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (doc.isVerifiedByGovt) "NMC Verified" else "Pending Audit",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
