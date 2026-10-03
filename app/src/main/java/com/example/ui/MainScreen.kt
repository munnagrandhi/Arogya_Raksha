package com.example.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserRole
import com.example.ui.components.AarogyaTopAppBar
import com.example.ui.components.dialNumber
import com.example.ui.screens.CitizenTriageScreen
import com.example.ui.screens.DoctorDirectoryScreen
import com.example.ui.screens.DoctorPortalScreen
import com.example.ui.screens.GovtOfficerScreen
import com.example.ui.screens.HospitalNavigationScreen
import com.example.ui.screens.RoleSelectionDialog
import com.example.ui.screens.login.LoginScreen
import com.example.ui.theme.EmergencyRed
import com.example.ui.viewmodel.AarogyaViewModel

@Composable
fun MainScreen(
    viewModel: AarogyaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val doctors by viewModel.filteredDoctors.collectAsStateWithLifecycle()
    val allDoctors by viewModel.allDoctors.collectAsStateWithLifecycle()
    val hospitals by viewModel.filteredHospitals.collectAsStateWithLifecycle()
    val allHospitals by viewModel.allHospitals.collectAsStateWithLifecycle()
    val trafficAlerts by viewModel.trafficAlerts.collectAsStateWithLifecycle()
    val emergencyIncidents by viewModel.emergencyIncidents.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showRoleDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.lastSosSuccessMessage) {
        uiState.lastSosSuccessMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSosMessage()
        }
    }

    // If NOT logged in, show the Login pages!
    if (!uiState.isLoggedIn) {
        LoginScreen(
            doctors = allDoctors,
            onRegisterUserWithRole = { userEntity ->
                viewModel.registerUserWithRole(userEntity)
                selectedTabIndex = 0
            },
            onCitizenQuickLogin = { name, phone, bloodGroup ->
                viewModel.loginAsCitizen(name, phone, bloodGroup)
                selectedTabIndex = 0
            },
            onDoctorQuickLogin = { docId, docName, badge ->
                viewModel.loginAsDoctor(docId, docName, badge)
                selectedTabIndex = 0
            },
            onGovtQuickLogin = { dept, officerId ->
                viewModel.loginAsGovtOfficer(dept, officerId)
                selectedTabIndex = 0
            }
        )
        return
    }

    // If on a sub-tab, BackHandler returns to the primary tab
    BackHandler(enabled = selectedTabIndex != 0) {
        selectedTabIndex = 0
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AarogyaTopAppBar(
                currentRole = uiState.currentRole,
                userName = uiState.loggedInUserName,
                onRoleClick = { showRoleDialog = true },
                onLogoutClick = { viewModel.logout() },
                onSosClick = { dialNumber(context, "108") }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                when (uiState.currentRole) {
                    UserRole.CITIZEN -> {
                        NavigationBarItem(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            icon = { Icon(Icons.Default.Emergency, contentDescription = "Emergency Triage") },
                            label = { Text("Triage SOS", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("nav_triage")
                        )
                        NavigationBarItem(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            icon = { Icon(Icons.Default.MedicalServices, contentDescription = "Doctors") },
                            label = { Text("Doctors & Clinics", fontSize = 11.sp) },
                            modifier = Modifier.testTag("nav_doctors")
                        )
                        NavigationBarItem(
                            selected = selectedTabIndex == 2,
                            onClick = { selectedTabIndex = 2 },
                            icon = { Icon(Icons.Default.LocalHospital, contentDescription = "Hospitals") },
                            label = { Text("Hospitals & Route", fontSize = 11.sp) },
                            modifier = Modifier.testTag("nav_hospitals")
                        )
                    }
                    UserRole.DOCTOR -> {
                        NavigationBarItem(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            icon = { Icon(Icons.Default.MedicalServices, contentDescription = "My Practice") },
                            label = { Text("Doctor Portal", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("nav_doctor_portal")
                        )
                        NavigationBarItem(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            icon = { Icon(Icons.Default.People, contentDescription = "Directory Preview") },
                            label = { Text("Doctor Directory", fontSize = 11.sp) },
                            modifier = Modifier.testTag("nav_doctor_preview")
                        )
                        NavigationBarItem(
                            selected = selectedTabIndex == 2,
                            onClick = { selectedTabIndex = 2 },
                            icon = { Icon(Icons.Default.LocalHospital, contentDescription = "Hospitals") },
                            label = { Text("Hospitals & ICU", fontSize = 11.sp) },
                            modifier = Modifier.testTag("nav_doctor_hospitals")
                        )
                    }
                    UserRole.GOVT_OFFICER -> {
                        NavigationBarItem(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            icon = { Icon(Icons.Default.Security, contentDescription = "Govt Command") },
                            label = { Text("Govt Liaison", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("nav_govt_liaison")
                        )
                        NavigationBarItem(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            icon = { Icon(Icons.Default.LocalHospital, contentDescription = "Hospitals & Green Corridor") },
                            label = { Text("Hospitals & Routes", fontSize = 11.sp) },
                            modifier = Modifier.testTag("nav_govt_hospitals")
                        )
                        NavigationBarItem(
                            selected = selectedTabIndex == 2,
                            onClick = { selectedTabIndex = 2 },
                            icon = { Icon(Icons.Default.Emergency, contentDescription = "Emergency Triage") },
                            label = { Text("Triage Monitor", fontSize = 11.sp) },
                            modifier = Modifier.testTag("nav_govt_triage")
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (uiState.currentRole == UserRole.CITIZEN && selectedTabIndex != 0) {
                FloatingActionButton(
                    onClick = { dialNumber(context, "108") },
                    containerColor = EmergencyRed,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("fab_sos_108")
                ) {
                    Icon(
                        imageVector = Icons.Default.Emergency,
                        contentDescription = "Quick Call 108 Ambulance",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState.currentRole) {
                UserRole.CITIZEN -> {
                    when (selectedTabIndex) {
                        0 -> CitizenTriageScreen(
                            activeAlerts = trafficAlerts,
                            doctors = allDoctors,
                            hospitals = allHospitals,
                            selectedTriage = uiState.selectedTriageItem,
                            onSelectTriage = { viewModel.selectTriageItem(it) },
                            onTriggerSos = { emergencyTitle, urgency, specialist, targetHospital, dialNo ->
                                viewModel.triggerSosDispatch(
                                    emergencyTitle,
                                    urgency,
                                    specialist,
                                    targetHospital,
                                    dialNo
                                )
                            },
                            onNavigateToDoctorTab = { selectedTabIndex = 1 }
                        )
                        1 -> DoctorDirectoryScreen(
                            doctors = doctors,
                            searchQuery = uiState.doctorSearchQuery,
                            onSearchChange = { viewModel.setDoctorSearchQuery(it) },
                            selectedSpecialty = uiState.selectedDoctorSpecialtyFilter,
                            onSpecialtyChange = { viewModel.setDoctorSpecialtyFilter(it) },
                            selectedType = uiState.selectedDoctorTypeFilter,
                            onTypeChange = { viewModel.setDoctorTypeFilter(it) }
                        )
                        2 -> HospitalNavigationScreen(
                            hospitals = hospitals,
                            activeAlerts = trafficAlerts,
                            searchQuery = uiState.hospitalSearchQuery,
                            onSearchChange = { viewModel.setHospitalSearchQuery(it) },
                            selectedFilter = uiState.hospitalTypeFilter,
                            onFilterChange = { viewModel.setHospitalTypeFilter(it) }
                        )
                    }
                }
                UserRole.DOCTOR -> {
                    when (selectedTabIndex) {
                        0 -> DoctorPortalScreen(
                            doctors = allDoctors,
                            activeDoctorId = uiState.activeDoctorId,
                            emergencyIncidents = emergencyIncidents,
                            onSelectDoctor = { viewModel.setActiveDoctorId(it) },
                            onSaveDoctor = { viewModel.registerOrUpdateDoctor(it) },
                            onUpdateAvailability = { docId, status ->
                                viewModel.updateDoctorAvailability(docId, status)
                            }
                        )
                        1 -> DoctorDirectoryScreen(
                            doctors = doctors,
                            searchQuery = uiState.doctorSearchQuery,
                            onSearchChange = { viewModel.setDoctorSearchQuery(it) },
                            selectedSpecialty = uiState.selectedDoctorSpecialtyFilter,
                            onSpecialtyChange = { viewModel.setDoctorSpecialtyFilter(it) },
                            selectedType = uiState.selectedDoctorTypeFilter,
                            onTypeChange = { viewModel.setDoctorTypeFilter(it) }
                        )
                        2 -> HospitalNavigationScreen(
                            hospitals = hospitals,
                            activeAlerts = trafficAlerts,
                            searchQuery = uiState.hospitalSearchQuery,
                            onSearchChange = { viewModel.setHospitalSearchQuery(it) },
                            selectedFilter = uiState.hospitalTypeFilter,
                            onFilterChange = { viewModel.setHospitalTypeFilter(it) }
                        )
                    }
                }
                UserRole.GOVT_OFFICER -> {
                    when (selectedTabIndex) {
                        0 -> GovtOfficerScreen(
                            hospitals = allHospitals,
                            doctors = allDoctors,
                            trafficAlerts = trafficAlerts,
                            onToggleGreenCorridor = { hospId, active, route ->
                                viewModel.toggleGreenCorridor(hospId, active, route)
                            },
                            onUpdateBeds = { hospId, icu, oxy ->
                                viewModel.updateHospitalBeds(hospId, icu, oxy)
                            },
                            onBroadcastAlert = { title, route, type, hosp, officer ->
                                viewModel.postTrafficAlert(title, route, type, hosp, officer)
                            },
                            onVerifyDoctor = { docId, isVerified ->
                                viewModel.verifyDoctorNmc(docId, isVerified)
                            }
                        )
                        1 -> HospitalNavigationScreen(
                            hospitals = hospitals,
                            activeAlerts = trafficAlerts,
                            searchQuery = uiState.hospitalSearchQuery,
                            onSearchChange = { viewModel.setHospitalSearchQuery(it) },
                            selectedFilter = uiState.hospitalTypeFilter,
                            onFilterChange = { viewModel.setHospitalTypeFilter(it) }
                        )
                        2 -> CitizenTriageScreen(
                            activeAlerts = trafficAlerts,
                            doctors = allDoctors,
                            hospitals = allHospitals,
                            selectedTriage = uiState.selectedTriageItem,
                            onSelectTriage = { viewModel.selectTriageItem(it) },
                            onTriggerSos = { emergencyTitle, urgency, specialist, targetHospital, dialNo ->
                                viewModel.triggerSosDispatch(
                                    emergencyTitle,
                                    urgency,
                                    specialist,
                                    targetHospital,
                                    dialNo
                                )
                            },
                            onNavigateToDoctorTab = { selectedTabIndex = 1 }
                        )
                    }
                }
            }
        }
    }

    if (showRoleDialog) {
        RoleSelectionDialog(
            currentRole = uiState.currentRole,
            onRoleSelected = {
                viewModel.selectRole(it)
                selectedTabIndex = 0
            },
            onDismiss = { showRoleDialog = false }
        )
    }
}
