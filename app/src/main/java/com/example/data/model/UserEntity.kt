package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AppRole(val code: String, val title: String, val subtitle: String) {
    GENERAL_PUBLIC(
        "GENERAL_PUBLIC",
        "General Public",
        "For patients & citizens seeking emergency triage, specialist doctors & hospital routes"
    ),
    PRIVATE_DOCTOR(
        "PRIVATE_DOCTOR",
        "Private Doctor",
        "For private medical consultants & specialists running clinics or in private hospitals"
    ),
    GOVERNMENT_DOCTOR(
        "GOVERNMENT_DOCTOR",
        "Government Doctor",
        "For doctors appointed in Govt Civil Hospitals, AIIMS, CHC/PHC with OPD & emergency duties"
    ),
    GOVERNMENT_OFFICIAL(
        "GOVERNMENT_OFFICIAL",
        "Government Official",
        "For District CMO, Health Directorate & Traffic Police Green Corridor liaison officers"
    )
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val displayName: String,
    val email: String = "",
    val role: String, // GENERAL_PUBLIC, PRIVATE_DOCTOR, GOVERNMENT_DOCTOR, GOVERNMENT_OFFICIAL
    val phone: String = "",
    
    // General Public details
    val bloodGroup: String = "",
    val emergencyContact: String = "",

    // Medical Credentials
    val medicalCouncil: String = "National Medical Commission (NMC)",
    val nmcRegNumber: String = "",
    val registrationYear: Int = 2018,
    val qualifications: String = "MBBS, MD",
    val primaryDegree: String = "MBBS",
    val postGraduateDegrees: String = "",
    val additionalCertifications: String = "",
    val experienceYears: Int = 8,

    // Area of Specialization
    val specialty: String = "Cardiology",
    val subSpecialties: String = "",
    val clinicalInterests: String = "",
    val languagesSpoken: String = "Hindi, English",

    // Current Hospital Affiliation
    val hospitalName: String = "",
    val hospitalType: String = "Government Apex Hospital",
    val hospitalDesignation: String = "Consultant Specialist",
    val hospitalDepartment: String = "Emergency & Critical Care",
    val hospitalTimings: String = "",
    val hospitalCity: String = "Delhi NCR",
    val emergencyDutyAvailable: Boolean = true,

    // Current Clinic Affiliation (Dual practice convenience)
    val hasPrivateClinic: Boolean = false,
    val clinicName: String = "",
    val clinicAddress: String = "",
    val clinicCity: String = "Delhi NCR",
    val clinicTimings: String = "",
    val clinicConsultFee: Int = 0,
    val clinicPhone: String = "",
    val appointmentBookingAvailable: Boolean = true,

    // Government Official details
    val govtDepartment: String = "",
    val govtBadgeId: String = "",

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
