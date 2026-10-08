package com.example.aquanexus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquanexus.data.model.SystemMode
import com.example.aquanexus.ui.theme.AquaDark
import com.example.aquanexus.ui.theme.AquaPrimary
import com.example.aquanexus.ui.theme.EmeraldGreen
import com.example.aquanexus.ui.viewmodel.AquaNexusUiState

@Composable
fun ControlsScreen(
    uiState: AquaNexusUiState,
    onToggleSystemMode: () -> Unit,
    onToggleInletValve: (Boolean) -> Unit,
    onToggleFirstFlushValve: (Boolean) -> Unit,
    onToggleRechargeValve: (Boolean) -> Unit,
    onToggleIrrigationPump: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val actuator = uiState.actuatorState
    val isAuto = actuator.systemMode == SystemMode.AUTOMATIC

    var soilThreshold by remember { mutableFloatStateOf(30f) }
    var rechargeThreshold by remember { mutableFloatStateOf(88f) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Text(
                text = "Automation & Valve Actuators",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Configure smart rules & physical relay switches",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Mode Switch Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAuto) AquaPrimary else Color(0xFF1E293B)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoMode,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isAuto) "AI Automatic Mode Active" else "Manual Override Mode",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isAuto) "System optimizes valves & recharge automatically" else "User has manual override on all actuators",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Switch(
                        checked = isAuto,
                        onCheckedChange = { onToggleSystemMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AquaPrimary,
                            checkedTrackColor = Color.White,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }
            }
        }

        // Physical Actuators Control Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Physical Relay & Valve Controls",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (isAuto) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "(Auto mode enabled - switches indicate live AI decisions)",
                            fontSize = 11.sp,
                            color = AquaPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ActuatorRow(
                        icon = Icons.Default.WaterDrop,
                        title = "Main Inlet Solenoid Valve",
                        subtitle = "Controls rooftop rainwater collection into storage tank",
                        isSwitchedOn = actuator.inletValveOpen,
                        onCheckChanged = onToggleInletValve
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ActuatorRow(
                        icon = Icons.Default.Water,
                        title = "Automated First-Flush Valve",
                        subtitle = "Diverts initial dirty roof dust (50L) to waste drain",
                        isSwitchedOn = actuator.firstFlushValveOpen,
                        onCheckChanged = onToggleFirstFlushValve
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ActuatorRow(
                        icon = Icons.Default.Sync,
                        title = "Groundwater Recharge Pit Valve",
                        subtitle = "Diverts excess overflow water into recharge well",
                        isSwitchedOn = actuator.rechargeValveOpen,
                        onCheckChanged = onToggleRechargeValve
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ActuatorRow(
                        icon = Icons.Default.Grass,
                        title = "Smart Irrigation Water Pump",
                        subtitle = "Powers soil irrigation pump from harvested water storage",
                        isSwitchedOn = actuator.irrigationPumpOn,
                        onCheckChanged = onToggleIrrigationPump
                    )
                }
            }
        }

        // Automated Rule Threshold Configuration
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SettingsInputComponent,
                            contentDescription = null,
                            tint = AquaPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Automation Thresholds",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Soil Moisture Slider
                    Text(
                        text = "Irrigation Trigger Soil Moisture: ${soilThreshold.toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Triggers irrigation pump when soil moisture drops below this value",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Slider(
                        value = soilThreshold,
                        onValueChange = { soilThreshold = it },
                        valueRange = 10f..50f,
                        colors = SliderDefaults.colors(
                            thumbColor = EmeraldGreen,
                            activeTrackColor = EmeraldGreen
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tank Recharge Slider
                    Text(
                        text = "Groundwater Recharge Trigger Level: ${rechargeThreshold.toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Diverts excess water to groundwater pit when storage tank reaches this level",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Slider(
                        value = rechargeThreshold,
                        onValueChange = { rechargeThreshold = it },
                        valueRange = 70f..98f,
                        colors = SliderDefaults.colors(
                            thumbColor = AquaPrimary,
                            activeTrackColor = AquaPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ActuatorRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSwitchedOn: Boolean,
    onCheckChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = AquaPrimary.copy(alpha = 0.12f),
                modifier = Modifier.size(36.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AquaPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = isSwitchedOn,
            onCheckedChange = onCheckChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AquaPrimary
            )
        )
    }
}
