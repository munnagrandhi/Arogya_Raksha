package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.TrafficAlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrafficAlertDao {
    @Query("SELECT * FROM traffic_alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<TrafficAlertEntity>>

    @Query("SELECT * FROM traffic_alerts WHERE isActive = 1 ORDER BY timestamp DESC")
    fun getActiveAlerts(): Flow<List<TrafficAlertEntity>>

    @Query("SELECT COUNT(*) FROM traffic_alerts")
    suspend fun getAlertCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: TrafficAlertEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<TrafficAlertEntity>)

    @Update
    suspend fun updateAlert(alert: TrafficAlertEntity)

    @Query("UPDATE traffic_alerts SET isActive = :isActive WHERE id = :id")
    suspend fun setAlertActive(id: Long, isActive: Boolean)
}

@Dao
interface EmergencyIncidentDao {
    @Query("SELECT * FROM emergency_incidents ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<EmergencyIncidentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: EmergencyIncidentEntity): Long

    @Query("UPDATE emergency_incidents SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)
}
