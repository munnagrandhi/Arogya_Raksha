package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.TirangaGreen
import com.example.ui.theme.TirangaSaffron

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AarogyaTopAppBar(
    currentRole: UserRole,
    userName: String = "",
    onRoleClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSosClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(EmergencyRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Emergency,
                        contentDescription = "Aarogya Raksha",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Aarogya Raksha",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        // Mini tricolor
                        Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                            Box(modifier = Modifier.size(3.dp, 8.dp).background(TirangaSaffron))
                            Box(modifier = Modifier.size(3.dp, 8.dp).background(Color.White))
                            Box(modifier = Modifier.size(3.dp, 8.dp).background(TirangaGreen))
                        }
                    }
                    Text(
                        text = if (userName.isNotBlank()) userName else "Emergency Doctor & Hospital Network",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )
                }
            }
        },
        actions = {
            // Role switcher pill button
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = when (currentRole) {
                    UserRole.CITIZEN -> Color(0xFFEFF6FF)
                    UserRole.DOCTOR -> Color(0xFFF0FDF4)
                    UserRole.GOVT_OFFICER -> Color(0xFFFEF2F2)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onRoleClick() }
                    .testTag("role_switcher_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentRole.displayName.split(" ").first(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (currentRole) {
                            UserRole.CITIZEN -> Color(0xFF1D4ED8)
                            UserRole.DOCTOR -> TirangaGreen
                            UserRole.GOVT_OFFICER -> EmergencyRed
                        }
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Switch Portal",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            IconButton(
                onClick = onLogoutClick,
                modifier = Modifier.testTag("topbar_logout_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Sign Out / Switch Login",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(19.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
