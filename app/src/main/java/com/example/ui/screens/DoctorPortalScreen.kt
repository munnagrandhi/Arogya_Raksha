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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DoctorAvailability
import com.example.data.model.DoctorEntity
import com.example.data.model.DoctorType
import com.example.data.model.EmergencyIncidentEntity
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyGreen
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.TirangaGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorPortalScreen(
    doctors: List<DoctorEntity>,
    activeDoctorId: Long?,
    emergencyIncidents: List<EmergencyIncidentEntity>,
    onSelectDoctor: (Long) -> Unit,
    onSaveDoctor: (DoctorEntity) -> Unit,
    onUpdateAvailability: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentDoctor = doctors.find { it.id == activeDoctorId } ?: doctors.firstOrNull()

    var isEditing by remember { mutableStateOf(false) }
    var isRegisteringNew by remember { mutableStateOf(false) }

    // Form fields state
    var formName by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "" else currentDoctor?.name ?: "")
    }
    var formQualifications by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "" else currentDoctor?.qualifications ?: "")
    }
    var formNmcReg by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "" else currentDoctor?.nmcRegNumber ?: "")
    }
    var formSpecialty by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "Cardiologist" else currentDoctor?.specialty ?: "Cardiologist")
    }
    var formDoctorType by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "DUAL_PRACTICE" else currentDoctor?.doctorType ?: "DUAL_PRACTICE")
    }
    var formPhone by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "+91 " else currentDoctor?.phone ?: "")
    }
    var formHospitalName by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "" else currentDoctor?.hospitalName ?: "")
    }
    var formHospitalTimings by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "Mon - Sat: 08:30 AM - 01:30 PM" else currentDoctor?.hospitalTimings ?: "")
    }
    var formHasClinic by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) true else currentDoctor?.hasPrivateClinic ?: false)
    }
    var formClinicName by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "" else currentDoctor?.clinicName ?: "")
    }
    var formClinicAddress by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "" else currentDoctor?.clinicAddress ?: "")
    }
    var formClinicTimings by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "Mon - Sat: 05:30 PM - 08:30 PM" else currentDoctor?.clinicTimings ?: "")
    }
    var formClinicFee by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "500" else (currentDoctor?.clinicConsultFee ?: 500).toString())
    }
    var formClinicPhone by remember(currentDoctor, isRegisteringNew) {
        mutableStateOf(if (isRegisteringNew) "+91 " else currentDoctor?.clinicPhone ?: "")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("doctor_portal_screen"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        doctors.take(4).forEach { doc ->
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

        // Active Emergency On-Duty Status Switcher (For currently logged in doctor)
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

        // EDIT / REGISTER PROFILE FORM
        if (isEditing || isRegisteringNew) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (isRegisteringNew) "Register New Doctor & Clinic" else "Edit Practice Details",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Mandatory Indian Medical registration & practice details",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = formName,
                            onValueChange = { formName = it },
                            label = { Text("Doctor Full Name (e.g. Dr. Rajesh Verma)") },
                            modifier = Modifier.fillMaxWidth().testTag("doctor_form_name"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = formQualifications,
                            onValueChange = { formQualifications = it },
                            label = { Text("Qualifications (e.g. MBBS, MD, DM)") },
                            modifier = Modifier.fillMaxWidth().testTag("doctor_form_qualifications"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = formNmcReg,
                            onValueChange = { formNmcReg = it },
                            label = { Text("NMC / State Medical Council Reg No (e.g. NMC-2015-8491)") },
                            modifier = Modifier.fillMaxWidth().testTag("doctor_form_nmc"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = formSpecialty,
                            onValueChange = { formSpecialty = it },
                            label = { Text("Specialty (Cardiologist, Neurologist, Trauma Surgeon, etc.)") },
                            modifier = Modifier.fillMaxWidth().testTag("doctor_form_specialty"),
                            singleLine = true
                        )

                        // Doctor Type selector
                        Text("Practice Category:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "DUAL_PRACTICE" to "Dual (Hospital + Clinic)",
                                "GOVT_DOCTOR" to "Govt Doctor Only",
                                "PRIVATE_DOCTOR" to "Private Doctor Only"
                            ).forEach { (typeKey, label) ->
                                FilterChip(
                                    selected = formDoctorType == typeKey,
                                    onClick = {
                                        formDoctorType = typeKey
                                        if (typeKey == "DUAL_PRACTICE") formHasClinic = true
                                        if (typeKey == "GOVT_DOCTOR") formHasClinic = false
                                    },
                                    label = { Text(label, fontSize = 10.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = formPhone,
                            onValueChange = { formPhone = it },
                            label = { Text("Emergency Contact Phone") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth().testTag("doctor_form_phone"),
                            singleLine = true
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        // Hospital Affiliation
                        Text("🏥 Hospital Details:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F3E6D))
                        OutlinedTextField(
                            value = formHospitalName,
                            onValueChange = { formHospitalName = it },
                            label = { Text("Hospital Name (e.g. District Civil Hospital / AIIMS)") },
                            modifier = Modifier.fillMaxWidth().testTag("doctor_form_hospital"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = formHospitalTimings,
                            onValueChange = { formHospitalTimings = it },
                            label = { Text("Hospital OPD Duty Hours (e.g. Mon-Sat 08:30 AM - 01:30 PM)") },
                            modifier = Modifier.fillMaxWidth().testTag("doctor_form_hospital_timings"),
                            singleLine = true
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        // Private Clinic Toggle & Details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("🩺 Running a Private Clinic?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                                Text("Provide evening clinic hours for citizen convenience", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = formHasClinic,
                                onCheckedChange = { formHasClinic = it },
                                modifier = Modifier.testTag("doctor_form_clinic_toggle")
                            )
                        }

                        if (formHasClinic) {
                            OutlinedTextField(
                                value = formClinicName,
                                onValueChange = { formClinicName = it },
                                label = { Text("Clinic Name (e.g. Rao Heart Clinic)") },
                                modifier = Modifier.fillMaxWidth().testTag("doctor_form_clinic_name"),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = formClinicAddress,
                                onValueChange = { formClinicAddress = it },
                                label = { Text("Clinic Address (e.g. Shop 14, Main Market, Sec-14)") },
                                modifier = Modifier.fillMaxWidth().testTag("doctor_form_clinic_address"),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = formClinicTimings,
                                onValueChange = { formClinicTimings = it },
                                label = { Text("Clinic Evening Hours (e.g. Mon-Sat 05:30 PM - 08:30 PM)") },
                                modifier = Modifier.fillMaxWidth().testTag("doctor_form_clinic_timings"),
                                singleLine = true
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = formClinicFee,
                                    onValueChange = { formClinicFee = it },
                                    label = { Text("Consult Fee (₹ INR)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f).testTag("doctor_form_clinic_fee"),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = formClinicPhone,
                                    onValueChange = { formClinicPhone = it },
                                    label = { Text("Clinic Phone") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.weight(1f).testTag("doctor_form_clinic_phone"),
                                    singleLine = true
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                if (formName.isBlank() || formHospitalName.isBlank()) {
                                    Toast.makeText(context, "Please fill doctor name and hospital name", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val docId = if (isRegisteringNew) 0L else (currentDoctor?.id ?: 0L)
                                val updatedDoctor = DoctorEntity(
                                    id = docId,
                                    name = formName,
                                    qualifications = formQualifications.ifBlank { "MBBS" },
                                    nmcRegNumber = formNmcReg.ifBlank { "NMC-VERIFIED" },
                                    specialty = formSpecialty,
                                    doctorType = formDoctorType,
                                    phone = formPhone,
                                    experienceYears = currentDoctor?.experienceYears ?: 10,
                                    hospitalName = formHospitalName,
                                    hospitalDesignation = currentDoctor?.hospitalDesignation ?: "Consultant Specialist",
                                    hospitalTimings = formHospitalTimings,
                                    hasPrivateClinic = formHasClinic,
                                    clinicName = formClinicName,
                                    clinicAddress = formClinicAddress,
                                    clinicTimings = formClinicTimings,
                                    clinicConsultFee = formClinicFee.toIntOrNull() ?: 500,
                                    clinicPhone = formClinicPhone,
                                    availabilityStatus = currentDoctor?.availabilityStatus ?: "AVAILABLE_EMERGENCY",
                                    isVerifiedByGovt = true,
                                    rating = currentDoctor?.rating ?: 4.9,
                                    totalConsults = currentDoctor?.totalConsults ?: 100
                                )
                                onSaveDoctor(updatedDoctor)
                                isEditing = false
                                isRegisteringNew = false
                                Toast.makeText(context, "Doctor profile saved successfully!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_doctor_btn"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isRegisteringNew) "Register Doctor" else "Save Changes", fontWeight = FontWeight.Bold)
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
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(EmergencyRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("108", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = incident.emergencyType,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF991B1B)
                            )
                            Text(
                                text = "Target: ${incident.hospitalTargeted} • Specialist: ${incident.specialistRecommended}",
                                fontSize = 11.sp,
                                color = Color(0xFF7F1D1D)
                            )
                        }
                    }
                }
            }
        }
    }
}
