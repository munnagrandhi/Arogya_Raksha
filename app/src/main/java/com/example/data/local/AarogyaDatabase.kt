package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.DoctorEntity
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.HospitalEntity
import com.example.data.model.TrafficAlertEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        DoctorEntity::class,
        HospitalEntity::class,
        TrafficAlertEntity::class,
        EmergencyIncidentEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AarogyaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun doctorDao(): DoctorDao
    abstract fun hospitalDao(): HospitalDao
    abstract fun trafficAlertDao(): TrafficAlertDao
    abstract fun emergencyIncidentDao(): EmergencyIncidentDao

    companion object {
        @Volatile
        private var INSTANCE: AarogyaDatabase? = null

        fun getDatabase(context: Context): AarogyaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AarogyaDatabase::class.java,
                    "aarogya_raksha_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
