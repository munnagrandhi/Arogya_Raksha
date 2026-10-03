package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClinicAffiliationData
import com.example.data.model.CompleteDoctorProfile
import com.example.data.model.DoctorCredentialsData
import com.example.data.model.DoctorEntity
import com.example.data.model.DoctorSpecializationData
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.HospitalAffiliationData
import com.example.data.model.toDoctorEntity
import com.example.ui.components.DoctorCredentialsInputForm
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyGreen
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.TirangaGreen

@Composable
fun DoctorPortalScreen(
    doctors: List<DoctorEntity>,
    activeDoctorId: Long?,
    emergencyIncidents: List<EmergencyIncidentEntity>,
    onSelectDoctor: (Long) -> Unit,
    onSaveDoctor: (DoctorEntity) -> Unit,
    onSaveDoctorProfile: ((CompleteDoctorProfile) -> Unit)? = null,
    onUpdateAvailability: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentDoctor = doctors.find { it.id == activeDoctorId } ?: doctors.firstOrNull()

    var isEditing by remember { mutableStateOf(false) }
    var isRegisteringNew by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
    ) {
        // Portal Header
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
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(TirangaGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MedicalServices,
                                    contentDescription = "Doctor Portal",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "DOCTOR ON-DUTY PORTAL",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF93C5FD),
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "National Medical Commission (NMC)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Button(
                            onClick = {
                                isRegisteringNew = true
                                isEditing = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Doctor", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Doctor", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Profile Selector Pills
                    Text(
                        text = "Switch Active Doctor Profile:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFCBD5E1)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        doctors.forEach { doc ->
                            val isSelected = doc.id == currentDoctor?.id && !isRegisteringNew
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Color(0xFF2563EB) else Color.Transparent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .testTag("select_doctor_${doc.id}")
                            ) {
                                Text(
                                    text = doc.name.replace("Dr. ", ""),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                        .clickable {
                                            isRegisteringNew = false
                                            isEditing = false
                                            onSelectDoctor(doc.id)
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Emergency On-Duty Status Switcher
        if (currentDoctor != null && !isRegisteringNew) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("doctor_availability_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Your Emergency On-Duty Status",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Citizens & 108 emergency triage see this live",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { isEditing = !isEditing }) {
                                Icon(
                                    imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 4 Status Pills
                        val statuses = listOf(
                            Triple("AVAILABLE_EMERGENCY", "On-Duty Emergency", EmergencyGreen),
                            Triple("IN_OPD", "In OPD", Color(0xFF2563EB)),
                            Triple("IN_SURGERY", "In Surgery / OT", EmergencyRed),
                            Triple("OFF_DUTY", "Off Duty", Color(0xFF64748B))
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            statuses.forEach { (statusCode, label, color) ->
                                val isSelected = currentDoctor.availabilityStatus == statusCode
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) color else color.copy(alpha = 0.1f),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .testTag("status_btn_$statusCode")
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else color,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(vertical = 8.dp, horizontal = 2.dp)
                                            .clickable {
                                                onUpdateAvailability(currentDoctor.id, statusCode)
                                                Toast.makeText(context, "Status set to: $label", Toast.LENGTH_SHORT).show()
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==========================================================
        // EDIT / REGISTER PROFILE FORM USING DoctorCredentialsInputForm
        // ==========================================================
        if (isEditing || isRegisteringNew) {
            item {
                DoctorCredentialsInputForm(
                    initialProfile = if (isRegisteringNew || currentDoctor == null) null else CompleteDoctorProfile(
                        userId = "doc_${currentDoctor.id}",
                        fullName = currentDoctor.name,
                        email = currentDoctor.email,
                        phone = currentDoctor.phone,
                        role = if (currentDoctor.doctorType == "GOVT_DOCTOR") "GOVERNMENT_DOCTOR" else "PRIVATE_DOCTOR",
                        credentials = DoctorCredentialsData(
                            medicalCouncil = "National Medical Commission (NMC)",
                            registrationNumber = currentDoctor.nmcRegNumber,
                            registrationYear = 2016,
                            primaryDegree = currentDoctor.qualifications.split(",").firstOrNull()?.trim() ?: "MBBS",
                            postGraduateDegrees = currentDoctor.qualifications.split(",").drop(1).joinToString(", ").trim(),
                            additionalCertifications = "Fellowship in Critical Care & Resuscitation",
                            yearsOfExperience = currentDoctor.experienceYears
                        ),
                        hospitalAffiliation = HospitalAffiliationData(
                            hospitalName = currentDoctor.hospitalName,
                            hospitalType = if (currentDoctor.doctorType == "GOVT_DOCTOR") "Government Apex Hospital" else "Private Multispeciality Hospital",
                            designation = currentDoctor.hospitalDesignation,
                            department = "Department of ${currentDoctor.specialty} & Emergency Services",
                            opdTimings = currentDoctor.hospitalTimings,
                            hospitalCity = currentDoctor.hospitalCity,
                            emergencyDutyAvailable = true
                        ),
                        clinicAffiliation = ClinicAffiliationData(
                            runsPrivateClinic = currentDoctor.hasPrivateClinic,
                            clinicName = currentDoctor.clinicName,
                            clinicAddress = currentDoctor.clinicAddress,
                            clinicCity = currentDoctor.hospitalCity,
                            eveningHours = currentDoctor.clinicTimings,
                            consultationFeeInr = currentDoctor.clinicConsultFee,
                            clinicContactPhone = currentDoctor.clinicPhone,
                            appointmentBookingAvailable = true
                        ),
                        specialization = DoctorSpecializationData(
                            primarySpecialty = currentDoctor.specialty,
                            subSpecialties = "Emergency Critical Care & Specialist Consultation",
                            clinicalInterests = "Acute Golden Hour Care & STEMI/Stroke Triage",
                            languagesSpoken = "Hindi, English"
                        )
                    ),
                    onSaveProfile = { profile ->
                        val updatedDoc = profile.toDoctorEntity(if (isRegisteringNew) 0L else (currentDoctor?.id ?: 0L))
                        onSaveDoctor(updatedDoc)
                        onSaveDoctorProfile?.invoke(profile)
                        isEditing = false
                        isRegisteringNew = false
                        Toast.makeText(context, "Doctor credentials & affiliations registered in database!", Toast.LENGTH_SHORT).show()
                    },
                    onCancel = {
                        isEditing = false
                        isRegisteringNew = false
                    }
                )
            }
        }

        // ==========================================================
        // REGISTERED PROFILE OVERVIEW (CREDENTIALS, AFFILIATIONS, SPECIALIZATION)
        // ==========================================================
        if (!isEditing && !isRegisteringNew && currentDoctor != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("doctor_profile_overview_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Title & Type Tag
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currentDoctor.name,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "NMC Verified",
                                        tint = TirangaGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = currentDoctor.qualifications,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Role Pill
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = when (currentDoctor.doctorType) {
                                    "GOVT_DOCTOR" -> TirangaGreen.copy(alpha = 0.12f)
                                    "DUAL_PRACTICE" -> Color(0xFFD97706).copy(alpha = 0.12f)
                                    else -> Color(0xFF2563EB).copy(alpha = 0.12f)
                                }
                            ) {
                                Text(
                                    text = when (currentDoctor.doctorType) {
                                        "GOVT_DOCTOR" -> "Govt Doctor"
                                        "DUAL_PRACTICE" -> "Dual Practice"
                                        else -> "Private Doctor"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (currentDoctor.doctorType) {
                                        "GOVT_DOCTOR" -> TirangaGreen
                                        "DUAL_PRACTICE" -> Color(0xFFD97706)
                                        else -> Color(0xFF2563EB)
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        // 1. Medical Credentials & Regulatory Data
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "📜 Medical Credentials & Regulatory Data",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "• Registration: ${currentDoctor.nmcRegNumber} (National Medical Commission)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Clinical Experience: ${currentDoctor.experienceYears} Years",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // 2. Area of Specialization
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "🎯 Area of Specialization",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C3AED)
                            )
                            Text(
                                text = "• Primary Specialty: ${currentDoctor.specialty}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "• Clinical Focus: Emergency Acute Care, Golden Hour Resuscitation & Triage",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // 3. Current Hospital Affiliation
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFF6FF), RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalHospital, contentDescription = null, tint = Color(0xFF1D4ED8), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Hospital Posting: ${currentDoctor.hospitalName}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A8A)
                                )
                            }
                            Text(
                                text = "Designation: ${currentDoctor.hospitalDesignation}",
                                fontSize = 11.sp,
                                color = Color(0xFF1E40AF)
                            )
                            Text(
                                text = "Daytime OPD Duty: ${currentDoctor.hospitalTimings}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1E3A8A)
                            )
                        }

                        // 4. Current Clinic Affiliation (Dual-Practice)
                        if (currentDoctor.hasPrivateClinic) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFEF3C7), RoundedCornerShape(10.dp))
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Private Clinic: ${currentDoctor.clinicName}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF78350F)
                                    )
                                }
                                Text(
                                    text = "Address: ${currentDoctor.clinicAddress}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF92400E)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Evening Hours: ${currentDoctor.clinicTimings}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF78350F)
                                    )
                                    Text(
                                        text = "Fee: ₹${currentDoctor.clinicConsultFee}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                }
                            }
                        }

                        // Edit Button
                        OutlinedButton(
                            onClick = { isEditing = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_edit_doctor_credentials"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Edit Credentials, Affiliations & Specialization", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Live Emergency Triage Incoming Alerts
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🚨 Real-Time Emergency Triage Pings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Patients in your district receiving emergency triage recommendations",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (emergencyIncidents.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = "No emergency pings registered yet. System in standby.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(emergencyIncidents.take(5), key = { it.id }) { incident ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(EmergencyRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = incident.emergencyType,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7F1D1D)
                                )
                            }
                            Text(
                                text = "Requested: ${incident.specialistRecommended}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "En route to: ${incident.hospitalTargeted}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmergencyRed,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = incident.urgencyLevel,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
