package com.example.aquanexus.data.model

/**
 * System Operational Mode
 */
enum class SystemMode {
    AUTOMATIC,
    MANUAL
}

/**
 * Water Quality Health Status
 */
enum class QualityStatus(val label: String) {
    OPTIMAL("Optimal / Safe"),
    MODERATE("Moderate / Filter Needed"),
    POOR("Poor / Contaminated")
}

/**
 * Real-time IoT Sensor Telemetry Data
 */
data class SensorData(
    val rainfallMmPerHour: Float = 12.5f,        // mm/hr
    val tankLevelPercent: Float = 68f,           // 0 - 100 %
    val tankCurrentLiters: Float = 3400f,        // Liters
    val tankMaxCapacityLiters: Float = 5000f,    // Liters
    val flowRateLmin: Float = 18.2f,             // Liters/min
    val totalWaterHarvestedLiters: Float = 14250f, // Cumulative Liters
    val groundwaterRechargedLiters: Float = 8900f, // Cumulative Liters
    val soilMoisturePercent: Float = 42f,        // 0 - 100 %
    val phLevel: Float = 7.2f,                   // 0 - 14 pH
    val turbidityNtu: Float = 2.4f,              // NTU (Nephelometric Turbidity Units)
    val tdsPpm: Float = 145f,                    // Total Dissolved Solids (ppm)
    val temperatureCelsius: Float = 26.5f       // °C
)

/**
 * Actuator States for Automated Valve & Pump Control
 */
data class ActuatorState(
    val systemMode: SystemMode = SystemMode.AUTOMATIC,
    val inletValveOpen: Boolean = true,
    val firstFlushValveOpen: Boolean = false,
    val rechargeValveOpen: Boolean = false,
    val irrigationPumpOn: Boolean = false,
    val isFirstFlushCompleted: Boolean = true,
    val firstFlushVolumeDivertedL: Float = 50f
)

/**
 * AI Engine Insights, Predictions, and Anomaly Detection
 */
data class AiInsights(
    val forecastedRainfallMm: Float = 35.0f,
    val rainExpectedInHours: Int = 2,
    val stormWarning: Boolean = true,
    val aiRecommendation: String = "Moderate rain storm expected in 2 hours. Pre-diverting first flush and ensuring groundwater recharge pit is primed.",
    val hasLeakDetected: Boolean = false,
    val leakLocation: String? = null,
    val waterQualityStatus: QualityStatus = QualityStatus.OPTIMAL,
    val dailyHarvestForecastLiters: Float = 1850f,
    val estimatedWaterSavingsRupees: Float = 1420f
)

/**
 * Rooftop Water Harvest Calculation Model
 * V = Area (m²) * Rainfall (mm) * Runoff Coefficient * Filter Efficiency
 */
data class HarvestingCalculation(
    val rooftopAreaSqMeters: Float = 120f,
    val rainfallMm: Float = 50f,
    val runoffCoefficient: Float = 0.85f, // 0.80 - 0.90 for concrete roof
    val filterEfficiency: Float = 0.90f   // 90% efficiency
) {
    val potentialHarvestLiters: Float
        get() = rooftopAreaSqMeters * rainfallMm * runoffCoefficient * filterEfficiency
}

/**
 * Historical Data Point for Analytics Charts
 */
data class HistoricalDataPoint(
    val dayLabel: String,
    val harvestedLiters: Float,
    val rechargedLiters: Float,
    val rainfallMm: Float
)
