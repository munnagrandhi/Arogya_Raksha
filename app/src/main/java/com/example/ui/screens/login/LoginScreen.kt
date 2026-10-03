package com.example.ui.screens.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppRole
import com.example.data.model.DoctorEntity
import com.example.data.model.UserEntity
import com.example.ui.components.dialNumber
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyGreen
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.TirangaGreen
import com.example.ui.theme.TirangaSaffron

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    doctors: List<DoctorEntity>,
    onRegisterUserWithRole: (UserEntity) -> Unit,
    onCitizenQuickLogin: (name: String, phone: String, bloodGroup: String) -> Unit,
    onDoctorQuickLogin: (doctorId: Long, doctorName: String, badge: String) -> Unit,
    onGovtQuickLogin: (department: String, officerId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isRegisterMode by remember { mutableStateOf(true) } // Highlight registration flow with role selection by default!
    var selectedAppRole by remember { mutableStateOf(AppRole.GENERAL_PUBLIC) }

    // Registration Form State
    var regName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    
    // General Public specific
    var regBloodGroup by remember { mutableStateOf("O+") }
    var regEmergencyContact by remember { mutableStateOf("") }
    var expandedBloodDropdown by remember { mutableStateOf(false) }

    // Doctor specific
    var regNmcNumber by remember { mutableStateOf("") }
    var regQualifications by remember { mutableStateOf("MBBS, MD") }
    var regSpecialty by remember { mutableStateOf("Cardiologist") }
    var regHospitalName by remember { mutableStateOf("District Civil Hospital") }
    var regHospitalTimings by remember { mutableStateOf("Mon - Sat: 08:30 AM - 01:30 PM") }
    var regHasClinic by remember { mutableStateOf(true) }
    var regClinicName by remember { mutableStateOf("Aarogya Heart & Rhythm Clinic") }
    var regClinicAddress by remember { mutableStateOf("Shop 14, Main Market, Sector 14") }
    var regClinicTimings by remember { mutableStateOf("Mon - Sat: 05:30 PM - 08:30 PM") }
    var regClinicFee by remember { mutableStateOf("600") }
    var regClinicPhone by remember { mutableStateOf("+91 98112 00000") }

    // Government Official specific
    var regGovtDept by remember { mutableStateOf("Chief Medical Officer (CMO) - District Health") }
    var regGovtBadgeId by remember { mutableStateOf("CMO-NCR-108") }
    var expandedDeptDropdown by remember { mutableStateOf(false) }

    val bloodGroups = listOf("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")
    val govtDepartments = listOf(
        "Chief Medical Officer (CMO) - District Health",
        "Traffic Police Emergency Green Corridor Cell",
        "108 Emergency Medical Response (NHM)",
        "Directorate of Health Services (DHS)"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("login_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // App Header Banner with Indian National Health Network branding
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF09233D)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(EmergencyRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Emergency,
                            contentDescription = "Aarogya Raksha",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AAROGYA RAKSHA",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Box(modifier = Modifier.size(4.dp, 14.dp).background(TirangaSaffron, RoundedCornerShape(1.dp)))
                            Box(modifier = Modifier.size(4.dp, 14.dp).background(Color.White, RoundedCornerShape(1.dp)))
                            Box(modifier = Modifier.size(4.dp, 14.dp).background(TirangaGreen, RoundedCornerShape(1.dp)))
                        }
                    }

                    Text(
                        text = "National Emergency Health & Doctor Directory Portal",
                        fontSize = 12.sp,
                        color = Color(0xFF93C5FD)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Instant 108 Emergency Dial & Google Sign-In helper bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuickDialPill("108", "Ambulance", EmergencyRed) { dialNumber(context, "108") }
                        Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color.White.copy(alpha = 0.3f)))
                        QuickDialPill("112", "National Help", Color(0xFF38BDF8)) { dialNumber(context, "112") }
                        Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color.White.copy(alpha = 0.3f)))
                        QuickDialPill("102", "Maternity", Color(0xFFFBBF24)) { dialNumber(context, "102") }
                    }
                }
            }
        }

        // Mode Switcher: "Register New User (Choose Role)" vs "Quick Sign In"
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isRegisterMode) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { isRegisterMode = true }
                            .testTag("tab_register_role_mode")
                    ) {
                        Text(
                            text = "Register with Role",
                            fontSize = 13.sp,
                            fontWeight = if (isRegisterMode) FontWeight.Bold else FontWeight.Normal,
                            color = if (isRegisterMode) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (!isRegisterMode) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { isRegisterMode = false }
                            .testTag("tab_quick_login_mode")
                    ) {
                        Text(
                            text = "Quick Portal Login",
                            fontSize = 13.sp,
                            fontWeight = if (!isRegisterMode) FontWeight.Bold else FontWeight.Normal,
                            color = if (!isRegisterMode) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // ==========================================
        // REGISTRATION MODE WITH ROLE SELECTION UI
        // ==========================================
        if (isRegisterMode) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("registration_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Select Your Role to Register",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Differentiate between general public, private doctors, government doctors, and government officials. Your role will be securely stored in the database.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // 4-ROLE SELECTION CARDS
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            RoleOptionCard(
                                role = AppRole.GENERAL_PUBLIC,
                                icon = Icons.Default.People,
                                iconColor = Color(0xFF2563EB),
                                isSelected = selectedAppRole == AppRole.GENERAL_PUBLIC,
                                onSelect = { selectedAppRole = AppRole.GENERAL_PUBLIC },
                                tag = "role_option_general_public"
                            )

                            RoleOptionCard(
                                role = AppRole.PRIVATE_DOCTOR,
                                icon = Icons.Default.MedicalServices,
                                iconColor = Color(0xFF7C3AED),
                                isSelected = selectedAppRole == AppRole.PRIVATE_DOCTOR,
                                onSelect = { selectedAppRole = AppRole.PRIVATE_DOCTOR },
                                tag = "role_option_private_doctor"
                            )

                            RoleOptionCard(
                                role = AppRole.GOVERNMENT_DOCTOR,
                                icon = Icons.Default.LocalHospital,
                                iconColor = TirangaGreen,
                                isSelected = selectedAppRole == AppRole.GOVERNMENT_DOCTOR,
                                onSelect = { selectedAppRole = AppRole.GOVERNMENT_DOCTOR },
                                tag = "role_option_govt_doctor"
                            )

                            RoleOptionCard(
                                role = AppRole.GOVERNMENT_OFFICIAL,
                                icon = Icons.Default.Security,
                                iconColor = Color(0xFFDC2626),
                                isSelected = selectedAppRole == AppRole.GOVERNMENT_OFFICIAL,
                                onSelect = { selectedAppRole = AppRole.GOVERNMENT_OFFICIAL },
                                tag = "role_option_govt_official"
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        // ROLE-SPECIFIC FORM FIELDS
                        Text(
                            text = "Registration Details for ${selectedAppRole.title}:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Common Basic Fields
                        OutlinedTextField(
                            value = regName,
                            onValueChange = { regName = it },
                            label = { Text(if (selectedAppRole == AppRole.PRIVATE_DOCTOR || selectedAppRole == AppRole.GOVERNMENT_DOCTOR) "Doctor Full Name (e.g. Dr. Rajesh Verma)" else "Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                            modifier = Modifier.fillMaxWidth().testTag("reg_input_name"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = regEmail,
                            onValueChange = { regEmail = it },
                            label = { Text("Email Address") },
                            modifier = Modifier.fillMaxWidth().testTag("reg_input_email"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = regPhone,
                            onValueChange = { regPhone = it },
                            label = { Text("Mobile Contact (+91)") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth().testTag("reg_input_phone"),
                            singleLine = true
                        )

                        // 1. GENERAL PUBLIC FIELDS
                        if (selectedAppRole == AppRole.GENERAL_PUBLIC) {
                            ExposedDropdownMenuBox(
                                expanded = expandedBloodDropdown,
                                onExpandedChange = { expandedBloodDropdown = !expandedBloodDropdown },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = "Blood Group: $regBloodGroup",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Emergency Blood Group") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBloodDropdown) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor().testTag("reg_input_blood_group")
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedBloodDropdown,
                                    onDismissRequest = { expandedBloodDropdown = false }
                                ) {
                                    bloodGroups.forEach { bg ->
                                        DropdownMenuItem(
                                            text = { Text(bg) },
                                            onClick = {
                                                regBloodGroup = bg
                                                expandedBloodDropdown = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = regEmergencyContact,
                                onValueChange = { regEmergencyContact = it },
                                label = { Text("Emergency Kin / Contact Phone") },
                                leadingIcon = { Icon(Icons.Default.Call, contentDescription = "Emergency Kin") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth().testTag("reg_input_emergency_contact"),
                                singleLine = true
                            )
                        }

                        // 2. DOCTOR FIELDS (GOVT DOCTOR & PRIVATE DOCTOR)
                        if (selectedAppRole == AppRole.PRIVATE_DOCTOR || selectedAppRole == AppRole.GOVERNMENT_DOCTOR) {
                            OutlinedTextField(
                                value = regNmcNumber,
                                onValueChange = { regNmcNumber = it },
                                label = { Text("NMC / State Medical Council Reg No. (e.g. NMC-2015-8491)") },
                                leadingIcon = { Icon(Icons.Default.Verified, contentDescription = "NMC Reg") },
                                modifier = Modifier.fillMaxWidth().testTag("reg_input_nmc"),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = regQualifications,
                                onValueChange = { regQualifications = it },
                                label = { Text("Qualifications (MBBS, MD, MS, DM, MCh, DNB)") },
                                modifier = Modifier.fillMaxWidth().testTag("reg_input_qualifications"),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = regSpecialty,
                                onValueChange = { regSpecialty = it },
                                label = { Text("Specialty (Cardiologist, Neurologist, Trauma, etc.)") },
                                modifier = Modifier.fillMaxWidth().testTag("reg_input_specialty"),
                                singleLine = true
                            )

                            // Hospital posting
                            Text(
                                text = if (selectedAppRole == AppRole.GOVERNMENT_DOCTOR) "🏥 Government Hospital Duty:" else "🏥 Associated Hospital / Medical Centre:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F3E6D)
                            )

                            OutlinedTextField(
                                value = regHospitalName,
                                onValueChange = { regHospitalName = it },
                                label = { Text("Hospital Name (e.g. AIIMS / District Civil Hospital)") },
                                modifier = Modifier.fillMaxWidth().testTag("reg_input_hospital_name"),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = regHospitalTimings,
                                onValueChange = { regHospitalTimings = it },
                                label = { Text("Hospital Duty OPD Hours") },
                                modifier = Modifier.fillMaxWidth().testTag("reg_input_hospital_timings"),
                                singleLine = true
                            )

                            // Private Clinic Section (Even govt doctors can run evening clinics in accordance with state guidelines)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🩺 Private Evening Clinic Hours?",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                    Text(
                                        text = "Showcase dual-practice convenience to patients",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = regHasClinic,
                                    onCheckedChange = { regHasClinic = it },
                                    modifier = Modifier.testTag("reg_switch_private_clinic")
                                )
                            }

                            if (regHasClinic) {
                                OutlinedTextField(
                                    value = regClinicName,
                                    onValueChange = { regClinicName = it },
                                    label = { Text("Private Clinic Name") },
                                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = "Clinic") },
                                    modifier = Modifier.fillMaxWidth().testTag("reg_input_clinic_name"),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = regClinicAddress,
                                    onValueChange = { regClinicAddress = it },
                                    label = { Text("Clinic Address & Landmark") },
                                    modifier = Modifier.fillMaxWidth().testTag("reg_input_clinic_address"),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = regClinicTimings,
                                    onValueChange = { regClinicTimings = it },
                                    label = { Text("Evening Clinic Hours (e.g. 05:30 PM - 08:30 PM)") },
                                    modifier = Modifier.fillMaxWidth().testTag("reg_input_clinic_timings"),
                                    singleLine = true
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = regClinicFee,
                                        onValueChange = { regClinicFee = it },
                                        label = { Text("Fee (₹ INR)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f).testTag("reg_input_clinic_fee"),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = regClinicPhone,
                                        onValueChange = { regClinicPhone = it },
                                        label = { Text("Clinic Phone") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                        modifier = Modifier.weight(1f).testTag("reg_input_clinic_phone"),
                                        singleLine = true
                                    )
                                }
                            }
                        }

                        // 3. GOVERNMENT OFFICIAL FIELDS
                        if (selectedAppRole == AppRole.GOVERNMENT_OFFICIAL) {
                            ExposedDropdownMenuBox(
                                expanded = expandedDeptDropdown,
                                onExpandedChange = { expandedDeptDropdown = !expandedDeptDropdown },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = regGovtDept,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Government Department") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDeptDropdown) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor().testTag("reg_input_govt_dept")
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedDeptDropdown,
                                    onDismissRequest = { expandedDeptDropdown = false }
                                ) {
                                    govtDepartments.forEach { dept ->
                                        DropdownMenuItem(
                                            text = { Text(dept, fontSize = 12.sp) },
                                            onClick = {
                                                regGovtDept = dept
                                                expandedDeptDropdown = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = regGovtBadgeId,
                                onValueChange = { regGovtBadgeId = it },
                                label = { Text("Official Govt Badge / Employee ID") },
                                leadingIcon = { Icon(Icons.Default.Shield, contentDescription = "Badge") },
                                modifier = Modifier.fillMaxWidth().testTag("reg_input_govt_badge"),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // SUBMIT REGISTRATION BUTTON
                        Button(
                            onClick = {
                                if (regName.isBlank()) {
                                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val userId = "usr_${System.currentTimeMillis()}"
                                val userEntity = UserEntity(
                                    userId = userId,
                                    displayName = if ((selectedAppRole == AppRole.PRIVATE_DOCTOR || selectedAppRole == AppRole.GOVERNMENT_DOCTOR) && !regName.startsWith("Dr.")) "Dr. $regName" else regName,
                                    email = regEmail,
                                    role = selectedAppRole.code,
                                    phone = regPhone.ifBlank { "+91 98000 00000" },
                                    bloodGroup = regBloodGroup,
                                    emergencyContact = regEmergencyContact,
                                    nmcRegNumber = regNmcNumber.ifBlank { "NMC-${System.currentTimeMillis() % 100000}" },
                                    qualifications = regQualifications,
                                    specialty = regSpecialty,
                                    hospitalName = regHospitalName,
                                    hospitalTimings = regHospitalTimings,
                                    hasPrivateClinic = regHasClinic,
                                    clinicName = regClinicName,
                                    clinicAddress = regClinicAddress,
                                    clinicTimings = regClinicTimings,
                                    clinicConsultFee = regClinicFee.toIntOrNull() ?: 500,
                                    clinicPhone = regClinicPhone,
                                    govtDepartment = regGovtDept,
                                    govtBadgeId = regGovtBadgeId
                                )
                                onRegisterUserWithRole(userEntity)
                                Toast.makeText(context, "Registered successfully as ${selectedAppRole.title}!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_complete_registration"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Register")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Register as ${selectedAppRole.title}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            // ==========================================
            // QUICK SIGN-IN MODE (FOR EXISTING PORTALS)
            // ==========================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Quick Sign In to Any Portal",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // 1. Citizen Portal Quick Sign In
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFEFF6FF),
                            border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onCitizenQuickLogin("Ramesh Kumar", "+91 98101 23456", "O+")
                                }
                                .testTag("quick_login_citizen")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.People, contentDescription = "Citizen", tint = Color(0xFF1D4ED8))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("General Citizen Portal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                                    Text("Emergency triage, doctor finder & hospital routes", fontSize = 11.sp, color = Color(0xFF1E40AF))
                                }
                                Button(
                                    onClick = { onCitizenQuickLogin("Ramesh Kumar", "+91 98101 23456", "O+") },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Enter", fontSize = 11.sp)
                                }
                            }
                        }

                        // 2. Doctor Portal Quick Sign In (Dual Practice)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFEF3C7),
                            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    val doc = doctors.find { it.doctorType == "DUAL_PRACTICE" } ?: doctors.firstOrNull()
                                    if (doc != null) {
                                        onDoctorQuickLogin(doc.id, doc.name, "${doc.hospitalName} & ${doc.clinicName}")
                                    }
                                }
                                .testTag("quick_login_doctor")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.MedicalServices, contentDescription = "Doctor", tint = Color(0xFFB45309))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Doctor Portal (Dual Practice)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                                    Text("Dr. Arvind Rao • Civil Hospital & Heart Clinic", fontSize = 11.sp, color = Color(0xFF92400E))
                                }
                                Button(
                                    onClick = {
                                        val doc = doctors.find { it.doctorType == "DUAL_PRACTICE" } ?: doctors.firstOrNull()
                                        if (doc != null) {
                                            onDoctorQuickLogin(doc.id, doc.name, "${doc.hospitalName} & ${doc.clinicName}")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Enter", fontSize = 11.sp)
                                }
                            }
                        }

                        // 3. Govt Official Quick Sign In
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFEF2F2),
                            border = BorderStroke(1.dp, Color(0xFFFECACA)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onGovtQuickLogin("District CMO Office", "CMO-DL-108")
                                }
                                .testTag("quick_login_govt")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Security, contentDescription = "Govt", tint = Color(0xFFDC2626))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Government & Traffic Officer Portal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF991B1B))
                                    Text("Green Corridor activation & live bed monitoring", fontSize = 11.sp, color = Color(0xFF7F1D1D))
                                }
                                Button(
                                    onClick = { onGovtQuickLogin("District CMO Office", "CMO-DL-108") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Enter", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoleOptionCard(
    role: AppRole,
    icon: ImageVector,
    iconColor: Color,
    isSelected: Boolean,
    onSelect: () -> Unit,
    tag: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) iconColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .testTag(tag),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) iconColor.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = role.title, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = role.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = role.subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun QuickDialPill(number: String, label: String, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Call, contentDescription = "Call $number", tint = color, modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(text = number, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = label, fontSize = 9.sp, color = Color(0xFFCBD5E1))
        }
    }
}
