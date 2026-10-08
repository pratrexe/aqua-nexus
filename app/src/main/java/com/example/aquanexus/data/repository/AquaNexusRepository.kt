package com.example.aquanexus.data.repository

import com.example.aquanexus.data.model.ActuatorState
import com.example.aquanexus.data.model.AiInsights
import com.example.aquanexus.data.model.HistoricalDataPoint
import com.example.aquanexus.data.model.QualityStatus
import com.example.aquanexus.data.model.SensorData
import com.example.aquanexus.data.model.SystemMode
import com.example.aquanexus.data.remote.GroqAiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class AquaNexusRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.Default + Job())
    private val groqAiService = GroqAiService()

    private val _sensorData = MutableStateFlow(SensorData())
    val sensorData: StateFlow<SensorData> = _sensorData.asStateFlow()

    private val _actuatorState = MutableStateFlow(ActuatorState())
    val actuatorState: StateFlow<ActuatorState> = _actuatorState.asStateFlow()

    private val _aiInsights = MutableStateFlow(AiInsights())
    val aiInsights: StateFlow<AiInsights> = _aiInsights.asStateFlow()

    private val _isSimulationRunning = MutableStateFlow(true)
    val isSimulationRunning: StateFlow<Boolean> = _isSimulationRunning.asStateFlow()

    private val _historicalData = MutableStateFlow(
        listOf(
            HistoricalDataPoint("Mon", 1250f, 800f, 18f),
            HistoricalDataPoint("Tue", 2100f, 1400f, 32f),
            HistoricalDataPoint("Wed", 450f, 300f, 8f),
            HistoricalDataPoint("Thu", 3100f, 2200f, 45f),
            HistoricalDataPoint("Fri", 1800f, 1200f, 25f),
            HistoricalDataPoint("Sat", 2900f, 1900f, 40f),
            HistoricalDataPoint("Sun", 2650f, 1100f, 35f)
        )
    )
    val historicalData: StateFlow<List<HistoricalDataPoint>> = _historicalData.asStateFlow()

    private var simulationJob: Job? = null
    private var groqAiJob: Job? = null

    init {
        startSimulationLoop()
        startGroqAiRefreshLoop()
    }

    private fun startSimulationLoop() {
        simulationJob?.cancel()
        simulationJob = repositoryScope.launch {
            while (isActive) {
                delay(3000)
                if (_isSimulationRunning.value) {
                    tickSimulation()
                }
            }
        }
    }

    private fun startGroqAiRefreshLoop() {
        groqAiJob?.cancel()
        groqAiJob = repositoryScope.launch {
            // Initial AI query
            refreshGroqAiRecommendation()
            while (isActive) {
                delay(20000) // Refresh AI recommendation every 20 seconds
                refreshGroqAiRecommendation()
            }
        }
    }

    private suspend fun refreshGroqAiRecommendation() {
        try {
            val recommendation = groqAiService.getAiRecommendation(
                sensorData = _sensorData.value,
                actuatorState = _actuatorState.value
            )
            _aiInsights.value = _aiInsights.value.copy(
                aiRecommendation = recommendation
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun tickSimulation() {
        val currentSensor = _sensorData.value
        val currentActuator = _actuatorState.value

        // Slight realistic random fluctuation for simulation
        val newRainfall = if (Random.nextFloat() > 0.3f) (currentSensor.rainfallMmPerHour + (Random.nextFloat() * 2f - 1f)).coerceIn(0f, 50f) else 0f
        
        var newTankLevel = currentSensor.tankLevelPercent
        var newHarvested = currentSensor.totalWaterHarvestedLiters
        var newRecharged = currentSensor.groundwaterRechargedLiters
        var newFlowRate = currentSensor.flowRateLmin

        if (newRainfall > 2.0f && currentActuator.inletValveOpen) {
            newFlowRate = (newRainfall * 1.5f + Random.nextFloat() * 2f).coerceIn(5f, 40f)
            val addedLiters = newFlowRate * (3f / 60f) // 3 second tick
            
            if (newTankLevel < 90f) {
                newHarvested += addedLiters
                newTankLevel = ((currentSensor.tankCurrentLiters + addedLiters) / currentSensor.tankMaxCapacityLiters * 100f).coerceIn(0f, 100f)
            } else {
                // Tank > 90%: divert directly to Groundwater Recharge Pit!
                newRecharged += addedLiters
            }
        } else if (currentActuator.irrigationPumpOn) {
            newFlowRate = 12.0f
            val usedLiters = newFlowRate * (3f / 60f)
            val newCurrentLiters = (currentSensor.tankCurrentLiters - usedLiters).coerceAtLeast(0f)
            newTankLevel = (newCurrentLiters / currentSensor.tankMaxCapacityLiters * 100f).coerceIn(0f, 100f)
        } else {
            newFlowRate = 0f
        }

        val newCurrentLiters = (newTankLevel / 100f) * currentSensor.tankMaxCapacityLiters

        val updatedSensor = currentSensor.copy(
            rainfallMmPerHour = newRainfall,
            tankLevelPercent = newTankLevel,
            tankCurrentLiters = newCurrentLiters,
            flowRateLmin = newFlowRate,
            totalWaterHarvestedLiters = newHarvested,
            groundwaterRechargedLiters = newRecharged
        )

        _sensorData.value = updatedSensor

        // Evaluate AI & Automated Rules
        evaluateAutomationRules(updatedSensor, currentActuator)
    }

    private fun evaluateAutomationRules(sensor: SensorData, actuator: ActuatorState) {
        if (actuator.systemMode == SystemMode.AUTOMATIC) {
            var updatedActuator = actuator

            // Rule 1: High Tank Level -> Divert to Recharge Pit automatically
            val shouldRecharge = sensor.tankLevelPercent >= 88f
            if (updatedActuator.rechargeValveOpen != shouldRecharge) {
                updatedActuator = updatedActuator.copy(rechargeValveOpen = shouldRecharge)
            }

            // Rule 2: Low Soil Moisture -> Trigger Smart Irrigation Pump
            val shouldIrrigate = sensor.soilMoisturePercent < 30f && sensor.tankLevelPercent > 15f
            if (updatedActuator.irrigationPumpOn != shouldIrrigate) {
                updatedActuator = updatedActuator.copy(irrigationPumpOn = shouldIrrigate)
            }

            // Rule 3: First flush during heavy rainfall start
            if (sensor.rainfallMmPerHour > 15f && !actuator.isFirstFlushCompleted) {
                updatedActuator = updatedActuator.copy(
                    firstFlushValveOpen = true,
                    inletValveOpen = false
                )
            } else if (actuator.firstFlushValveOpen && sensor.rainfallMmPerHour > 5f) {
                // First flush finished
                updatedActuator = updatedActuator.copy(
                    firstFlushValveOpen = false,
                    isFirstFlushCompleted = true,
                    inletValveOpen = true
                )
            }

            _actuatorState.value = updatedActuator
        }

        // Evaluate AI Insights & Water Quality
        val quality = when {
            sensor.phLevel in 6.5f..8.5f && sensor.turbidityNtu < 5.0f && sensor.tdsPpm < 300f -> QualityStatus.OPTIMAL
            sensor.phLevel in 6.0f..9.0f && sensor.turbidityNtu < 10.0f -> QualityStatus.MODERATE
            else -> QualityStatus.POOR
        }

        val leakDetected = sensor.flowRateLmin > 5.0f && !actuator.inletValveOpen && !actuator.irrigationPumpOn

        _aiInsights.value = _aiInsights.value.copy(
            waterQualityStatus = quality,
            hasLeakDetected = leakDetected,
            leakLocation = if (leakDetected) "Distribution Pipe B2" else null
        )
    }

    suspend fun askWaterAssistant(userQuery: String): String {
        return groqAiService.askWaterAssistant(userQuery, _sensorData.value)
    }

    fun toggleSystemMode() {
        val newMode = if (_actuatorState.value.systemMode == SystemMode.AUTOMATIC) SystemMode.MANUAL else SystemMode.AUTOMATIC
        _actuatorState.value = _actuatorState.value.copy(systemMode = newMode)
        evaluateAutomationRules(_sensorData.value, _actuatorState.value)
    }

    fun toggleInletValve(open: Boolean) {
        _actuatorState.value = _actuatorState.value.copy(inletValveOpen = open)
    }

    fun toggleFirstFlushValve(open: Boolean) {
        _actuatorState.value = _actuatorState.value.copy(firstFlushValveOpen = open)
    }

    fun toggleRechargeValve(open: Boolean) {
        _actuatorState.value = _actuatorState.value.copy(rechargeValveOpen = open)
    }

    fun toggleIrrigationPump(on: Boolean) {
        _actuatorState.value = _actuatorState.value.copy(irrigationPumpOn = on)
    }

    fun toggleSimulationRunning(running: Boolean) {
        _isSimulationRunning.value = running
    }

    fun updateSimulatedSensors(
        rainfall: Float? = null,
        tankLevel: Float? = null,
        soilMoisture: Float? = null,
        ph: Float? = null,
        turbidity: Float? = null
    ) {
        val current = _sensorData.value
        val newRain = rainfall ?: current.rainfallMmPerHour
        val newTank = tankLevel ?: current.tankLevelPercent
        val newCurrentLiters = (newTank / 100f) * current.tankMaxCapacityLiters

        val updated = current.copy(
            rainfallMmPerHour = newRain,
            tankLevelPercent = newTank,
            tankCurrentLiters = newCurrentLiters,
            soilMoisturePercent = soilMoisture ?: current.soilMoisturePercent,
            phLevel = ph ?: current.phLevel,
            turbidityNtu = turbidity ?: current.turbidityNtu
        )
        _sensorData.value = updated
        evaluateAutomationRules(updated, _actuatorState.value)
    }
}
