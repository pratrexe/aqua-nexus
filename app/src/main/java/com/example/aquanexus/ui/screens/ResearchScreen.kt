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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquanexus.data.model.ResearchPaperData
import com.example.aquanexus.ui.theme.AquaDark
import com.example.aquanexus.ui.theme.AquaPrimary

@Composable
fun ResearchScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & Authors Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AquaPrimary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NCSC RESEARCH INVESTIGATION",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = ResearchPaperData.TITLE,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Submitted by: ${ResearchPaperData.AUTHORS}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Teacher Guide: ${ResearchPaperData.TEACHER_GUIDE}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        text = ResearchPaperData.SCHOOL,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Keywords Horizontal Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ResearchPaperData.KEYWORDS) { keyword ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AquaPrimary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = keyword,
                            color = AquaPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Abstract Card
        item {
            ResearchSectionCard(
                icon = Icons.Default.Book,
                title = "Abstract"
            ) {
                Text(
                    text = ResearchPaperData.ABSTRACT,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                )
            }
        }

        // 5 System Architecture Layers Card
        item {
            ResearchSectionCard(
                icon = Icons.Default.Architecture,
                title = "5-Layer System Architecture"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ResearchPaperData.SYSTEM_LAYERS.forEach { (layerName, layerDesc) ->
                        Column {
                            Text(
                                text = layerName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AquaPrimary
                            )
                            Text(
                                text = layerDesc,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        // Mathematical Model Card
        item {
            ResearchSectionCard(
                icon = Icons.Default.Functions,
                title = "Mathematical Harvesting Model"
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0F9FF),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = ResearchPaperData.MATHEMATICAL_FORMULA,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AquaDark,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }

        // Hardware Components Card
        item {
            ResearchSectionCard(
                icon = Icons.Default.DeveloperBoard,
                title = "Hardware & Sensors Specifications"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ResearchPaperData.HARDWARE_COMPONENTS.forEach { (compName, compDesc) ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "• ",
                                fontWeight = FontWeight.Bold,
                                color = AquaPrimary
                            )
                            Column {
                                Text(
                                    text = compName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = compDesc,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResearchSectionCard(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
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
                    imageVector = icon,
                    contentDescription = null,
                    tint = AquaPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
