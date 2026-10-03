package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "traffic_alerts")
data class TrafficAlertEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val routeDescription: String,
    val alertType: String, // "GREEN_CORRIDOR", "TRAFFIC_JAM", "ROAD_DIVERSION", "ACCIDENT_CLEARANCE"
    val affectedHospitalName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val broadcastByOfficer: String = "District Traffic Liaison Cell"
)

@Entity(tableName = "emergency_incidents")
data class EmergencyIncidentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val emergencyType: String,
    val urgencyLevel: String,
    val specialistRecommended: String,
    val hospitalTargeted: String,
    val dialNumber: String = "108",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "ALERT_TRIGGERED",
    val patientName: String = "Emergency Citizen",
    val locationNotes: String = "Current Device GPS Coordinates Shared"
)
