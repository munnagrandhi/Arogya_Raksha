package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppRole
import com.example.data.model.ClinicAffiliationData
import com.example.data.model.CompleteDoctorProfile
import com.example.data.model.DoctorCredentialsData
import com.example.data.model.DoctorSpecializationData
import com.example.data.model.EmergencyTriageData
import com.example.data.model.HospitalAffiliationData
import com.example.data.model.UserRole
import com.example.data.model.toDoctorEntity
import com.example.data.model.toUserEntity
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

    @Test
    fun `verify doctor profile data model stores medical credentials hospital clinic affiliations and specialization in user entity`() {
        val doctorProfile = CompleteDoctorProfile(
            userId = "doc_delhi_101",
            fullName = "Dr. Rajesh K. Varma",
            email = "dr.varma@aiims.edu.in",
            phone = "+91 98111 22334",
            role = "GOVERNMENT_DOCTOR",
            credentials = DoctorCredentialsData(
                medicalCouncil = "National Medical Commission (NMC)",
                registrationNumber = "NMC/2014/98765",
                registrationYear = 2014,
                primaryDegree = "MBBS",
                postGraduateDegrees = "MD, DM (Cardiology)",
                additionalCertifications = "Fellowship in Interventional Cardiology (FSCAI)",
                yearsOfExperience = 12
            ),
            hospitalAffiliation = HospitalAffiliationData(
                hospitalName = "All India Institute of Medical Sciences (AIIMS)",
                hospitalType = "Government Apex Hospital",
                designation = "Senior Consultant & Head of Cath Lab",
                department = "Department of Cardiology & Emergency Triage",
                opdTimings = "Mon - Sat: 08:30 AM - 01:30 PM",
                hospitalCity = "New Delhi",
                emergencyDutyAvailable = true
            ),
            clinicAffiliation = ClinicAffiliationData(
                runsPrivateClinic = true,
                clinicName = "Varma Heart & Cardiac Rhythm Centre",
                clinicAddress = "B-42, South Extension Part II",
                clinicCity = "New Delhi",
                eveningHours = "Mon - Fri: 05:30 PM - 08:30 PM",
                consultationFeeInr = 800,
                clinicContactPhone = "+91 11 2625 0000",
                appointmentBookingAvailable = true
            ),
            specialization = DoctorSpecializationData(
                primarySpecialty = "Cardiology",
                subSpecialties = "Primary Angioplasty, STEMI Golden Hour, Heart Failure",
                clinicalInterests = "Emergency Resuscitation & Complex Coronary Interventions",
                languagesSpoken = "Hindi, English, Punjabi"
            )
        )

        // Store in User Profile Entity
        val userEntity = doctorProfile.toUserEntity()

        assertEquals("doc_delhi_101", userEntity.userId)
        assertEquals("Dr. Rajesh K. Varma", userEntity.displayName)
        assertEquals("GOVERNMENT_DOCTOR", userEntity.role)

        // Medical Credentials Verification
        assertEquals("National Medical Commission (NMC)", userEntity.medicalCouncil)
        assertEquals("NMC/2014/98765", userEntity.nmcRegNumber)
        assertEquals(2014, userEntity.registrationYear)
        assertEquals("MBBS", userEntity.primaryDegree)
        assertEquals("MD, DM (Cardiology)", userEntity.postGraduateDegrees)
        assertEquals("Fellowship in Interventional Cardiology (FSCAI)", userEntity.additionalCertifications)
        assertEquals(12, userEntity.experienceYears)

        // Specialization Verification
        assertEquals("Cardiology", userEntity.specialty)
        assertEquals("Primary Angioplasty, STEMI Golden Hour, Heart Failure", userEntity.subSpecialties)
        assertEquals("Emergency Resuscitation & Complex Coronary Interventions", userEntity.clinicalInterests)
        assertEquals("Hindi, English, Punjabi", userEntity.languagesSpoken)

        // Current Hospital Affiliation Verification
        assertEquals("All India Institute of Medical Sciences (AIIMS)", userEntity.hospitalName)
        assertEquals("Government Apex Hospital", userEntity.hospitalType)
        assertEquals("Senior Consultant & Head of Cath Lab", userEntity.hospitalDesignation)
        assertEquals("Department of Cardiology & Emergency Triage", userEntity.hospitalDepartment)
        assertEquals("Mon - Sat: 08:30 AM - 01:30 PM", userEntity.hospitalTimings)
        assertEquals("New Delhi", userEntity.hospitalCity)
        assertTrue(userEntity.emergencyDutyAvailable)

        // Current Clinic Affiliation Verification (Dual Practice)
        assertTrue(userEntity.hasPrivateClinic)
        assertEquals("Varma Heart & Cardiac Rhythm Centre", userEntity.clinicName)
        assertEquals("B-42, South Extension Part II", userEntity.clinicAddress)
        assertEquals("New Delhi", userEntity.clinicCity)
        assertEquals("Mon - Fri: 05:30 PM - 08:30 PM", userEntity.clinicTimings)
        assertEquals(800, userEntity.clinicConsultFee)
        assertEquals("+91 11 2625 0000", userEntity.clinicPhone)
        assertTrue(userEntity.appointmentBookingAvailable)

        // Doctor Entity mapping
        val doctorEntity = doctorProfile.toDoctorEntity(42L)
        assertEquals(42L, doctorEntity.id)
        assertEquals("DUAL_PRACTICE", doctorEntity.doctorType)
        assertEquals("Cardiology", doctorEntity.specialty)
        assertEquals("AVAILABLE_EMERGENCY", doctorEntity.availabilityStatus)
    }
}
