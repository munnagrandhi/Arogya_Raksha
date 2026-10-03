package com.example.data.repository

import android.util.Log
import com.example.data.local.DoctorDao
import com.example.data.local.EmergencyIncidentDao
import com.example.data.local.HospitalDao
import com.example.data.local.InitialDataSeed
import com.example.data.local.TrafficAlertDao
import com.example.data.local.UserDao
import com.example.data.model.DoctorEntity
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.HospitalEntity
import com.example.data.model.TrafficAlertEntity
import com.example.data.model.UserEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AarogyaRepository(
    private val userDao: UserDao,
    private val doctorDao: DoctorDao,
    private val hospitalDao: HospitalDao,
    private val trafficAlertDao: TrafficAlertDao,
    private val incidentDao: EmergencyIncidentDao,
    private val firestoreDatabaseId: String
) {
    val activeUser: Flow<UserEntity?> = userDao.getActiveUser()
    val allDoctors: Flow<List<DoctorEntity>> = doctorDao.getAllDoctors()
    val allHospitals: Flow<List<HospitalEntity>> = hospitalDao.getAllHospitals()
    val allTrafficAlerts: Flow<List<TrafficAlertEntity>> = trafficAlertDao.getAllAlerts()
    val activeTrafficAlerts: Flow<List<TrafficAlertEntity>> = trafficAlertDao.getActiveAlerts()
    val allIncidents: Flow<List<EmergencyIncidentEntity>> = incidentDao.getAllIncidents()

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance(firestoreDatabaseId)
        } catch (e: Exception) {
            Log.w("AarogyaRepository", "Could not initialize Firestore with $firestoreDatabaseId", e)
            null
        }
    }

    suspend fun initializeSeedDataIfNeeded() = withContext(Dispatchers.IO) {
        if (doctorDao.getDoctorCount() == 0) {
            doctorDao.insertDoctors(InitialDataSeed.getInitialDoctors())
        }
        if (hospitalDao.getHospitalCount() == 0) {
            hospitalDao.insertHospitals(InitialDataSeed.getInitialHospitals())
        }
        if (trafficAlertDao.getAlertCount() == 0) {
            trafficAlertDao.insertAlerts(InitialDataSeed.getInitialTrafficAlerts())
        }
    }

    // User operations (Room User Database + Cloud Firestore)
    suspend fun saveUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
        try {
            firestore?.collection("users")?.document(user.userId)?.set(
                mapOf(
                    "userId" to user.userId,
                    "displayName" to user.displayName,
                    "email" to user.email,
                    "role" to user.role,
                    "phone" to user.phone,
                    "bloodGroup" to user.bloodGroup,
                    "emergencyContact" to user.emergencyContact,
                    "medicalCouncil" to user.medicalCouncil,
                    "nmcRegNumber" to user.nmcRegNumber,
                    "registrationYear" to user.registrationYear,
                    "qualifications" to user.qualifications,
                    "primaryDegree" to user.primaryDegree,
                    "postGraduateDegrees" to user.postGraduateDegrees,
                    "additionalCertifications" to user.additionalCertifications,
                    "experienceYears" to user.experienceYears,
                    "specialty" to user.specialty,
                    "subSpecialties" to user.subSpecialties,
                    "clinicalInterests" to user.clinicalInterests,
                    "languagesSpoken" to user.languagesSpoken,
                    "hospitalName" to user.hospitalName,
                    "hospitalType" to user.hospitalType,
                    "hospitalDesignation" to user.hospitalDesignation,
                    "hospitalDepartment" to user.hospitalDepartment,
                    "hospitalTimings" to user.hospitalTimings,
                    "hospitalCity" to user.hospitalCity,
                    "emergencyDutyAvailable" to user.emergencyDutyAvailable,
                    "hasPrivateClinic" to user.hasPrivateClinic,
                    "clinicName" to user.clinicName,
                    "clinicAddress" to user.clinicAddress,
                    "clinicCity" to user.clinicCity,
                    "clinicTimings" to user.clinicTimings,
                    "clinicConsultFee" to user.clinicConsultFee,
                    "clinicPhone" to user.clinicPhone,
                    "appointmentBookingAvailable" to user.appointmentBookingAvailable,
                    "govtDepartment" to user.govtDepartment,
                    "govtBadgeId" to user.govtBadgeId,
                    "createdAt" to user.createdAt,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            Log.w("AarogyaRepository", "Firestore sync failed", e)
        }
    }

    suspend fun clearUserSession() = withContext(Dispatchers.IO) {
        userDao.clearAllUsers()
    }

    // Doctor operations
    suspend fun registerDoctor(doctor: DoctorEntity): Long = withContext(Dispatchers.IO) {
        doctorDao.insertDoctor(doctor)
    }

    suspend fun updateDoctor(doctor: DoctorEntity) = withContext(Dispatchers.IO) {
        doctorDao.updateDoctor(doctor)
    }

    suspend fun updateDoctorAvailability(doctorId: Long, status: String) = withContext(Dispatchers.IO) {
        doctorDao.updateAvailabilityStatus(doctorId, status)
    }

    suspend fun verifyDoctorByGovt(doctorId: Long, isVerified: Boolean) = withContext(Dispatchers.IO) {
        doctorDao.updateVerification(doctorId, isVerified)
    }

    suspend fun verifyDoctorNmc(doctorId: Long, isVerified: Boolean) = withContext(Dispatchers.IO) {
        doctorDao.updateVerification(doctorId, isVerified)
    }

    // Hospital operations
    suspend fun updateHospital(hospital: HospitalEntity) = withContext(Dispatchers.IO) {
        hospitalDao.updateHospital(hospital)
    }

    suspend fun toggleGreenCorridor(hospitalId: Long, active: Boolean, route: String) = withContext(Dispatchers.IO) {
        hospitalDao.updateGreenCorridor(hospitalId, active, route)
    }

    suspend fun updateHospitalBeds(hospitalId: Long, icuBeds: Int, oxygenBeds: Int) = withContext(Dispatchers.IO) {
        hospitalDao.updateBeds(hospitalId, icuBeds, oxygenBeds)
    }

    suspend fun updateHospitalTraffic(hospitalId: Long, trafficCondition: String, minutes: Int) = withContext(Dispatchers.IO) {
        hospitalDao.updateTraffic(hospitalId, trafficCondition, minutes)
    }

    // Traffic Alerts
    suspend fun postTrafficAlert(alert: TrafficAlertEntity): Long = withContext(Dispatchers.IO) {
        trafficAlertDao.insertAlert(alert)
    }

    suspend fun toggleAlertStatus(alertId: Long, isActive: Boolean) = withContext(Dispatchers.IO) {
        trafficAlertDao.setAlertActive(alertId, isActive)
    }

    // Emergency Incidents / SOS
    suspend fun logEmergencyIncident(incident: EmergencyIncidentEntity): Long = withContext(Dispatchers.IO) {
        incidentDao.insertIncident(incident)
    }
}
