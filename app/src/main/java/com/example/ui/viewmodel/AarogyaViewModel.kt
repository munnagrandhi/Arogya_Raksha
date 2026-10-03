package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.DoctorAvailability
import com.example.data.model.DoctorEntity
import com.example.data.model.DoctorType
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.EmergencyTriageData
import com.example.data.model.EmergencyTriageItem
import com.example.data.model.HospitalEntity
import com.example.data.model.TrafficAlertEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.repository.AarogyaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AarogyaUiState(
    val isLoggedIn: Boolean = false,
    val currentRole: UserRole = UserRole.CITIZEN,
    val loggedInUserName: String = "",
    val loggedInUserBadge: String = "",
    val selectedTriageItem: EmergencyTriageItem? = null,
    val doctorSearchQuery: String = "",
    val selectedDoctorSpecialtyFilter: String = "ALL",
    val selectedDoctorTypeFilter: String = "ALL", // ALL, GOVT_DOCTOR, PRIVATE_DOCTOR, DUAL_PRACTICE
    val hospitalSearchQuery: String = "",
    val hospitalTypeFilter: String = "ALL", // ALL, GOVT, PRIVATE, AB_PMJAY
    val activeDoctorId: Long? = 1L, // Default selected doctor profile for Doctor Portal
    val lastSosSuccessMessage: String? = null
)

