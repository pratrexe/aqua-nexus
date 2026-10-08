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
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquanexus.ui.components.AiRecommendationBanner
import com.example.aquanexus.ui.theme.AquaPrimary
import com.example.aquanexus.ui.theme.CyanAccent
import com.example.aquanexus.ui.theme.EmeraldGreen
import com.example.aquanexus.ui.viewmodel.AquaNexusUiState

@Composable
fun SimulationScreen(
    uiState: AquaNexusUiState,
    onToggleSimulation: (Boolean) -> Unit,
    onUpdateSimulatedSensors: (Float?, Float?, Float?, Float?, Float?) -> Unit,
    modifier: Modifier = Modifier
) {
    val sensor = uiState.sensorData
    val isRunning = uiState.isSimulationRunning

    var simRainfall by remember(sensor.rainfallMmPerHour) { mutableFloatStateOf(sensor.rainfallMmPerHour) }
    var simTankLevel by remember(sensor.tankLevelPercent) { mutableFloatStateOf(sensor.tankLevelPercent) }
    var simSoilMoisture by remember(sensor.soilMoisturePercent) { mutableFloatStateOf(sensor.soilMoisturePercent) }
    var simPh by remember(sensor.phLevel) { mutableFloatStateOf(sensor.phLevel) }
    var simTurbidity by remember(sensor.turbidityNtu) { mutableFloatStateOf(sensor.turbidityNtu) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Text(
                text = "IoT Hardware & Sensor Sandbox",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Simulate ESP32 physical inputs to test real-time AI response",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Live Simulation Status Toggle
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isRunning) EmeraldGreen else Color(0xFF64748B)
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
                            imageVector = Icons.Default.DeveloperBoard,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isRunning) "ESP32 Live Telemetry Active" else "Simulation Paused",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isRunning) "System is processing simulated environmental ticks" else "Manual sliders active for static scenario testing",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Switch(
                        checked = isRunning,
                        onCheckedChange = onToggleSimulation,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = EmeraldGreen,
                            checkedTrackColor = Color.White,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }
            }
        }

        // AI Insight Banner Feedback
        item {
            AiRecommendationBanner(aiInsights = uiState.aiInsights)
        }

        // Interactive Sliders Sandbox Card
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
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = AquaPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Physical Sensor Inputs Sandbox",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 1. Rainfall Sensor Slider
                    SimSliderRow(
                        label = "Rainfall Intensity",
                        valueText = "${String.format("%.1f", simRainfall)} mm/h",
                        value = simRainfall,
                        range = 0f..50f,
                        thumbColor = CyanAccent,
                        onValueChange = {
                            simRainfall = it
                            onUpdateSimulatedSensors(it, null, null, null, null)
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. Ultrasonic Tank Level Slider
                    SimSliderRow(
                        label = "Water Tank Level",
                        valueText = "${simTankLevel.toInt()}%",
                        value = simTankLevel,
                        range = 0f..100f,
                        thumbColor = AquaPrimary,
                        onValueChange = {
                            simTankLevel = it
                            onUpdateSimulatedSensors(null, it, null, null, null)
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Soil Moisture Slider
                    SimSliderRow(
                        label = "Capacitive Soil Moisture",
                        valueText = "${simSoilMoisture.toInt()}%",
                        value = simSoilMoisture,
                        range = 0f..100f,
                        thumbColor = EmeraldGreen,
                        onValueChange = {
                            simSoilMoisture = it
                            onUpdateSimulatedSensors(null, null, it, null, null)
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Water pH Slider
                    SimSliderRow(
                        label = "Water pH Level",
                        valueText = String.format("%.1f", simPh),
                        value = simPh,
                        range = 0f..14f,
                        thumbColor = Color(0xFF8B5CF6),
                        onValueChange = {
                            simPh = it
                            onUpdateSimulatedSensors(null, null, null, it, null)
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 5. Water Turbidity Slider
                    SimSliderRow(
                        label = "Water Turbidity",
                        valueText = "${String.format("%.1f", simTurbidity)} NTU",
                        value = simTurbidity,
                        range = 0f..20f,
                        thumbColor = Color(0xFFF59E0B),
                        onValueChange = {
                            simTurbidity = it
                            onUpdateSimulatedSensors(null, null, null, null, it)
                        }
                    )
                }
            }
        }

        // Quick Preset Simulation Scenarios
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
                        text = "Quick Test Scenarios",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onUpdateSimulatedSensors(35f, 92f, 40f, 7.2f, 2.0f)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AquaPrimary),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Heavy Rain & Recharge", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                onUpdateSimulatedSensors(0f, 20f, 18f, 7.1f, 1.5f)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Dry Soil Irrigation", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SimSliderRow(
    label: String,
    valueText: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    thumbColor: Color,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = valueText,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = thumbColor
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = thumbColor,
                activeTrackColor = thumbColor
            )
        )
    }
}
