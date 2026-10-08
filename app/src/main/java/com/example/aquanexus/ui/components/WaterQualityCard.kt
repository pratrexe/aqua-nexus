package com.example.aquanexus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquanexus.data.model.QualityStatus
import com.example.aquanexus.data.model.SensorData
import com.example.aquanexus.ui.theme.AquaPrimary
import com.example.aquanexus.ui.theme.EmeraldGreen

@Composable
fun WaterQualityCard(
    sensorData: SensorData,
    qualityStatus: QualityStatus,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                    Surface(
                        shape = CircleShape,
                        color = EmeraldGreen.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Water Quality Monitor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Real-time Purity Metrics",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = when (qualityStatus) {
                        QualityStatus.OPTIMAL -> EmeraldGreen.copy(alpha = 0.15f)
                        QualityStatus.MODERATE -> Color(0xFFF59E0B).copy(alpha = 0.15f)
                        QualityStatus.POOR -> Color(0xFFEF4444).copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = qualityStatus.label,
                        color = when (qualityStatus) {
                            QualityStatus.OPTIMAL -> EmeraldGreen
                            QualityStatus.MODERATE -> Color(0xFFF59E0B)
                            QualityStatus.POOR -> Color(0xFFEF4444)
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QualityMetricItem(
                    label = "pH Level",
                    value = String.format("%.1f", sensorData.phLevel),
                    unit = "pH",
                    status = if (sensorData.phLevel in 6.5f..8.5f) "Safe" else "Check"
                )
                QualityMetricItem(
                    label = "Turbidity",
                    value = String.format("%.1f", sensorData.turbidityNtu),
                    unit = "NTU",
                    status = if (sensorData.turbidityNtu < 5.0f) "Clear" else "Filter"
                )
                QualityMetricItem(
                    label = "TDS Level",
                    value = sensorData.tdsPpm.toInt().toString(),
                    unit = "ppm",
                    status = if (sensorData.tdsPpm < 300f) "Good" else "High"
                )
                QualityMetricItem(
                    label = "Water Temp",
                    value = String.format("%.1f", sensorData.temperatureCelsius),
                    unit = "°C",
                    status = "Normal"
                )
            }
        }
    }
}

@Composable
private fun QualityMetricItem(
    label: String,
    value: String,
    unit: String,
    status: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = " $unit",
                fontSize = 10.sp,
                color = AquaPrimary,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
        Text(
            text = status,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = EmeraldGreen
        )
    }
}
