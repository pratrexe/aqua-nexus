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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquanexus.data.model.SystemMode
import com.example.aquanexus.ui.components.AiRecommendationBanner
import com.example.aquanexus.ui.components.MetricCard
import com.example.aquanexus.ui.components.WaterQualityCard
import com.example.aquanexus.ui.components.WaterTankVisualizer
import com.example.aquanexus.ui.theme.AquaPrimary
import com.example.aquanexus.ui.theme.CyanAccent
import com.example.aquanexus.ui.theme.EmeraldGreen
import com.example.aquanexus.ui.viewmodel.AquaNexusUiState

@Composable
fun DashboardScreen(
    uiState: AquaNexusUiState,
    onToggleSystemMode: () -> Unit,
    onToggleInletValve: (Boolean) -> Unit,
    onToggleRechargeValve: (Boolean) -> Unit,
    onToggleIrrigationPump: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val sensor = uiState.sensorData
    val actuator = uiState.actuatorState

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // System Mode Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AquaPrimary
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AQUA NEXUS",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Smart Rainwater & Groundwater Recharge",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        onClick = onToggleSystemMode
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (actuator.systemMode == SystemMode.AUTOMATIC) "AI AUTO" else "MANUAL",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // AI Insight Banner
        item {
            AiRecommendationBanner(aiInsights = uiState.aiInsights)
        }

        // Water Tank Visualizer
        item {
            WaterTankVisualizer(
                sensorData = sensor,
                isRecharging = actuator.rechargeValveOpen,
                isFirstFlush = actuator.firstFlushValveOpen
            )
        }

        // Telemetry Metrics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Rainfall Intensity",
                        value = String.format("%.1f", sensor.rainfallMmPerHour),
                        unit = "mm/h",
                        icon = Icons.Default.Thunderstorm,
                        iconColor = CyanAccent,
                        badgeText = if (sensor.rainfallMmPerHour > 0f) "Raining" else "Dry",
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Inflow Flow Rate",
                        value = String.format("%.1f", sensor.flowRateLmin),
                        unit = "L/min",
                        icon = Icons.Default.Water,
                        iconColor = AquaPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Soil Moisture",
                        value = "${sensor.soilMoisturePercent.toInt()}",
                        unit = "%",
                        icon = Icons.Default.Grass,
                        iconColor = EmeraldGreen,
                        badgeText = if (sensor.soilMoisturePercent < 30f) "Dry" else "Moist",
                        badgeColor = if (sensor.soilMoisturePercent < 30f) Color(0xFFF59E0B) else EmeraldGreen,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Total Harvested",
                        value = "${sensor.totalWaterHarvestedLiters.toInt()}",
                        unit = "L",
                        icon = Icons.Default.WaterDamage,
                        iconColor = Color(0xFF6366F1),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Actuator Switches Card
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
                        text = "Actuator Quick Controls",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    QuickSwitchRow(
                        title = "Tank Inlet Solenoid Valve",
                        isSwitchedOn = actuator.inletValveOpen,
                        onCheckChanged = onToggleInletValve
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    QuickSwitchRow(
                        title = "Groundwater Recharge Pit Valve",
                        isSwitchedOn = actuator.rechargeValveOpen,
                        onCheckChanged = onToggleRechargeValve
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    QuickSwitchRow(
                        title = "Smart Irrigation Pump",
                        isSwitchedOn = actuator.irrigationPumpOn,
                        onCheckChanged = onToggleIrrigationPump
                    )
                }
            }
        }

        // Water Quality Card
        item {
            WaterQualityCard(
                sensorData = sensor,
                qualityStatus = uiState.aiInsights.waterQualityStatus
            )
        }
    }
}

@Composable
private fun QuickSwitchRow(
    title: String,
    isSwitchedOn: Boolean,
    onCheckChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
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
