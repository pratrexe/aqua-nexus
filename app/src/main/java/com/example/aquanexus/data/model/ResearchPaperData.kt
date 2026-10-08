package com.example.aquanexus.data.model

object ResearchPaperData {
    const val TITLE = "AQUA NEXUS: AI-Powered Smart Rainwater Harvesting and Groundwater Recharge System"
    const val SUBTITLE = "An Original Scientific Investigation for National Children's Science Congress (NCSC)"
    const val AUTHORS = "Pratyush Rai & Khushi Meena"
    const val TEACHER_GUIDE = "Ajay Kumar Chaudhary"
    const val SCHOOL = "PM SHRI Kendriya Vidyalaya Tagore Garden, New Delhi"
    
    val ABSTRACT = """
        Water is one of the most valuable natural resources supporting human life, agriculture, industries, and ecosystems. Despite receiving considerable annual rainfall, many regions experience severe water scarcity due to inadequate rainwater harvesting systems, rapid urbanization, excessive groundwater extraction, and climate change.
        
        AQUA NEXUS introduces an Artificial Intelligence (AI) and Internet of Things (IoT) based smart rainwater harvesting and groundwater recharge system designed to maximize rainwater utilization through intelligent monitoring and automation. The system integrates rainfall sensors, ultrasonic water level sensors, flow monitoring, soil moisture analysis, pH and turbidity water quality evaluation, weather forecasting APIs, and cloud analytics.
        
        The system automatically monitors rainfall events, diverts initial roof dust using a smart first-flush valve, filters clean water into storage, and automatically diverts excess water into underground recharge pits whenever tank capacity reaches optimum levels. AI algorithms analyze historical rainfall records and weather forecasts to optimize storage and irrigation scheduling.
    """.trimIndent()

    val KEYWORDS = listOf(
        "Rainwater Harvesting",
        "Artificial Intelligence",
        "Internet of Things (IoT)",
        "Groundwater Recharge",
        "Water Conservation",
        "Smart Water Management",
        "ESP32 Microcontroller",
        "Environmental Monitoring"
    )

    val SYSTEM_LAYERS = listOf(
        "Layer 1 - Data Acquisition Layer" to "Continuously measures rainfall intensity (mm/hr), ultrasonic tank level (%), flow rate (L/min), soil moisture (%), pH, turbidity, TDS, and water temperature using physical sensors connected to an ESP32 microcontroller.",
        "Layer 2 - Communication Layer" to "Transmits real-time sensor metrics via Wi-Fi/GSM to Firebase Cloud Database, enabling seamless sync between physical actuators and the mobile app.",
        "Layer 3 - Intelligence Layer (AI Engine)" to "Uses predictive analytics to forecast incoming rainfall, estimate water demand, detect pipeline leaks/anomalies, evaluate water quality, and optimize groundwater recharge strategies.",
        "Layer 4 - Automation Layer" to "Controls physical solenoid valves and relay pumps: Automated First-Flush diversion, Tank Inlet Valve control, Groundwater Recharge Pit Valve opening, and Soil Moisture-driven Smart Irrigation.",
        "Layer 5 - Presentation Layer" to "An intuitive Android application providing live telemetry, visual tank level indicators, interactive rooftop harvest calculators, actuator controls, and NCSC scientific documentation."
    )

    val HARDWARE_COMPONENTS = listOf(
        "ESP32 Wi-Fi Microcontroller" to "Central processing unit with low power consumption and dual-core wireless IoT capability.",
        "Ultrasonic Water Level Sensor (HC-SR04 / JSN-SR04T)" to "Measures distance to water surface for precise storage volume calculations.",
        "Rainfall Sensor Module" to "Detects rainfall onset and intensity.",
        "YF-S201 Water Flow Sensor" to "Measures real-time flow rate in Liters/min and total volume harvested.",
        "Capacitive Soil Moisture Sensor" to "Evaluates soil water content for automated irrigation scheduling.",
        "pH & Turbidity Sensors" to "Monitors water purity and cleanliness before storage.",
        "Servo Motor / Solenoid Valves" to "Automated physical valves for First-Flush, Tank Inlet, and Recharge Pit diversion.",
        "Relay Module & Water Pump" to "Drives irrigation pump based on soil moisture levels."
    )

    val MATHEMATICAL_FORMULA = """
        Rooftop Rainwater Harvesting Formula:
        
        V = A × P × C × E
        
        Where:
        • V = Volume of harvested water (Liters)
        • A = Rooftop catchment area (m²)
        • P = Total rainfall (mm)
        • C = Runoff coefficient (0.80 - 0.95 for concrete rooftops)
        • E = Filter & collection efficiency (typically 0.85 - 0.90)
        
        Groundwater Recharge Estimation:
        Recharge Volume = Overflow Volume + Direct Recharge Pit Diversion
        (Replenishes localized aquifers and prevents land subsidence)
    """.trimIndent()
}
