package com.example.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.example.data.model.EmergencyTriageData
import com.example.data.model.EmergencyTriageItem
import com.example.data.model.HospitalEntity
import com.example.data.model.TrafficAlertEntity
import com.example.data.model.UrgencyLevel
import com.example.ui.components.DoctorCard
import com.example.ui.components.EmergencyHeaderBanner
import com.example.ui.components.HospitalCard
import com.example.ui.components.dialNumber
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyGreen
import com.example.ui.theme.EmergencyRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitizenTriageScreen(
    activeAlerts: List<TrafficAlertEntity>,
    doctors: List<DoctorEntity>,
    hospitals: List<HospitalEntity>,
    selectedTriage: EmergencyTriageItem?,
    onSelectTriage: (EmergencyTriageItem?) -> Unit,
    onTriggerSos: (emergencyTitle: String, urgency: String, specialist: String, hospital: String, dialNumber: String) -> Unit,
    onNavigateToDoctorTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    // If detail view is open, back button clears triage selection
    if (selectedTriage != null) {
        BackHandler {
            onSelectTriage(null)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("citizen_triage_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. National Emergency Header (108 / 112 / 102 / 104) & Active Green Corridor Alert
        item {
            EmergencyHeaderBanner(activeAlerts = activeAlerts)
        }

        // If a triage situation is selected, display the detailed triage decision screen!
        if (selectedTriage != null) {
            item {
                TriageDetailHeader(
                    triage = selectedTriage,
                    onBack = { onSelectTriage(null) }
                )
            }

            // CRITICAL QUESTION ANSWER: Which specialist doctor to meet?
            item {
                SpecialistRecommendationCard(triage = selectedTriage)
            }

            // Urgent SOS Action Card
            item {
                TriageSosActionCard(
                    triage = selectedTriage,
                    nearestHospital = hospitals.minByOrNull { it.distanceKm }?.name ?: "District Civil Hospital",
                    onSosClick = {
                        val targetHosp = hospitals.minByOrNull { it.distanceKm }?.name ?: "District Civil Hospital"
                        onTriggerSos(
                            selectedTriage.title,
                            selectedTriage.urgencyLevel.label,
                            selectedTriage.recommendedDoctorTitle,
                            targetHosp,
                            selectedTriage.emergencyHotline
                        )
                        dialNumber(context, selectedTriage.emergencyHotline)
                    }
                )
            }

            // Crucial Do's and Don'ts (Indian emergency protocol)
            item {
                TriageFirstAidCard(triage = selectedTriage)
            }

            // Recommended Nearest Equipped Hospitals
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Nearest Equipped Hospitals (Real-Time Traffic)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = selectedTriage.hospitalRequirement,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            val matchingHospitals = hospitals.filter { hosp ->
                when (selectedTriage.requiredSpecialty) {
                    "Cardiologist" -> hosp.hasCathLab
                    "Neurologist" -> hosp.hasStrokeUnit
                    "Trauma Surgeon" -> hosp.hasTraumaCenter
                    "Burn Specialist" -> hosp.hasBurnUnit
                    "Emergency Physician" -> hosp.hasAntiSnakeVenom
                    else -> true
                }
            }.ifEmpty { hospitals }

            items(matchingHospitals.take(3), key = { "hosp_${it.id}" }) { hospital ->
                HospitalCard(hospital = hospital)
            }

            // Recommended Specialists (Showing dual-practice convenience)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "On-Duty Specialists to Consult",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Available in Hospital & Private Evening Clinics",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val matchingDoctors = doctors.filter {
                it.specialty.contains(selectedTriage.requiredSpecialty, ignoreCase = true)
            }.ifEmpty {
                doctors.take(3)
            }

            items(matchingDoctors, key = { "doc_${it.id}" }) { doctor ->
                DoctorCard(doctor = doctor)
            }

        } else {
            // MAIN EMERGENCY SELECTION DASHBOARD
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "What is the Critical Situation?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Select symptom to find exact specialist doctor, first aid & nearest hospital with real-time green corridor sync",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Search Bar for symptoms
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("triage_search_input"),
                    placeholder = { Text("Search symptom (e.g., chest pain, snake bite, accident, stroke)") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    singleLine = true
                )
            }

            // Triage Cards Grid/List
            val filteredTriage = EmergencyTriageData.allEmergencies.filter {
                searchQuery.isBlank() ||
                        it.title.contains(searchQuery, ignoreCase = true) ||
                        it.hindiTitle.contains(searchQuery, ignoreCase = true) ||
                        it.requiredSpecialty.contains(searchQuery, ignoreCase = true) ||
                        it.keySymptoms.any { sym -> sym.contains(searchQuery, ignoreCase = true) }
            }

            items(filteredTriage, key = { it.id }) { triageItem ->
                TriageSelectorCard(
                    triage = triageItem,
                    onClick = { onSelectTriage(triageItem) }
                )
            }

            // Quick Dual-Practice Doctor Spotlight
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Featured Doctors (Dual Practice)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Govt Hospital in morning • Private Clinic in evening",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = onNavigateToDoctorTab,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("View All", fontSize = 12.sp)
                    }
                }
            }

            val dualPracticeDocs = doctors.filter { it.doctorType == "DUAL_PRACTICE" }.take(2)
            items(dualPracticeDocs, key = { "dual_${it.id}" }) { doc ->
                DoctorCard(doctor = doc)
            }
        }
    }
}

