package com.example.aquanexus.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquanexus.data.model.SensorData
import com.example.aquanexus.ui.theme.AquaPrimary
import com.example.aquanexus.ui.theme.AquaPrimaryLight
import com.example.aquanexus.ui.theme.CyanAccent
import com.example.aquanexus.ui.theme.EmeraldGreen
import kotlin.math.sin

@Composable
fun WaterTankVisualizer(
    sensorData: SensorData,
    isRecharging: Boolean,
    isFirstFlush: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedLevel = remember { Animatable(sensorData.tankLevelPercent) }
    LaunchedEffect(sensorData.tankLevelPercent) {
        animatedLevel.animateTo(
            targetValue = sensorData.tankLevelPercent,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    val waveOffset = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        waveOffset.animateTo(
            targetValue = 2f * Math.PI.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = AquaPrimary.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = AquaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Smart Water Tank",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cap: ${sensorData.tankMaxCapacityLiters.toInt()} Liters",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                // Status Chip
                val (statusText, statusColor) = when {
                    isFirstFlush -> "First-Flush Active" to Color(0xFFF59E0B)
                    isRecharging -> "Recharging Aquifer" to EmeraldGreen
                    sensorData.tankLevelPercent >= 90f -> "Tank Full (Diverting)" to EmeraldGreen
                    else -> "Harvesting Active" to AquaPrimary
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Tank Canvas Visualizer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0F172A).copy(alpha = 0.05f))
                    .border(2.dp, AquaPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val level = (animatedLevel.value / 100f).coerceIn(0f, 1f)
                    val waterTop = height * (1f - level)

                    // Draw Water Body with Wave
                    val path = Path().apply {
                        moveTo(0f, height)
                        lineTo(0f, waterTop)

                        val waveAmplitude = 12.dp.toPx()
                        val waveFrequency = 2f * Math.PI / width

                        for (x in 0..width.toInt() step 5) {
                            val y = waterTop + (waveAmplitude * sin(waveFrequency * x + waveOffset.value)).toFloat()
                            lineTo(x.toFloat(), y)
                        }

                        lineTo(width, height)
                        close()
                    }

                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(CyanAccent, AquaPrimaryLight, AquaPrimary)
                        )
                    )
                }

                // Tank Overlay Information Text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "${animatedLevel.value.toInt()}%",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (animatedLevel.value > 50f) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${sensorData.tankCurrentLiters.toInt()} / ${sensorData.tankMaxCapacityLiters.toInt()} L",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (animatedLevel.value > 50f) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TankDetailItem(
                    icon = Icons.Default.Opacity,
                    label = "Inflow Rate",
                    value = "${String.format("%.1f", sensorData.flowRateLmin)} L/min"
                )
                TankDetailItem(
                    icon = Icons.Default.Sync,
                    label = "Groundwater Recharged",
                    value = "${sensorData.groundwaterRechargedLiters.toInt()} L"
                )
            }
        }
    }
}

@Composable
private fun TankDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AquaPrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
