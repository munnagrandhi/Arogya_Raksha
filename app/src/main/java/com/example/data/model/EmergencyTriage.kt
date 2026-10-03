package com.example.data.model

data class EmergencyTriageItem(
    val id: String,
    val title: String,
    val hindiTitle: String,
    val requiredSpecialty: String,
    val urgencyLevel: UrgencyLevel,
    val goldenHourMinutes: Int,
    val recommendedDoctorTitle: String,
    val keySymptoms: List<String>,
    val firstAidDos: List<String>,
    val firstAidDonts: List<String>,
    val emergencyHotline: String, // 108, 112, 102
    val hospitalRequirement: String // e.g. "Needs Cath Lab / 24x7 Trauma / ASV stock"
)

enum class UrgencyLevel(val label: String, val colorHex: Long, val badgeText: String) {
    CODE_RED("CRITICAL (Code Red)", 0xFFDC2626, "Immediate Resuscitation Required"),
    CODE_AMBER("URGENT (Code Amber)", 0xFFD97706, "Urgent Hospital Evaluation (<1 hr)"),
    CODE_YELLOW("ACUTE (Code Yellow)", 0xFF2563EB, "Specialist OPD / Emergency Care")
}

object EmergencyTriageData {
    val allEmergencies = listOf(
        EmergencyTriageItem(
            id = "cardiac_arrest",
            title = "Heart Attack / Severe Chest Pain",
            hindiTitle = "दिल का दौरा / सीने में तेज दर्द",
            requiredSpecialty = "Cardiologist",
            urgencyLevel = UrgencyLevel.CODE_RED,
            goldenHourMinutes = 60,
            recommendedDoctorTitle = "Interventional Cardiologist / Emergency Physician",
            keySymptoms = listOf(
                "Crushing central chest pain radiating to left arm, neck or jaw",
                "Profuse cold sweating, shortness of breath, dizziness",
                "Feeling of impending doom or sudden collapse"
            ),
            firstAidDos = listOf(
                "Call 108 / 112 immediately. Inform 'Suspected STEMI / Heart Attack'",
                "Make patient sit comfortably in semi-reclined position (W-position)",
                "If not allergic and conscious, give Disprin / Aspirin 300mg to chew immediately",
                "If unresponsive and no normal breathing, begin chest compressions (CPR) at 100-120 bpm"
            ),
            firstAidDonts = listOf(
                "Do NOT allow patient to walk, drive or exert effort",
                "Do NOT give heavy food, hot tea or heavy fluids",
                "Do NOT wait hoping it is 'just acidity'"
            ),
            emergencyHotline = "108",
            hospitalRequirement = "Hospital with 24x7 Cath Lab & Emergency ICU"
        ),
        EmergencyTriageItem(
            id = "acute_stroke",
            title = "Brain Stroke / Paralysis (FAST)",
            hindiTitle = "ब्रेन स्ट्रोक / लकवा",
            requiredSpecialty = "Neurologist",
            urgencyLevel = UrgencyLevel.CODE_RED,
            goldenHourMinutes = 270, // 4.5 hours for IV tPA thrombolysis
            recommendedDoctorTitle = "Stroke Neurologist / Neurosurgeon",
            keySymptoms = listOf(
                "F - Face Drooping (uneven smile)",
                "A - Arm Weakness (cannot lift one arm)",
                "S - Speech Difficulty (slurred or unintelligible words)",
                "T - Time to call 108 immediately"
            ),
            firstAidDos = listOf(
                "Note exact time when symptoms first appeared (Critical for clot-busting medicine)",
                "Call 108 / 112 and request a Stroke-ready hospital with CT Scan",
                "Keep patient lying down on their side to prevent choking if vomiting occurs",
                "Loosen tight clothing around neck and chest"
            ),
            firstAidDonts = listOf(
                "Do NOT give Aspirin/Disprin (could be hemorrhagic bleeding stroke!)",
                "Do NOT give anything to eat, drink, or oral medicines",
                "Do NOT delay waiting for symptoms to 'settle down'"
            ),
            emergencyHotline = "108",
            hospitalRequirement = "Hospital with 24/7 emergency CT/MRI & Stroke ICU"
        ),
        EmergencyTriageItem(
            id = "road_trauma",
            title = "Road Accident / Heavy Bleeding / Polytrauma",
            hindiTitle = "सड़क दुर्घटना / गंभीर चोट व रक्तस्राव",
            requiredSpecialty = "Trauma Surgeon",
            urgencyLevel = UrgencyLevel.CODE_RED,
            goldenHourMinutes = 60,
            recommendedDoctorTitle = "Trauma Surgeon / Orthopedic Surgeon",
            keySymptoms = listOf(
                "Active arterial bleeding or deep gaping wounds",
                "Suspected bone fractures with deformity or inability to bear weight",
                "Head/spine injury, disorientation or unconsciousness"
            ),
            firstAidDos = listOf(
                "Call 108 (Ambulance) and 112 (Police & Emergency Transit)",
                "Apply direct firm pressure on bleeding wounds using clean cloth/gauze",
                "Keep victim's neck and spine straight; do NOT twist or pull forcefully",
                "Cover victim with a blanket to prevent hypothermia and shock"
            ),
            firstAidDonts = listOf(
                "Do NOT remove deeply embedded objects (glass, metal rods) from wounds",
                "Do NOT move the injured person unless there is immediate danger (fire/explosion)",
                "Do NOT administer water if victim is drowsy or vomiting"
            ),
            emergencyHotline = "108",
            hospitalRequirement = "Government District Hospital / Level-1/2 Trauma Center with Blood Bank"
        ),
        EmergencyTriageItem(
            id = "snake_bite",
            title = "Snake Bite / Envenomation (Big 4 Snakes)",
            hindiTitle = "सांप का काटना (सर्पदंश)",
            requiredSpecialty = "Emergency Physician",
            urgencyLevel = UrgencyLevel.CODE_RED,
            goldenHourMinutes = 120,
            recommendedDoctorTitle = "Emergency Physician / Toxicologist",
            keySymptoms = listOf(
                "Twin puncture fang marks, rapid swelling, burning pain",
                "Ptosis (drooping eyelids), double vision, breathing difficulty (Neurotoxic)",
                "Spontaneous bleeding from gums, nose or bite site (Hemotoxic)"
            ),
            firstAidDos = listOf(
                "Rush to nearest Govt District Hospital / CHC with Polyvalent Anti-Snake Venom (ASV)",
                "Reassure victim and keep them completely calm and immobile",
                "Immobilize the bitten limb with a splint/crepe bandage (like a fractured limb)",
                "Remove rings, anklets, bracelets before swelling begins"
            ),
            firstAidDonts = listOf(
                "Do NOT use a tight arterial tourniquet (causes tissue necrosis/gangrene)",
                "Do NOT cut, slash, suck, or burn the wound with blades or chemicals",
                "Do NOT waste precious time with traditional quacks/tantriks - ASV is the only cure"
            ),
            emergencyHotline = "108",
            hospitalRequirement = "District Govt Hospital or Medical College with Anti-Snake Venom (ASV) & Ventilator"
        ),
        EmergencyTriageItem(
            id = "respiratory_distress",
            title = "Severe Breathlessness / Asthma / Choking",
            hindiTitle = "सांस लेने में भारी तकलीफ / अस्थमा दौरा",
            requiredSpecialty = "Pulmonologist",
            urgencyLevel = UrgencyLevel.CODE_RED,
            goldenHourMinutes = 30,
            recommendedDoctorTitle = "Pulmonologist / Critical Care Intensivist",
            keySymptoms = listOf(
                "Gasping for air, inability to speak complete sentences",
                "Bluish discoloration of lips/fingertips (Cyanosis), SpO2 < 90%",
                "Severe wheezing, stridor, or sudden obstruction while eating"
            ),
            firstAidDos = listOf(
                "Sit the patient upright leaning slightly forward",
                "Administer reliever inhaler (Salbutamol/Asthalin) with spacer (2 to 4 puffs)",
                "If choking in adult: perform Heimlich maneuver (abdominal thrusts)",
                "Keep windows open for fresh ventilation, monitor pulse"
            ),
            firstAidDonts = listOf(
                "Do NOT lay patient flat on back (worsens breathing)",
                "Do NOT crowd around the patient",
                "Do NOT offer food or solid pills during acute respiratory distress"
            ),
            emergencyHotline = "108",
            hospitalRequirement = "Hospital with High-Flow Oxygen, Nebulization & ICU Ventilators"
        ),
        EmergencyTriageItem(
            id = "pediatric_emergency",
            title = "Pediatric High Fever / Seizures / Dehydration",
            hindiTitle = "बच्चों की आपात स्थिति / तेज बुखार के झटके",
            requiredSpecialty = "Pediatrician",
            urgencyLevel = UrgencyLevel.CODE_AMBER,
            goldenHourMinutes = 60,
            recommendedDoctorTitle = "Pediatrician / Pediatric Intensivist (PICU)",
            keySymptoms = listOf(
                "Febrile convulsions (child shaking uncontrollably with high fever)",
                "Lethargy, refusal to feed, sunken eyes, no wet diapers (severe dehydration)",
                "Persistent high fever (>103°F) not responding to paracetamol"
            ),
            firstAidDos = listOf(
                "During seizure: place child gently on side in recovery position on soft surface",
                "Tepid sponging with lukewarm water over forehead and neck to bring down temperature",
                "Keep airway clear; loosen tight clothing",
                "Take immediately to a hospital with Pediatric Care Unit"
            ),
            firstAidDonts = listOf(
                "Do NOT force spoons, keys, or fingers into the child's mouth during seizure",
                "Do NOT use cold ice water for sponging (causes shivering & rebound spike)",
                "Do NOT restrain shaking limbs vigorously"
            ),
            emergencyHotline = "102", // Janani Shishu Suraksha / Pediatric
            hospitalRequirement = "Maternal & Child Hospital / Hospital with PICU (Pediatric ICU)"
        ),
        EmergencyTriageItem(
            id = "pregnancy_labor",
            title = "High-Risk Pregnancy / Labor Complication",
            hindiTitle = "गर्भावस्था जटिलता / प्रसव आपातकाल",
            requiredSpecialty = "Gynecologist",
            urgencyLevel = UrgencyLevel.CODE_RED,
            goldenHourMinutes = 45,
            recommendedDoctorTitle = "Obstetrician & Gynecologist (OB/GYN)",
            keySymptoms = listOf(
                "Vaginal bleeding, severe abdominal pain or constant contractions",
                "Sudden severe headache, blurred vision, swelling of face/hands (Eclampsia)",
                "Rupture of membranes (water broke) or reduced fetal movements"
            ),
            firstAidDos = listOf(
                "Dial 102 (Janani Shishu Suraksha Ambulance - Free Govt Service) or 108",
                "Keep pregnant mother lying on her LEFT side to improve blood flow to baby",
                "Keep Mother and Child Protection (MCP) card / medical reports ready",
                "Stay calm and offer sips of water if she is not vomiting"
            ),
            firstAidDonts = listOf(
                "Do NOT ask mother to bear down unless instructed by a qualified doctor/nurse",
                "Do NOT give painkiller injections without prescription",
                "Do NOT travel via jerky two-wheelers or unpaved tracks if ambulance is en route"
            ),
            emergencyHotline = "102",
            hospitalRequirement = "Government Sub-District/District Hospital (FRU) or Private Maternity Center with OT & NICU"
        ),
        EmergencyTriageItem(
            id = "burns_acid",
            title = "Severe Burns / Scalds / Chemical & Acid Injury",
            hindiTitle = "गंभीर जलन / तेजाब व आग से झुलसना",
            requiredSpecialty = "Burn Specialist",
            urgencyLevel = UrgencyLevel.CODE_RED,
            goldenHourMinutes = 60,
            recommendedDoctorTitle = "Plastic & Reconstructive Surgeon / Burn Specialist",
            keySymptoms = listOf(
                "Deep 2nd or 3rd degree burns covering hands, face, joints or large area",
                "Chemical / acid contact causing deep stinging and tissue erosion",
                "Inhalation of smoke with soot in nose/mouth or hoarse voice"
            ),
            firstAidDos = listOf(
                "Cool the burn under gentle running tap water for at least 15-20 minutes",
                "Remove rings, tight belts and shoes before edema (swelling) develops",
                "Cover burn gently with clean, sterile, non-adherent cling wrap or clean cloth",
                "Keep patient warm and hydrated if conscious"
            ),
            firstAidDonts = listOf(
                "Do NOT apply toothpaste, butter, raw egg, turmeric or cow dung (causes severe infection)",
                "Do NOT burst blisters or peel off clothing stuck to the burnt skin",
                "Do NOT use ice directly on burns (causes further frostbite tissue damage)"
            ),
            emergencyHotline = "108",
            hospitalRequirement = "Dedicated Hospital Burn Ward / Tertiary Burn Center"
        ),
        EmergencyTriageItem(
            id = "acute_abdomen",
            title = "Severe Acute Abdomen / Appendicitis / Perforation",
            hindiTitle = "पेट में तीव्र असहनीय दर्द",
            requiredSpecialty = "General Surgeon",
            urgencyLevel = UrgencyLevel.CODE_AMBER,
            goldenHourMinutes = 180,
            recommendedDoctorTitle = "General & Laparoscopic Surgeon",
            keySymptoms = listOf(
                "Sudden rigid, board-like abdomen with severe tenderness",
                "Right lower abdominal pain worsening with cough/movement (Appendicitis)",
                "Repeated bilious vomiting, inability to pass stool or gas"
            ),
            firstAidDos = listOf(
                "Rush to surgical hospital or emergency OPD",
                "Keep patient in a position of comfort (knees bent towards chest)",
                "Carry all previous ultrasound, blood test or endoscopy reports"
            ),
            firstAidDonts = listOf(
                "Do NOT give food, tea, milk or painkiller pills (Keep 'Nil by Mouth' / NPO for urgent surgery)",
                "Do NOT give laxatives or enemas (can rupture inflamed appendix or bowel)",
                "Do NOT apply heating pads on the abdomen"
            ),
            emergencyHotline = "108",
            hospitalRequirement = "Hospital with Emergency Surgical Operation Theatre (OT) & Ultrasound"
        )
    )
}
