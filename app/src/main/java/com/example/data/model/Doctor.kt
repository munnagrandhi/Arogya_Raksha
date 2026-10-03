package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val qualifications: String,
    val nmcRegNumber: String, // National Medical Commission / State Council Registration No.
    val specialty: String,
    val doctorType: String, // "GOVT_DOCTOR", "PRIVATE_DOCTOR", "DUAL_PRACTICE"
    val phone: String,
    val email: String = "",
    val experienceYears: Int = 10,
    
    // Hospital affiliation
    val hospitalName: String,
    val hospitalDesignation: String = "Consultant Specialist",
    val hospitalTimings: String, // e.g. "Mon - Sat: 08:30 AM - 01:30 PM"
    val hospitalCity: String = "Delhi NCR",

    // Private clinic affiliation (Dual practice convenience)
    val hasPrivateClinic: Boolean = false,
    val clinicName: String = "",
    val clinicAddress: String = "",
    val clinicTimings: String = "", // e.g. "Evening 05:30 PM - 08:30 PM"
    val clinicConsultFee: Int = 500, // In Indian Rupees (₹)
    val clinicPhone: String = "",

    // Current emergency on-duty status
    val availabilityStatus: String = "AVAILABLE_EMERGENCY", // AVAILABLE_EMERGENCY, IN_OPD, IN_SURGERY, OFF_DUTY
    val isVerifiedByGovt: Boolean = true,
    val rating: Double = 4.8,
    val totalConsults: Int = 1240
)
