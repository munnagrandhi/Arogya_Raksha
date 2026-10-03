package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.ClinicAffiliationData
import com.example.data.model.CompleteDoctorProfile
import com.example.data.model.DoctorCredentialsData
import com.example.data.model.DoctorSpecializationData
import com.example.data.model.HospitalAffiliationData
import com.example.ui.theme.EmergencyGreen
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.TirangaGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorCredentialsInputForm(
    initialProfile: CompleteDoctorProfile? = null,
    onSaveProfile: (CompleteDoctorProfile) -> Unit,
    onCancel: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentStep by remember { mutableIntStateOf(0) } // 0: Credentials, 1: Specialization, 2: Hospital, 3: Clinic

    // Basic Identification
    var fullName by remember { mutableStateOf(initialProfile?.fullName ?: "") }
    var email by remember { mutableStateOf(initialProfile?.email ?: "") }
    var phone by remember { mutableStateOf(initialProfile?.phone ?: "") }
    var doctorRole by remember { mutableStateOf(initialProfile?.role ?: "GOVERNMENT_DOCTOR") }

    // 1. Medical Credentials
    var medicalCouncil by remember {
        mutableStateOf(initialProfile?.credentials?.medicalCouncil ?: "National Medical Commission (NMC)")
    }
    var regNumber by remember { mutableStateOf(initialProfile?.credentials?.registrationNumber ?: "") }
    var regYear by remember { mutableStateOf(initialProfile?.credentials?.registrationYear?.toString() ?: "2016") }
    var primaryDegree by remember { mutableStateOf(initialProfile?.credentials?.primaryDegree ?: "MBBS") }
    var postGradDegrees by remember {
        mutableStateOf(initialProfile?.credentials?.postGraduateDegrees ?: "MD (Medicine), DM (Cardiology)")
    }
    var additionalCertifications by remember {
        mutableStateOf(initialProfile?.credentials?.additionalCertifications ?: "Fellowship in Interventional Cardiology")
    }
    var experienceYears by remember {
        mutableStateOf(initialProfile?.credentials?.yearsOfExperience?.toString() ?: "10")
    }

    // 2. Area of Specialization
    var primarySpecialty by remember {
        mutableStateOf(initialProfile?.specialization?.primarySpecialty ?: "Cardiology")
    }
    var subSpecialties by remember {
        mutableStateOf(initialProfile?.specialization?.subSpecialties ?: "Primary Angioplasty, Cardiac ICU, Heart Failure")
    }
    var clinicalInterests by remember {
        mutableStateOf(initialProfile?.specialization?.clinicalInterests ?: "Acute STEMI Golden Hour Treatment")
    }
    var languagesSpoken by remember {
        mutableStateOf(initialProfile?.specialization?.languagesSpoken ?: "Hindi, English")
    }

    // 3. Hospital Affiliations
    var hospitalName by remember {
        mutableStateOf(initialProfile?.hospitalAffiliation?.hospitalName ?: "All India Institute of Medical Sciences (AIIMS)")
    }
    var hospitalType by remember {
        mutableStateOf(initialProfile?.hospitalAffiliation?.hospitalType ?: "Government Apex Hospital")
    }
    var designation by remember {
        mutableStateOf(initialProfile?.hospitalAffiliation?.designation ?: "Senior Consultant & Cath Lab In-Charge")
    }
    var department by remember {
        mutableStateOf(initialProfile?.hospitalAffiliation?.department ?: "Department of Cardiology & Emergency Care")
    }
    var opdTimings by remember {
        mutableStateOf(initialProfile?.hospitalAffiliation?.opdTimings ?: "Mon - Sat: 08:30 AM - 01:30 PM")
    }
    var hospitalCity by remember {
        mutableStateOf(initialProfile?.hospitalAffiliation?.hospitalCity ?: "Delhi NCR")
    }
    var emergencyDutyAvailable by remember {
        mutableStateOf(initialProfile?.hospitalAffiliation?.emergencyDutyAvailable ?: true)
    }

    // 4. Private Clinic Affiliations (Dual-Practice)
    var runsPrivateClinic by remember {
        mutableStateOf(initialProfile?.clinicAffiliation?.runsPrivateClinic ?: true)
    }
    var clinicName by remember {
        mutableStateOf(initialProfile?.clinicAffiliation?.clinicName ?: "Aarogya Heart & Rhythm Clinic")
    }
    var clinicAddress by remember {
        mutableStateOf(initialProfile?.clinicAffiliation?.clinicAddress ?: "Plot 24, Near Metro Pillar 140, Sector 14")
    }
    var clinicCity by remember {
        mutableStateOf(initialProfile?.clinicAffiliation?.clinicCity ?: "Delhi NCR")
    }
    var eveningHours by remember {
        mutableStateOf(initialProfile?.clinicAffiliation?.eveningHours ?: "Mon - Sat: 05:30 PM - 08:30 PM")
    }
    var consultationFee by remember {
        mutableStateOf(initialProfile?.clinicAffiliation?.consultationFeeInr?.toString() ?: "600")
    }
    var clinicPhone by remember {
        mutableStateOf(initialProfile?.clinicAffiliation?.clinicContactPhone ?: "+91 98112 00000")
    }
    var appointmentBookingAvailable by remember {
        mutableStateOf(initialProfile?.clinicAffiliation?.appointmentBookingAvailable ?: true)
    }

    // Dropdown states
    var expandedCouncilDropdown by remember { mutableStateOf(false) }
    var expandedSpecialtyDropdown by remember { mutableStateOf(false) }
    var expandedHospitalTypeDropdown by remember { mutableStateOf(false) }

    val councils = listOf(
        "National Medical Commission (NMC)",
        "Delhi Medical Council (DMC)",
        "Maharashtra Medical Council",
        "Karnataka Medical Council",
        "Tamil Nadu Medical Council",
        "West Bengal Medical Council",
        "Uttar Pradesh Medical Council",
        "State Medical Council (Other)"
    )

    val specialties = listOf(
        "Cardiology",
        "Neurology & Neurosurgery",
        "Trauma & Orthopedic Surgery",
        "Pulmonology & Respiratory Medicine",
        "Emergency Medicine & Toxicology",
        "Obstetrics & Gynecology",
        "Pediatrics & Neonatal Care",
        "Plastic & Burn Surgery",
        "General & Laparoscopic Surgery",
        "Critical Care Medicine"
    )

    val hospitalTypes = listOf(
        "Government Apex Hospital",
        "District Civil Hospital",
        "Community Health Centre (CHC)",
        "Private Multispeciality Hospital",
        "Charitable Trust Hospital"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("doctor_credentials_form_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
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
                            .background(TirangaGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalInformation,
                            contentDescription = "Medical Credentials",
                            tint = TirangaGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Doctor Medical Profile & Affiliations",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Credentials, Hospital & Clinic Affiliations, Specialization",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Step Indicator Tabs
            TabRow(
                selectedTabIndex = currentStep,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                Tab(
                    selected = currentStep == 0,
                    onClick = { currentStep = 0 },
                    text = { Text("1. Credentials", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("form_tab_credentials")
                )
                Tab(
                    selected = currentStep == 1,
                    onClick = { currentStep = 1 },
                    text = { Text("2. Specialty", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("form_tab_specialty")
                )
                Tab(
                    selected = currentStep == 2,
                    onClick = { currentStep = 2 },
                    text = { Text("3. Hospital", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("form_tab_hospital")
                )
                Tab(
                    selected = currentStep == 3,
                    onClick = { currentStep = 3 },
                    text = { Text("4. Clinic", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("form_tab_clinic")
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // -------------------------------------------------------------
            // STEP 0: MEDICAL CREDENTIALS & IDENTIFICATION
            // -------------------------------------------------------------
            if (currentStep == 0) {
                Text(
                    text = "Medical Qualifications & Registration (NMC Compliance):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Role selection (Govt Doctor vs Private Doctor)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = doctorRole == "GOVERNMENT_DOCTOR",
                        onClick = { doctorRole = "GOVERNMENT_DOCTOR" },
                        label = { Text("Government Doctor", fontSize = 11.sp) },
                        leadingIcon = {
                            if (doctorRole == "GOVERNMENT_DOCTOR") {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        },
                        modifier = Modifier.weight(1f).testTag("chip_role_govt_doc")
                    )
                    FilterChip(
                        selected = doctorRole == "PRIVATE_DOCTOR",
                        onClick = { doctorRole = "PRIVATE_DOCTOR" },
                        label = { Text("Private Doctor", fontSize = 11.sp) },
                        leadingIcon = {
                            if (doctorRole == "PRIVATE_DOCTOR") {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        },
                        modifier = Modifier.weight(1f).testTag("chip_role_private_doc")
                    )
                }

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Doctor Full Name (e.g. Dr. Ramesh Gupta)") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Doctor Name") },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_name"),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Mobile Contact (+91)") },
                        leadingIcon = { Icon(Icons.Default.Call, contentDescription = "Phone") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.weight(1f).testTag("input_doc_phone"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Official Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.weight(1f).testTag("input_doc_email"),
                        singleLine = true
                    )
                }

                // Medical Council Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedCouncilDropdown,
                    onExpandedChange = { expandedCouncilDropdown = !expandedCouncilDropdown },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = medicalCouncil,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Medical Regulatory Council") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCouncilDropdown) },
                        leadingIcon = { Icon(Icons.Default.Verified, contentDescription = "Council") },
                        modifier = Modifier.fillMaxWidth().menuAnchor().testTag("input_doc_council")
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCouncilDropdown,
                        onDismissRequest = { expandedCouncilDropdown = false }
                    ) {
                        councils.forEach { council ->
                            DropdownMenuItem(
                                text = { Text(council, fontSize = 12.sp) },
                                onClick = {
                                    medicalCouncil = council
                                    expandedCouncilDropdown = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = regNumber,
                        onValueChange = { regNumber = it },
                        label = { Text("NMC / Council Reg No.") },
                        modifier = Modifier.weight(1.3f).testTag("input_doc_reg_number"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = regYear,
                        onValueChange = { regYear = it },
                        label = { Text("Reg Year") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(0.7f).testTag("input_doc_reg_year"),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = primaryDegree,
                        onValueChange = { primaryDegree = it },
                        label = { Text("Primary Degree (MBBS)") },
                        modifier = Modifier.weight(0.9f).testTag("input_doc_primary_degree"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = experienceYears,
                        onValueChange = { experienceYears = it },
                        label = { Text("Exp (Years)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(0.6f).testTag("input_doc_exp_years"),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = postGradDegrees,
                    onValueChange = { postGradDegrees = it },
                    label = { Text("Post-Graduate Degrees (MD, MS, DM, MCh, DNB)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_postgrad_degrees"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = additionalCertifications,
                    onValueChange = { additionalCertifications = it },
                    label = { Text("Fellowships & Additional Certifications") },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_certifications"),
                    singleLine = true
                )
            }

            // -------------------------------------------------------------
            // STEP 1: AREA OF SPECIALIZATION & CLINICAL FOCUS
            // -------------------------------------------------------------
            if (currentStep == 1) {
                Text(
                    text = "Area of Specialization & Emergency Clinical Focus:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Primary Specialty Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedSpecialtyDropdown,
                    onExpandedChange = { expandedSpecialtyDropdown = !expandedSpecialtyDropdown },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = primarySpecialty,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Primary Area of Specialization") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSpecialtyDropdown) },
                        leadingIcon = { Icon(Icons.Default.MedicalServices, contentDescription = "Specialty") },
                        modifier = Modifier.fillMaxWidth().menuAnchor().testTag("input_doc_specialty")
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSpecialtyDropdown,
                        onDismissRequest = { expandedSpecialtyDropdown = false }
                    ) {
                        specialties.forEach { spec ->
                            DropdownMenuItem(
                                text = { Text(spec, fontSize = 12.sp) },
                                onClick = {
                                    primarySpecialty = spec
                                    expandedSpecialtyDropdown = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = subSpecialties,
                    onValueChange = { subSpecialties = it },
                    label = { Text("Sub-Specialties & Clinical Procedures") },
                    supportingText = { Text("e.g. Primary Angioplasty, Cardiac Catheterization, Pacemaker, Arrhythmia", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_subspecialties")
                )

                OutlinedTextField(
                    value = clinicalInterests,
                    onValueChange = { clinicalInterests = it },
                    label = { Text("Emergency Clinical Interests & Focus") },
                    supportingText = { Text("e.g. Acute STEMI Golden Hour Revascularization, Critical Care Transport", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_clinical_interests")
                )

                OutlinedTextField(
                    value = languagesSpoken,
                    onValueChange = { languagesSpoken = it },
                    label = { Text("Languages Spoken for Patient Communication") },
                    leadingIcon = { Icon(Icons.Default.Language, contentDescription = "Language") },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_languages"),
                    singleLine = true
                )
            }

            // -------------------------------------------------------------
            // STEP 2: HOSPITAL AFFILIATION
            // -------------------------------------------------------------
            if (currentStep == 2) {
                Text(
                    text = "Current Hospital Posting & Department Affiliation:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F3E6D)
                )

                OutlinedTextField(
                    value = hospitalName,
                    onValueChange = { hospitalName = it },
                    label = { Text("Current Hospital Name (e.g. AIIMS / District Civil Hospital)") },
                    leadingIcon = { Icon(Icons.Default.LocalHospital, contentDescription = "Hospital") },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_hospital_name"),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = expandedHospitalTypeDropdown,
                    onExpandedChange = { expandedHospitalTypeDropdown = !expandedHospitalTypeDropdown },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = hospitalType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Hospital Institution Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedHospitalTypeDropdown) },
                        modifier = Modifier.fillMaxWidth().menuAnchor().testTag("input_doc_hospital_type")
                    )
                    ExposedDropdownMenu(
                        expanded = expandedHospitalTypeDropdown,
                        onDismissRequest = { expandedHospitalTypeDropdown = false }
                    ) {
                        hospitalTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type, fontSize = 12.sp) },
                                onClick = {
                                    hospitalType = type
                                    expandedHospitalTypeDropdown = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = designation,
                        onValueChange = { designation = it },
                        label = { Text("Designation") },
                        modifier = Modifier.weight(1f).testTag("input_doc_hospital_designation"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = hospitalCity,
                        onValueChange = { hospitalCity = it },
                        label = { Text("City / Region") },
                        modifier = Modifier.weight(1f).testTag("input_doc_hospital_city"),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text("Hospital Department / Unit") },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_hospital_dept"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = opdTimings,
                    onValueChange = { opdTimings = it },
                    label = { Text("Hospital Daytime OPD Duty Timings") },
                    leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = "OPD Timings") },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_hospital_timings"),
                    singleLine = true
                )

                // Emergency on-call duty toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFEF2F2), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🚨 On-Call for 108 Emergency Triage?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmergencyRed
                        )
                        Text(
                            text = "Receive urgent notifications during Golden Hour trauma / STEMI crises",
                            fontSize = 10.sp,
                            color = Color(0xFF7F1D1D)
                        )
                    }
                    Switch(
                        checked = emergencyDutyAvailable,
                        onCheckedChange = { emergencyDutyAvailable = it },
                        modifier = Modifier.testTag("switch_emergency_on_call")
                    )
                }
            }

            // -------------------------------------------------------------
            // STEP 3: PRIVATE CLINIC AFFILIATION (DUAL PRACTICE)
            // -------------------------------------------------------------
            if (currentStep == 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🩺 Private Clinic Affiliation (Dual-Practice)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                        Text(
                            text = "A doctor can work in a hospital and run a private evening clinic. Show this convenience to citizens.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = runsPrivateClinic,
                        onCheckedChange = { runsPrivateClinic = it },
                        modifier = Modifier.testTag("switch_runs_private_clinic")
                    )
                }

                if (runsPrivateClinic) {
                    OutlinedTextField(
                        value = clinicName,
                        onValueChange = { clinicName = it },
                        label = { Text("Private Clinic Name") },
                        leadingIcon = { Icon(Icons.Default.Business, contentDescription = "Clinic") },
                        modifier = Modifier.fillMaxWidth().testTag("input_doc_clinic_name"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = clinicAddress,
                        onValueChange = { clinicAddress = it },
                        label = { Text("Clinic Physical Address & Landmark") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Address") },
                        modifier = Modifier.fillMaxWidth().testTag("input_doc_clinic_address"),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = clinicCity,
                            onValueChange = { clinicCity = it },
                            label = { Text("Clinic City") },
                            modifier = Modifier.weight(1f).testTag("input_doc_clinic_city"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = eveningHours,
                            onValueChange = { eveningHours = it },
                            label = { Text("Evening OPD Hours") },
                            modifier = Modifier.weight(1.2f).testTag("input_doc_clinic_hours"),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = consultationFee,
                            onValueChange = { consultationFee = it },
                            label = { Text("Fee (₹ INR)") },
                            leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = "Fee") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(0.9f).testTag("input_doc_clinic_fee"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = clinicPhone,
                            onValueChange = { clinicPhone = it },
                            label = { Text("Clinic Booking Phone") },
                            leadingIcon = { Icon(Icons.Default.Call, contentDescription = "Phone") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.weight(1.1f).testTag("input_doc_clinic_phone"),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Enable in-app patient appointment booking for clinic?",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            checked = appointmentBookingAvailable,
                            onCheckedChange = { appointmentBookingAvailable = it },
                            modifier = Modifier.testTag("switch_appointment_booking")
                        )
                    }
                } else {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Doctor practices exclusively at $hospitalName without a separate private clinic.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Navigation Buttons (Back / Next / Save to User Profile)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentStep > 0) {
                    Button(
                        onClick = { currentStep -= 1 },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("btn_form_prev_step")
                    ) {
                        Text("← Back", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                } else {
                    if (onCancel != null) {
                        Button(
                            onClick = onCancel,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.testTag("btn_form_cancel")
                        ) {
                            Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }

                if (currentStep < 3) {
                    Button(
                        onClick = {
                            if (currentStep == 0 && (fullName.isBlank() || regNumber.isBlank())) {
                                Toast.makeText(context, "Please enter your name and NMC registration number", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            currentStep += 1
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.testTag("btn_form_next_step")
                    ) {
                        Text("Next Step →", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            if (fullName.isBlank()) {
                                Toast.makeText(context, "Please enter doctor's full name", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val feeVal = consultationFee.toIntOrNull() ?: 500
                            val expVal = experienceYears.toIntOrNull() ?: 8
                            val regYearVal = regYear.toIntOrNull() ?: 2016

                            val completeProfile = CompleteDoctorProfile(
                                userId = initialProfile?.userId ?: "doc_${System.currentTimeMillis()}",
                                fullName = if (fullName.startsWith("Dr.")) fullName else "Dr. $fullName",
                                email = email,
                                phone = phone.ifBlank { "+91 98000 00000" },
                                role = doctorRole,
                                credentials = DoctorCredentialsData(
                                    medicalCouncil = medicalCouncil,
                                    registrationNumber = regNumber.ifBlank { "NMC-${System.currentTimeMillis() % 100000}" },
                                    registrationYear = regYearVal,
                                    primaryDegree = primaryDegree,
                                    postGraduateDegrees = postGradDegrees,
                                    additionalCertifications = additionalCertifications,
                                    yearsOfExperience = expVal
                                ),
                                hospitalAffiliation = HospitalAffiliationData(
                                    hospitalName = hospitalName.ifBlank { "District Civil Hospital" },
                                    hospitalType = hospitalType,
                                    designation = designation,
                                    department = department,
                                    opdTimings = opdTimings,
                                    hospitalCity = hospitalCity,
                                    emergencyDutyAvailable = emergencyDutyAvailable
                                ),
                                clinicAffiliation = ClinicAffiliationData(
                                    runsPrivateClinic = runsPrivateClinic,
                                    clinicName = clinicName,
                                    clinicAddress = clinicAddress,
                                    clinicCity = clinicCity,
                                    eveningHours = eveningHours,
                                    consultationFeeInr = feeVal,
                                    clinicContactPhone = clinicPhone,
                                    appointmentBookingAvailable = appointmentBookingAvailable
                                ),
                                specialization = DoctorSpecializationData(
                                    primarySpecialty = primarySpecialty,
                                    subSpecialties = subSpecialties,
                                    clinicalInterests = clinicalInterests,
                                    languagesSpoken = languagesSpoken
                                )
                            )
                            onSaveProfile(completeProfile)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TirangaGreen),
                        modifier = Modifier.testTag("btn_save_doctor_profile")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save to Profile & Database", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
