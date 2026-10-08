package com.example.aquanexus.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquanexus.ui.screens.AiChatScreen
import com.example.aquanexus.ui.screens.AnalyticsScreen
import com.example.aquanexus.ui.screens.ControlsScreen
import com.example.aquanexus.ui.screens.DashboardScreen
import com.example.aquanexus.ui.screens.ResearchScreen
import com.example.aquanexus.ui.screens.SimulationScreen
import com.example.aquanexus.ui.theme.AquaPrimary
import com.example.aquanexus.ui.viewmodel.AquaNexusViewModel

enum class NavigationDestination(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    DASHBOARD("dashboard", "Home", Icons.Default.Dashboard),
    ANALYTICS("analytics", "Analytics", Icons.Default.Analytics),
    CONTROLS("controls", "Valves", Icons.Default.Tune),
    SIMULATION("simulation", "IoT Test", Icons.Default.DeveloperBoard),
    AI_CHAT("ai_chat", "Groq AI", Icons.Default.AutoAwesome),
    RESEARCH("research", "NCSC Paper", Icons.Default.Book)
}

@Composable
fun AquaNexusMainApp(
    viewModel: AquaNexusViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var currentDestination by remember { mutableStateOf(NavigationDestination.DASHBOARD) }

    val uiState by viewModel.uiState.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationDestination.entries.forEach { destination ->
                    val isSelected = currentDestination == destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentDestination = destination },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = destination.title,
                                fontSize = 10.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AquaPrimary,
                            selectedTextColor = AquaPrimary,
                            indicatorColor = AquaPrimary.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        when (currentDestination) {
            NavigationDestination.DASHBOARD -> DashboardScreen(
                uiState = uiState,
                onToggleSystemMode = { viewModel.toggleSystemMode() },
                onToggleInletValve = { viewModel.toggleInletValve(it) },
                onToggleRechargeValve = { viewModel.toggleRechargeValve(it) },
                onToggleIrrigationPump = { viewModel.toggleIrrigationPump(it) },
                modifier = modifier
            )

            NavigationDestination.ANALYTICS -> AnalyticsScreen(
                uiState = uiState,
                onUpdateCalculation = { area, rain, runoff, filter ->
                    viewModel.updateCalculation(area, rain, runoff, filter)
                },
                modifier = modifier
            )

            NavigationDestination.CONTROLS -> ControlsScreen(
                uiState = uiState,
                onToggleSystemMode = { viewModel.toggleSystemMode() },
                onToggleInletValve = { viewModel.toggleInletValve(it) },
                onToggleFirstFlushValve = { viewModel.toggleFirstFlushValve(it) },
                onToggleRechargeValve = { viewModel.toggleRechargeValve(it) },
                onToggleIrrigationPump = { viewModel.toggleIrrigationPump(it) },
                modifier = modifier
            )

            NavigationDestination.SIMULATION -> SimulationScreen(
                uiState = uiState,
                onToggleSimulation = { viewModel.toggleSimulation(it) },
                onUpdateSimulatedSensors = { rain, tank, soil, ph, turb ->
                    viewModel.updateSimulatedSensors(rain, tank, soil, ph, turb)
                },
                modifier = modifier
            )

            NavigationDestination.AI_CHAT -> AiChatScreen(
                chatMessages = chatMessages,
                isAiThinking = isAiThinking,
                onSendMessage = { viewModel.sendUserChatMessage(it) },
                modifier = modifier
            )

            NavigationDestination.RESEARCH -> ResearchScreen(
                modifier = modifier
            )
        }
    }
}
