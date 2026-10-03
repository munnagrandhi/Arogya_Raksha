package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.HospitalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HospitalDao {
    @Query("SELECT * FROM hospitals ORDER BY distanceKm ASC")
    fun getAllHospitals(): Flow<List<HospitalEntity>>

    @Query("SELECT * FROM hospitals WHERE id = :id")
    suspend fun getHospitalById(id: Long): HospitalEntity?

    @Query("SELECT COUNT(*) FROM hospitals")
    suspend fun getHospitalCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHospital(hospital: HospitalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHospitals(hospitals: List<HospitalEntity>)

    @Update
    suspend fun updateHospital(hospital: HospitalEntity)

    @Query("UPDATE hospitals SET greenCorridorActive = :active, greenCorridorRoute = :route WHERE id = :hospitalId")
    suspend fun updateGreenCorridor(hospitalId: Long, active: Boolean, route: String)

    @Query("UPDATE hospitals SET icuBedsAvailable = :icuBeds, oxygenBedsAvailable = :oxygenBeds WHERE id = :hospitalId")
    suspend fun updateBeds(hospitalId: Long, icuBeds: Int, oxygenBeds: Int)

    @Query("UPDATE hospitals SET trafficCondition = :traffic, estimatedTravelMinutes = :minutes WHERE id = :hospitalId")
    suspend fun updateTraffic(hospitalId: Long, traffic: String, minutes: Int)
}
