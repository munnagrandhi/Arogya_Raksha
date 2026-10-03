package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hospitals")
data class HospitalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: String, // "GOVT_APEX", "GOVT_DISTRICT", "PRIVATE_MULTISPECIALITY", "PRIVATE_HOSPITAL"
    val address: String,
    val city: String = "Delhi NCR",
    val distanceKm: Double = 3.2,
    val emergencyPhone: String,
    val ambulancePhone: String = "108",
    
    // Critical care infrastructure (synced with District Health Control)
    val icuBedsAvailable: Int = 8,
    val totalIcuBeds: Int = 30,
    val oxygenBedsAvailable: Int = 18,
    val hasCathLab: Boolean = true,
    val hasStrokeUnit: Boolean = true,
    val hasTraumaCenter: Boolean = true,
    val hasAntiSnakeVenom: Boolean = true, // Critical Indian standard
    val hasBurnUnit: Boolean = false,
    val hasBloodBank: Boolean = true,
    val isAyushmanBharatEmpanelled: Boolean = true, // AB-PMJAY
    
    // Real-time Traffic & Green Corridor Sync
    val trafficCondition: String = "MODERATE", // "CLEAR", "MODERATE", "HEAVY_CONGESTION"
    val estimatedTravelMinutes: Int = 14,
    val greenCorridorActive: Boolean = false,
    val greenCorridorRoute: String = "",
    val latitude: Double = 28.5672,
    val longitude: Double = 77.2100
)