class AarogyaViewModel(private val repository: AarogyaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AarogyaUiState())
    val uiState: StateFlow<AarogyaUiState> = _uiState.asStateFlow()

    val allDoctors: StateFlow<List<DoctorEntity>> = repository.allDoctors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allHospitals: StateFlow<List<HospitalEntity>> = repository.allHospitals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trafficAlerts: StateFlow<List<TrafficAlertEntity>> = repository.allTrafficAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val emergencyIncidents: StateFlow<List<EmergencyIncidentEntity>> = repository.allIncidents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered doctors for directory
    val filteredDoctors: StateFlow<List<DoctorEntity>> = combine(
        allDoctors,
        _uiState
    ) { doctors, state ->
        doctors.filter { doc ->
            val matchesQuery = state.doctorSearchQuery.isBlank() ||
                    doc.name.contains(state.doctorSearchQuery, ignoreCase = true) ||
                    doc.specialty.contains(state.doctorSearchQuery, ignoreCase = true) ||
                    doc.hospitalName.contains(state.doctorSearchQuery, ignoreCase = true) ||
                    doc.clinicName.contains(state.doctorSearchQuery, ignoreCase = true) ||
                    doc.clinicAddress.contains(state.doctorSearchQuery, ignoreCase = true)

            val matchesSpecialty = state.selectedDoctorSpecialtyFilter == "ALL" ||
                    doc.specialty.equals(state.selectedDoctorSpecialtyFilter, ignoreCase = true)

            val matchesType = state.selectedDoctorTypeFilter == "ALL" ||
                    doc.doctorType == state.selectedDoctorTypeFilter

            matchesQuery && matchesSpecialty && matchesType
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered hospitals
    val filteredHospitals: StateFlow<List<HospitalEntity>> = combine(
        allHospitals,
        _uiState
    ) { hospitals, state ->
        hospitals.filter { hosp ->
            val matchesQuery = state.hospitalSearchQuery.isBlank() ||
                    hosp.name.contains(state.hospitalSearchQuery, ignoreCase = true) ||
                    hosp.address.contains(state.hospitalSearchQuery, ignoreCase = true)

            val matchesType = when (state.hospitalTypeFilter) {
                "GOVT" -> hosp.type.startsWith("GOVT")
                "PRIVATE" -> hosp.type.startsWith("PRIVATE")
                "AB_PMJAY" -> hosp.isAyushmanBharatEmpanelled
                else -> true
            }

            matchesQuery && matchesType
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loginAsCitizen(name: String, phone: String, bloodGroup: String) {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = true,
            currentRole = UserRole.CITIZEN,
            loggedInUserName = name.ifBlank { "Citizen ($phone)" },
            loggedInUserBadge = if (bloodGroup.isNotBlank()) "Blood Group: $bloodGroup" else "Verified Citizen"
        )
    }

    fun loginAsDoctor(doctorId: Long, doctorName: String, badge: String) {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = true,
            currentRole = UserRole.DOCTOR,
            activeDoctorId = doctorId,
            loggedInUserName = doctorName,
            loggedInUserBadge = badge
        )
    }

    fun loginAsGovtOfficer(department: String, officerId: String) {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = true,
            currentRole = UserRole.GOVT_OFFICER,
            loggedInUserName = "Officer ($officerId)",
            loggedInUserBadge = department
        )
    }

    fun registerUserWithRole(user: UserEntity) {
        viewModelScope.launch {
            repository.saveUser(user)
            val mappedRole = when (user.role) {
                "GOVERNMENT_DOCTOR", "PRIVATE_DOCTOR" -> UserRole.DOCTOR
                "GOVERNMENT_OFFICIAL" -> UserRole.GOVT_OFFICER
                else -> UserRole.CITIZEN
            }
            val roleBadge = when (user.role) {
                "GOVERNMENT_DOCTOR" -> "Govt Doctor • ${user.hospitalName}"
                "PRIVATE_DOCTOR" -> if (user.hasPrivateClinic) "Private Doctor • ${user.clinicName}" else "Private Specialist"
                "GOVERNMENT_OFFICIAL" -> "Govt Officer • ${user.govtDepartment}"
                else -> if (user.bloodGroup.isNotBlank()) "Blood Group: ${user.bloodGroup}" else "General Citizen"
            }

            var doctorIdToActivate = _uiState.value.activeDoctorId
            if (user.role == "GOVERNMENT_DOCTOR" || user.role == "PRIVATE_DOCTOR") {
                val docType = if (user.hasPrivateClinic && user.hospitalName.isNotBlank()) {
                    "DUAL_PRACTICE"
                } else if (user.role == "GOVERNMENT_DOCTOR") {
                    "GOVT_DOCTOR"
                } else {
                    "PRIVATE_DOCTOR"
                }
                val newDocId = repository.registerDoctor(
                    DoctorEntity(
                        id = 0L,
                        name = if (user.displayName.startsWith("Dr.")) user.displayName else "Dr. ${user.displayName}",
                        qualifications = user.qualifications.ifBlank { "MBBS, MD" },
                        nmcRegNumber = user.nmcRegNumber.ifBlank { "NMC-${System.currentTimeMillis() % 100000}" },
                        specialty = user.specialty.ifBlank { "Emergency Physician" },
                        doctorType = docType,
                        phone = user.phone,
                        hospitalName = user.hospitalName.ifBlank { "District Hospital" },
                        hospitalTimings = user.hospitalTimings.ifBlank { "09:00 AM - 02:00 PM" },
                        hasPrivateClinic = user.hasPrivateClinic,
                        clinicName = user.clinicName,
                        clinicAddress = user.clinicAddress,
                        clinicTimings = user.clinicTimings,
                        clinicConsultFee = user.clinicConsultFee,
                        clinicPhone = user.clinicPhone,
                        availabilityStatus = "AVAILABLE_EMERGENCY",
                        isVerifiedByGovt = true
                    )
                )
                doctorIdToActivate = newDocId
            }

            _uiState.value = _uiState.value.copy(
                isLoggedIn = true,
                currentRole = mappedRole,
                activeDoctorId = doctorIdToActivate,
                loggedInUserName = user.displayName,
                loggedInUserBadge = roleBadge
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearUserSession()
        }
        _uiState.value = _uiState.value.copy(
            isLoggedIn = false,
            loggedInUserName = "",
            loggedInUserBadge = ""
        )
    }

    fun selectRole(role: UserRole) {
        _uiState.value = _uiState.value.copy(currentRole = role)
    }

    fun selectTriageItem(item: EmergencyTriageItem?) {
        _uiState.value = _uiState.value.copy(selectedTriageItem = item)
    }

    fun setDoctorSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(doctorSearchQuery = query)
    }

    fun setDoctorSpecialtyFilter(specialty: String) {
        _uiState.value = _uiState.value.copy(selectedDoctorSpecialtyFilter = specialty)
    }

    fun setDoctorTypeFilter(type: String) {
        _uiState.value = _uiState.value.copy(selectedDoctorTypeFilter = type)
    }

    fun setHospitalSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(hospitalSearchQuery = query)
    }

    fun setHospitalTypeFilter(filter: String) {
        _uiState.value = _uiState.value.copy(hospitalTypeFilter = filter)
    }

    fun setActiveDoctorId(id: Long) {
        _uiState.value = _uiState.value.copy(activeDoctorId = id)
    }

    fun clearSosMessage() {
        _uiState.value = _uiState.value.copy(lastSosSuccessMessage = null)
    }

    // Citizen SOS Dispatch Action
    fun triggerSosDispatch(
        emergencyTitle: String,
        urgency: String,
        specialist: String,
        targetHospital: String,
        dialNumber: String
    ) {
        viewModelScope.launch {
            val incident = EmergencyIncidentEntity(
                emergencyType = emergencyTitle,
                urgencyLevel = urgency,
                specialistRecommended = specialist,
                hospitalTargeted = targetHospital,
                dialNumber = dialNumber,
                status = "AMBULANCE_DISPATCH_ALERT"
            )
            repository.logEmergencyIncident(incident)
            _uiState.value = _uiState.value.copy(
                lastSosSuccessMessage = "Emergency alert registered! Dialing $dialNumber & routing to $targetHospital"
            )
        }
    }

    // Doctor Actions
    fun updateDoctorAvailability(doctorId: Long, status: String) {
        viewModelScope.launch {
            repository.updateDoctorAvailability(doctorId, status)
        }
    }

    fun registerOrUpdateDoctor(doctor: DoctorEntity) {
        viewModelScope.launch {
            if (doctor.id == 0L) {
                val newId = repository.registerDoctor(doctor)
                _uiState.value = _uiState.value.copy(activeDoctorId = newId)
            } else {
                repository.updateDoctor(doctor)
            }
        }
    }

    // Govt Officer Actions
    fun toggleGreenCorridor(hospitalId: Long, active: Boolean, route: String) {
        viewModelScope.launch {
            repository.toggleGreenCorridor(hospitalId, active, route)
            if (active) {
                repository.postTrafficAlert(
                    TrafficAlertEntity(
                        title = "🚨 GREEN CORRIDOR ACTIVATED: Priority Clearance",
                        routeDescription = route,
                        alertType = "GREEN_CORRIDOR",
                        affectedHospitalName = "Hospital #$hospitalId",
                        broadcastByOfficer = "CMO / Traffic Police Emergency Unit"
                    )
                )
            }
        }
    }

    fun updateHospitalBeds(hospitalId: Long, icuBeds: Int, oxygenBeds: Int) {
        viewModelScope.launch {
            repository.updateHospitalBeds(hospitalId, icuBeds, oxygenBeds)
        }
    }

    fun updateHospitalTraffic(hospitalId: Long, trafficCondition: String, minutes: Int) {
        viewModelScope.launch {
            repository.updateHospitalTraffic(hospitalId, trafficCondition, minutes)
        }
    }

    fun postTrafficAlert(title: String, route: String, type: String, hospitalName: String, officer: String) {
        viewModelScope.launch {
            val alert = TrafficAlertEntity(
                title = title,
                routeDescription = route,
                alertType = type,
                affectedHospitalName = hospitalName,
                broadcastByOfficer = officer
            )
            repository.postTrafficAlert(alert)
        }
    }

    fun verifyDoctorNmc(doctorId: Long, isVerified: Boolean) {
        viewModelScope.launch {
            repository.verifyDoctorNmc(doctorId, isVerified)
        }
    }
}

class AarogyaViewModelFactory(private val repository: AarogyaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AarogyaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AarogyaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