@Composable
fun TriageSelectorCard(
    triage: EmergencyTriageItem,
    onClick: () -> Unit
) {
    val urgencyColor = when (triage.urgencyLevel) {
        UrgencyLevel.CODE_RED -> EmergencyRed
        UrgencyLevel.CODE_AMBER -> EmergencyAmber
        UrgencyLevel.CODE_YELLOW -> Color(0xFF2563EB)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("triage_card_${triage.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(urgencyColor.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Emergency,
                    contentDescription = triage.title,
                    tint = urgencyColor,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = triage.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = urgencyColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = triage.urgencyLevel.name.replace("_", " "),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = urgencyColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = triage.hindiTitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = "Doctor",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Meet: ${triage.recommendedDoctorTitle}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun TriageDetailHeader(
    triage: EmergencyTriageItem,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("triage_detail_back_button")
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back to all emergencies",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = triage.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = triage.hindiTitle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SpecialistRecommendationCard(triage: EmergencyTriageItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("specialist_recommendation_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0C2A4A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "CRITICAL SITUATION PROTOCOL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF93C5FD),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "WHICH SPECIALIST DOCTOR TO MEET:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = triage.recommendedDoctorTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF67E8F9)
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Urgency level badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Triage Priority",
                            fontSize = 10.sp,
                            color = Color(0xFFE2E8F0)
                        )
                        Text(
                            text = triage.urgencyLevel.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFCA5A5)
                        )
                    }
                }

                // Golden Hour window
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Golden Hour Window",
                            fontSize = 10.sp,
                            color = Color(0xFFE2E8F0)
                        )
                        Text(
                            text = "${triage.goldenHourMinutes} Minutes",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDE047)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TriageSosActionCard(
    triage: EmergencyTriageItem,
    nearestHospital: String,
    onSosClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.NotificationImportant,
                    contentDescription = "Alert",
                    tint = EmergencyRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "EMERGENCY AMBULANCE & TRAFFIC DISPATCH",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B)
                    )
                    Text(
                        text = "Syncs with 108 Emergency & Nearest Hospital: $nearestHospital",
                        fontSize = 12.sp,
                        color = Color(0xFF7F1D1D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSosClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sos_dispatch_button"),
                colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "SOS Call",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "DIAL ${triage.emergencyHotline} & ALERT NEAREST HOSPITAL",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun TriageFirstAidCard(triage: EmergencyTriageItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Immediate First-Aid Protocol (Indian Health Rules)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))

            // DO's
            Text(
                text = "DO'S (Immediate Actions):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmergencyGreen
            )
            triage.firstAidDos.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Do",
                        tint = EmergencyGreen,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(10.dp))

            // DONT's
            Text(
                text = "DON'TS (Avoid Dangerous Mistakes):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmergencyRed
            )
            triage.firstAidDonts.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Don't",
                        tint = EmergencyRed,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
