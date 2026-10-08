package com.example.aquanexus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquanexus.data.model.ActuatorState
import com.example.aquanexus.data.model.AiInsights
import com.example.aquanexus.data.model.HarvestingCalculation
import com.example.aquanexus.data.model.HistoricalDataPoint
import com.example.aquanexus.data.model.SensorData
import com.example.aquanexus.data.repository.AquaNexusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AquaNexusUiState(
    val sensorData: SensorData = SensorData(),
    val actuatorState: ActuatorState = ActuatorState(),
    val aiInsights: AiInsights = AiInsights(),
    val historicalData: List<HistoricalDataPoint> = emptyList(),
    val isSimulationRunning: Boolean = true,
    val calculation: HarvestingCalculation = HarvestingCalculation()
)

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class AquaNexusViewModel(
    private val repository: AquaNexusRepository = AquaNexusRepository()
) : ViewModel() {

    private val _calculationState = MutableStateFlow(HarvestingCalculation())
    val calculationState: StateFlow<HarvestingCalculation> = _calculationState.asStateFlow()

    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("Hello! I am your AQUA NEXUS Groq AI Assistant powered by gpt-oss/llama3. How can I help you with your rainwater harvesting, groundwater recharge, or system telemetry today?", isUser = false)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    val uiState: StateFlow<AquaNexusUiState> = combine(
        repository.sensorData,
        repository.actuatorState,
        repository.aiInsights,
        repository.historicalData,
        combine(repository.isSimulationRunning, _calculationState) { simRunning, calc -> simRunning to calc }
    ) { sensor, actuator, ai, history, simCalc ->
        AquaNexusUiState(
            sensorData = sensor,
            actuatorState = actuator,
            aiInsights = ai,
            historicalData = history,
            isSimulationRunning = simCalc.first,
            calculation = simCalc.second
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AquaNexusUiState()
    )

    fun sendUserChatMessage(userQuery: String) {
        if (userQuery.isBlank()) return

        val userMsg = ChatMessage(userQuery, isUser = true)
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val responseText = repository.askWaterAssistant(userQuery)
            _chatMessages.value = _chatMessages.value + ChatMessage(responseText, isUser = false)
            _isAiThinking.value = false
        }
    }

    fun toggleSystemMode() {
        repository.toggleSystemMode()
    }

    fun toggleInletValve(open: Boolean) {
        repository.toggleInletValve(open)
    }

    fun toggleFirstFlushValve(open: Boolean) {
        repository.toggleFirstFlushValve(open)
    }

    fun toggleRechargeValve(open: Boolean) {
        repository.toggleRechargeValve(open)
    }

    fun toggleIrrigationPump(on: Boolean) {
        repository.toggleIrrigationPump(on)
    }

    fun toggleSimulation(running: Boolean) {
        repository.toggleSimulationRunning(running)
    }

    fun updateSimulatedSensors(
        rainfall: Float? = null,
        tankLevel: Float? = null,
        soilMoisture: Float? = null,
        ph: Float? = null,
        turbidity: Float? = null
    ) {
        repository.updateSimulatedSensors(rainfall, tankLevel, soilMoisture, ph, turbidity)
    }

    fun updateCalculation(
        areaSqM: Float,
        rainfallMm: Float,
        runoffCoeff: Float,
        filterEfficiency: Float
    ) {
        _calculationState.value = HarvestingCalculation(
            rooftopAreaSqMeters = areaSqM,
            rainfallMm = rainfallMm,
            runoffCoefficient = runoffCoeff,
            filterEfficiency = filterEfficiency
        )
    }
}
