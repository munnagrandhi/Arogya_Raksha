package com.example.data.model

/**
 * Structured data models for doctor registration, credentials verification,
 * hospital/clinic affiliations, and area of specialization.
 */

data class DoctorCredentialsData(
    val medicalCouncil: String = "National Medical Commission (NMC)",
    val registrationNumber: String = "",
    val registrationYear: Int = 2018,
    val primaryDegree: String = "MBBS",
    val postGraduateDegrees: String = "MD (Medicine)",
    val additionalCertifications: String = "",
    val yearsOfExperience: Int = 8
)

data class HospitalAffiliationData(
    val hospitalName: String = "",
    val hospitalType: String = "Government Apex Hospital", // Government Apex, District Civil Hospital, Private Multispeciality
    val designation: String = "Consultant Specialist",
    val department: String = "Emergency & Critical Care",
    val opdTimings: String = "Mon - Sat: 08:30 AM - 01:30 PM",
    val hospitalCity: String = "Delhi NCR",
    val emergencyDutyAvailable: Boolean = true
)

data class ClinicAffiliationData(
    val runsPrivateClinic: Boolean = true,
    val clinicName: String = "",
    val clinicAddress: String = "",
    val clinicCity: String = "Delhi NCR",
    val eveningHours: String = "Mon - Sat: 05:30 PM - 08:30 PM",
    val consultationFeeInr: Int = 500,
    val clinicContactPhone: String = "",
    val appointmentBookingAvailable: Boolean = true
)

data class DoctorSpecializationData(
    val primarySpecialty: String = "Cardiology",
    val subSpecialties: String = "",
    val clinicalInterests: String = "",
    val languagesSpoken: String = "Hindi, English"
)

data class CompleteDoctorProfile(
    val userId: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val role: String, // "GOVERNMENT_DOCTOR" or "PRIVATE_DOCTOR"
    val credentials: DoctorCredentialsData,
    val hospitalAffiliation: HospitalAffiliationData,
    val clinicAffiliation: ClinicAffiliationData,
    val specialization: DoctorSpecializationData
)

fun CompleteDoctorProfile.toUserEntity(): UserEntity {
    return UserEntity(
        userId = userId,
        displayName = fullName,
        email = email,
        role = role,
        phone = phone,
        medicalCouncil = credentials.medicalCouncil,
        nmcRegNumber = credentials.registrationNumber,
        registrationYear = credentials.registrationYear,
        qualifications = listOf(credentials.primaryDegree, credentials.postGraduateDegrees)
            .filter { it.isNotBlank() }
            .joinToString(", "),
        primaryDegree = credentials.primaryDegree,
        postGraduateDegrees = credentials.postGraduateDegrees,
        additionalCertifications = credentials.additionalCertifications,
        experienceYears = credentials.yearsOfExperience,
        specialty = specialization.primarySpecialty,
        subSpecialties = specialization.subSpecialties,
        clinicalInterests = specialization.clinicalInterests,
        languagesSpoken = specialization.languagesSpoken,
        hospitalName = hospitalAffiliation.hospitalName,
        hospitalType = hospitalAffiliation.hospitalType,
        hospitalDesignation = hospitalAffiliation.designation,
        hospitalDepartment = hospitalAffiliation.department,
        hospitalTimings = hospitalAffiliation.opdTimings,
        hospitalCity = hospitalAffiliation.hospitalCity,
        emergencyDutyAvailable = hospitalAffiliation.emergencyDutyAvailable,
        hasPrivateClinic = clinicAffiliation.runsPrivateClinic,
        clinicName = clinicAffiliation.clinicName,
        clinicAddress = clinicAffiliation.clinicAddress,
        clinicCity = clinicAffiliation.clinicCity,
        clinicTimings = clinicAffiliation.eveningHours,
        clinicConsultFee = clinicAffiliation.consultationFeeInr,
        clinicPhone = clinicAffiliation.clinicContactPhone,
        appointmentBookingAvailable = clinicAffiliation.appointmentBookingAvailable,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}

fun CompleteDoctorProfile.toDoctorEntity(existingId: Long = 0L): DoctorEntity {
    val docType = if (clinicAffiliation.runsPrivateClinic && hospitalAffiliation.hospitalName.isNotBlank()) {
        "DUAL_PRACTICE"
    } else if (role == "GOVERNMENT_DOCTOR") {
        "GOVT_DOCTOR"
    } else {
        "PRIVATE_DOCTOR"
    }
    return DoctorEntity(
        id = existingId,
        name = fullName,
        qualifications = listOf(credentials.primaryDegree, credentials.postGraduateDegrees)
            .filter { it.isNotBlank() }
            .joinToString(", "),
        nmcRegNumber = credentials.registrationNumber,
        specialty = specialization.primarySpecialty,
        doctorType = docType,
        phone = phone,
        email = email,
        experienceYears = credentials.yearsOfExperience,
        hospitalName = hospitalAffiliation.hospitalName,
        hospitalDesignation = hospitalAffiliation.designation,
        hospitalTimings = hospitalAffiliation.opdTimings,
        hospitalCity = hospitalAffiliation.hospitalCity,
        hasPrivateClinic = clinicAffiliation.runsPrivateClinic,
        clinicName = clinicAffiliation.clinicName,
        clinicAddress = clinicAffiliation.clinicAddress,
        clinicTimings = clinicAffiliation.eveningHours,
        clinicConsultFee = clinicAffiliation.consultationFeeInr,
        clinicPhone = clinicAffiliation.clinicContactPhone,
        availabilityStatus = if (hospitalAffiliation.emergencyDutyAvailable) "AVAILABLE_EMERGENCY" else "IN_OPD",
        isVerifiedByGovt = true
    )
}
