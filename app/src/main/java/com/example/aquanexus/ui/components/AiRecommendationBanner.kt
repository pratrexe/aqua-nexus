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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Warning
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
import com.example.aquanexus.data.model.AiInsights
import com.example.aquanexus.ui.theme.AquaDark
import com.example.aquanexus.ui.theme.AquaPrimary

@Composable
fun AiRecommendationBanner(
    aiInsights: AiInsights,
    modifier: Modifier = Modifier
) {
    val cardBg = if (aiInsights.hasLeakDetected) {
        Color(0xFFFEF2F2)
    } else {
        Color(0xFFF0F9FF)
    }

    val contentColor = if (aiInsights.hasLeakDetected) {
        Color(0xFF991B1B)
    } else {
        AquaDark
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (aiInsights.hasLeakDetected) Color(0xFFFEE2E2) else AquaPrimary.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (aiInsights.hasLeakDetected) Icons.Default.Warning else Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = if (aiInsights.hasLeakDetected) Color(0xFFDC2626) else AquaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (aiInsights.hasLeakDetected) "Leak Anomaly Detected" else "AI Intelligence Engine",
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                        fontSize = 15.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AquaPrimary.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Thunderstorm,
                            contentDescription = null,
                            tint = AquaPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Rain in ${aiInsights.rainExpectedInHours}h",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AquaPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = aiInsights.aiRecommendation,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor.copy(alpha = 0.9f),
                lineHeight = 18.sp
            )
        }
    }
}
