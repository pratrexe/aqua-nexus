package com.example.aquanexus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquanexus.data.model.HistoricalDataPoint
import com.example.aquanexus.ui.theme.AquaDark
import com.example.aquanexus.ui.theme.AquaPrimary
import com.example.aquanexus.ui.theme.EmeraldGreen
import com.example.aquanexus.ui.viewmodel.AquaNexusUiState

@Composable
fun AnalyticsScreen(
    uiState: AquaNexusUiState,
    onUpdateCalculation: (Float, Float, Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val history = uiState.historicalData
    val calc = uiState.calculation

    var areaInput by remember { mutableStateOf(calc.rooftopAreaSqMeters.toInt().toString()) }
    var rainInput by remember { mutableStateOf(calc.rainfallMm.toInt().toString()) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Text(
                text = "Analytics & Harvest Calculator",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Historical trends & predictive mathematical modeling",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Weekly Harvest vs Recharge Chart
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Analytics,
                                contentDescription = null,
                                tint = AquaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Weekly Harvest vs Recharge",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LegendItem(color = AquaPrimary, label = "Harvested")
                            Spacer(modifier = Modifier.width(10.dp))
                            LegendItem(color = EmeraldGreen, label = "Recharged")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Bar Chart Representation
                    val maxVal = (history.maxOfOrNull { it.harvestedLiters } ?: 3500f).coerceAtLeast(1000f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        history.forEach { item ->
                            BarChartColumn(
                                dataPoint = item,
                                maxVal = maxVal
                            )
                        }
                    }
                }
            }
        }

        // Rooftop Rainwater Harvest Calculator Card
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
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = AquaPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Rooftop Harvest Calculator",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Formula: V = Area (m²) × Rain (mm) × Runoff Coeff × Efficiency",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = areaInput,
                            onValueChange = {
                                areaInput = it
                                val area = it.toFloatOrNull() ?: 100f
                                val rain = rainInput.toFloatOrNull() ?: 50f
                                onUpdateCalculation(area, rain, 0.85f, 0.90f)
                            },
                            label = { Text("Roof Area (m²)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = rainInput,
                            onValueChange = {
                                rainInput = it
                                val area = areaInput.toFloatOrNull() ?: 100f
                                val rain = it.toFloatOrNull() ?: 50f
                                onUpdateCalculation(area, rain, 0.85f, 0.90f)
                            },
                            label = { Text("Rainfall (mm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Calculation Result Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE0F2FE)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Estimated Harvest Potential",
                                fontSize = 12.sp,
                                color = AquaDark
                            )
                            Text(
                                text = "${calc.potentialHarvestLiters.toInt()} Liters",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AquaPrimary
                            )
                            Text(
                                text = "Equivalent to replenishing localized aquifer by ~${(calc.potentialHarvestLiters * 0.6f).toInt()} L",
                                fontSize = 11.sp,
                                color = AquaDark.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        // Environmental & Economic Impact Summary Card
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
                        text = "Environmental & Economic Impact",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ImpactItem(
                            icon = Icons.Default.CurrencyRupee,
                            title = "Est. Water Savings",
                            value = "₹1,420 / mo"
                        )
                        ImpactItem(
                            icon = Icons.Default.Opacity,
                            title = "Municipal Water Saved",
                            value = "${uiState.sensorData.totalWaterHarvestedLiters.toInt()} L"
                        )
                        ImpactItem(
                            icon = Icons.Default.Psychology,
                            title = "AI Optimization Efficiency",
                            value = "+34.2%"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BarChartColumn(
    dataPoint: HistoricalDataPoint,
    maxVal: Float
) {
    val harvestHeightRatio = (dataPoint.harvestedLiters / maxVal).coerceIn(0.1f, 1f)
    val rechargeHeightRatio = (dataPoint.rechargedLiters / maxVal).coerceIn(0.1f, 1f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .fillMaxHeight(harvestHeightRatio)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(AquaPrimary)
            )
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .fillMaxHeight(rechargeHeightRatio)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(EmeraldGreen)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = dataPoint.dayLabel,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ImpactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AquaPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = title,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
