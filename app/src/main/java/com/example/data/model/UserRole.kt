package com.example.data.model

enum class UserRole(val displayName: String, val description: String) {
    CITIZEN("General Public", "Patients & family requiring emergency triage, doctor finder & hospital navigation"),
    DOCTOR("Doctor (Govt / Private)", "Registered medical practitioners managing hospital duties & private clinic"),
    GOVT_OFFICER("Govt / Traffic Officer", "District Health Officer (CMO) & Traffic Police emergency green corridor liaison")
}

enum class DoctorType(val displayName: String, val badgeColorHex: Long) {
    GOVT_DOCTOR("Govt Hospital Doctor", 0xFF046A38),
    PRIVATE_DOCTOR("Private Specialist / Consultant", 0xFF0F3E6D),
    DUAL_PRACTICE("Govt Hospital & Private Clinic", 0xFFD97706)
}

enum class DoctorAvailability(val label: String, val colorHex: Long) {
    AVAILABLE_EMERGENCY("On-Duty (Emergency Call)", 0xFF16A34A),
    IN_OPD("In OPD Consultation", 0xFF2563EB),
    IN_SURGERY("In Surgery / OT", 0xFFDC2626),
    OFF_DUTY("Off Duty / On Break", 0xFF64748B)
}
