package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppRole
import com.example.data.model.EmergencyTriageData
import com.example.data.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Aarogya Raksha", appName)
    }

    @Test
    fun `verify emergency triage data has critical specialist recommendations`() {
        val emergencies = EmergencyTriageData.allEmergencies
        assertTrue(emergencies.isNotEmpty())
        val cardiac = emergencies.find { it.id == "cardiac_arrest" }
        assertNotNull(cardiac)
        assertEquals("Cardiologist", cardiac?.requiredSpecialty)
        assertEquals("108", cardiac?.emergencyHotline)
    }

    @Test
    fun `verify three login user roles exist`() {
        val roles = UserRole.values()
        assertEquals(3, roles.size)
        assertTrue(roles.contains(UserRole.CITIZEN))
        assertTrue(roles.contains(UserRole.DOCTOR))
        assertTrue(roles.contains(UserRole.GOVT_OFFICER))
    }

    @Test
    fun `verify registration differentiates between 4 roles`() {
        val appRoles = AppRole.values()
        assertEquals(4, appRoles.size)
        assertTrue(appRoles.contains(AppRole.GENERAL_PUBLIC))
        assertTrue(appRoles.contains(AppRole.PRIVATE_DOCTOR))
        assertTrue(appRoles.contains(AppRole.GOVERNMENT_DOCTOR))
        assertTrue(appRoles.contains(AppRole.GOVERNMENT_OFFICIAL))
    }
}
