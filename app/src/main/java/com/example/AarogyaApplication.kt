package com.example

import android.app.Application
import com.example.data.local.AarogyaDatabase
import com.example.data.repository.AarogyaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AarogyaApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val database by lazy { AarogyaDatabase.getDatabase(this) }
    val repository by lazy {
        val dbId = try {
            getString(R.string.firestore_database_id)
        } catch (_: Exception) {
            "ai-studio-android-aarogyar-509c2f92-de8a-4752-bbc2-420d5de35c5a"
        }
        AarogyaRepository(
            userDao = database.userDao(),
            doctorDao = database.doctorDao(),
            hospitalDao = database.hospitalDao(),
            trafficAlertDao = database.trafficAlertDao(),
            incidentDao = database.emergencyIncidentDao(),
            firestoreDatabaseId = dbId
        )
    }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            repository.initializeSeedDataIfNeeded()
        }
    }
}
