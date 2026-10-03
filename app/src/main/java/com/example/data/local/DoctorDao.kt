package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DoctorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {
    @Query("SELECT * FROM doctors ORDER BY id ASC")
    fun getAllDoctors(): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE id = :id")
    suspend fun getDoctorById(id: Long): DoctorEntity?

    @Query("SELECT * FROM doctors WHERE specialty = :specialty ORDER BY rating DESC")
    fun getDoctorsBySpecialty(specialty: String): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE doctorType = :doctorType ORDER BY rating DESC")
    fun getDoctorsByType(doctorType: String): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE availabilityStatus = 'AVAILABLE_EMERGENCY' ORDER BY rating DESC")
    fun getEmergencyAvailableDoctors(): Flow<List<DoctorEntity>>

    @Query("SELECT COUNT(*) FROM doctors")
    suspend fun getDoctorCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctor(doctor: DoctorEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<DoctorEntity>)

    @Update
    suspend fun updateDoctor(doctor: DoctorEntity)

    @Delete
    suspend fun deleteDoctor(doctor: DoctorEntity)

    @Query("UPDATE doctors SET availabilityStatus = :status WHERE id = :doctorId")
    suspend fun updateAvailabilityStatus(doctorId: Long, status: String)

    @Query("UPDATE doctors SET isVerifiedByGovt = :isVerified WHERE id = :doctorId")
    suspend fun updateVerification(doctorId: Long, isVerified: Boolean)
}
