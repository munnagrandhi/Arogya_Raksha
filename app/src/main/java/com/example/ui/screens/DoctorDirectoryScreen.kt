package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DoctorEntity
import com.example.ui.components.DoctorCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDirectoryScreen(
    doctors: List<DoctorEntity>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedSpecialty: String,
    onSpecialtyChange: (String) -> Unit,
    selectedType: String,
    onTypeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val specialties = listOf(
        "ALL" to "All Specialties",
        "Cardiologist" to "Cardiology",
        "Neurologist" to "Neurology",
        "Trauma Surgeon" to "Trauma & Ortho",
        "Emergency Physician" to "Emergency / ASV",
        "Pulmonologist" to "Pulmonology",
        "Gynecologist" to "OB/GYN",
        "Pediatrician" to "Pediatrics",
        "General Surgeon" to "General Surgery",
        "Burn Specialist" to "Burn Specialist"
    )

    val typeTabs = listOf(
        "ALL" to "All Doctors",
        "DUAL_PRACTICE" to "Dual-Practice (Hospital + Clinic)",
        "GOVT_DOCTOR" to "Govt Doctors",
        "PRIVATE_DOCTOR" to "Private Specialists"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("doctor_directory_screen")
    ) {
        // Search & Filter header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Registered Doctors & Clinics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Find NMC verified doctors with dual Hospital & Private Clinic hours",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("doctor_search_bar"),
                placeholder = { Text("Search doctor, clinic, hospital or specialty...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = true
            )
        }

        // Practice Type Tab Row (Highlighting Dual Practice)
        TabRow(
            selectedTabIndex = typeTabs.indexOfFirst { it.first == selectedType }.coerceAtLeast(0),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            typeTabs.forEach { (typeKey, label) ->
                Tab(
                    selected = selectedType == typeKey,
                    onClick = { onTypeChange(typeKey) },
                    text = {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (selectedType == typeKey) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                )
            }
        }

        // Specialty Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            specialties.forEach { (specKey, label) ->
                FilterChip(
                    selected = selectedSpecialty == specKey,
                    onClick = { onSpecialtyChange(specKey) },
                    label = { Text(label, fontSize = 12.sp) },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }

        // Doctors List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("doctors_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (doctors.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No matching doctors found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Try clearing search filters or changing specialty",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                items(doctors, key = { it.id }) { doctor ->
                    DoctorCard(doctor = doctor)
                }
            }
        }
    }
}
