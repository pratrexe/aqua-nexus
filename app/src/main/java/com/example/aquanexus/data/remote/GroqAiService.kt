package com.example.aquanexus.data.remote

import com.example.aquanexus.BuildConfig
import com.example.aquanexus.data.model.ActuatorState
import com.example.aquanexus.data.model.SensorData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class GroqAiService(
    private val apiKey: String = BuildConfig.GROQ_API_KEY
) {

    private val apiUrl = "https://api.groq.com/openai/v1/chat/completions"

    // Candidate models in priority order for resilience against model deprecations/404s
    private val candidateModels = listOf(
        "llama-3.3-70b-versatile",
        "llama-3.1-8b-instant",
        "llama3-70b-8192"
    )

    suspend fun getAiRecommendation(
        sensorData: SensorData,
        actuatorState: ActuatorState
    ): String = withContext(Dispatchers.IO) {
        try {
            val systemPrompt = """
                You are AQUA NEXUS AI, an expert AI engine for smart rainwater harvesting and groundwater recharge systems.
                Analyze the provided real-time IoT sensor telemetry and return a concise, high-impact action recommendation (max 2 sentences).
                Focus on: Valve operations, first-flush diversion, groundwater recharge, and leak alerts if any.
            """.trimIndent()

            val userMessage = """
                Live Telemetry:
                - Rainfall: ${sensorData.rainfallMmPerHour} mm/h
                - Water Tank Level: ${sensorData.tankLevelPercent}% (${sensorData.tankCurrentLiters} / ${sensorData.tankMaxCapacityLiters} L)
                - Inflow Flow Rate: ${sensorData.flowRateLmin} L/min
                - Soil Moisture: ${sensorData.soilMoisturePercent}%
                - Water Quality: pH ${sensorData.phLevel}, Turbidity ${sensorData.turbidityNtu} NTU, TDS ${sensorData.tdsPpm} ppm
                - Actuator Status: Inlet Valve=${actuatorState.inletValveOpen}, Recharge Valve=${actuatorState.rechargeValveOpen}, Irrigation Pump=${actuatorState.irrigationPumpOn}

                Provide real-time AI recommendation and system directive.
            """.trimIndent()

            callGroqApi(systemPrompt, userMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            "AI Engine (Offline fallback): " + when {
                sensorData.tankLevelPercent >= 88f -> "Tank near capacity (>=88%). Excess rainwater automatically diverted to groundwater recharge pit."
                sensorData.rainfallMmPerHour > 15f -> "Heavy rainfall active. First-flush cleared; storage tank filling at optimal efficiency."
                sensorData.soilMoisturePercent < 30f -> "Soil moisture low (${sensorData.soilMoisturePercent.toInt()}%). Smart irrigation pump active."
                else -> "System operating under AI optimization. All environmental sensors normal."
            }
        }
    }

    suspend fun askWaterAssistant(
        userQuery: String,
        sensorData: SensorData
    ): String = withContext(Dispatchers.IO) {
        try {
            val systemPrompt = """
                You are the AQUA NEXUS AI Assistant created for the National Children's Science Congress (NCSC) project by Pratyush Rai & Khushi Meena (Kendriya Vidyalaya Tagore Garden).
                You help users with rainwater harvesting calculations, groundwater recharge methods, ESP32 IoT sensors, and environmental water conservation.
                Keep responses informative, direct, concise, and helpful.
            """.trimIndent()

            val userMessage = """
                Current Tank Level: ${sensorData.tankLevelPercent.toInt()}%, Rainfall: ${sensorData.rainfallMmPerHour} mm/h, Soil Moisture: ${sensorData.soilMoisturePercent.toInt()}%.
                User Question: $userQuery
            """.trimIndent()

            callGroqApi(systemPrompt, userMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            val errorMsg = e.message ?: "Network error"
            if (errorMsg.contains("404") || errorMsg.contains("API Key")) {
                "Groq AI connection error ($errorMsg). Please check your GROQ_API_KEY in .env or local.properties."
            } else {
                "Unable to connect to Groq AI network. Please check internet connection."
            }
        }
    }

    private fun callGroqApi(systemPrompt: String, userPrompt: String): String {
        if (apiKey.isBlank()) {
            throw IllegalArgumentException("Groq API key is missing. Please set GROQ_API_KEY in .env or local.properties.")
        }

        var lastException: Exception? = null

        for (model in candidateModels) {
            try {
                return executeSingleApiCall(model, apiKey, systemPrompt, userPrompt)
            } catch (e: Exception) {
                lastException = e
                System.err.println("Groq API call failed for model $model: ${e.message}")
            }
        }

        throw lastException ?: RuntimeException("All Groq API model attempts failed.")
    }

    private fun executeSingleApiCall(
        modelName: String,
        key: String,
        systemPrompt: String,
        userPrompt: String
    ): String {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Authorization", "Bearer $key")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true
        connection.connectTimeout = 8000
        connection.readTimeout = 12000

        val jsonBody = JSONObject().apply {
            put("model", modelName)
            put("temperature", 0.6)
            put("max_tokens", 250)

            val messages = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", systemPrompt)
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", userPrompt)
                })
            }
            put("messages", messages)
        }

        OutputStreamWriter(connection.outputStream).use { writer ->
            writer.write(jsonBody.toString())
            writer.flush()
        }

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val responseText = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
            val jsonResponse = JSONObject(responseText)
            val choices = jsonResponse.getJSONArray("choices")
            if (choices.length() > 0) {
                val message = choices.getJSONObject(0).getJSONObject("message")
                return message.getString("content").trim()
            }
        } else {
            val errorStream = connection.errorStream ?: connection.inputStream
            val errorText = if (errorStream != null) {
                BufferedReader(InputStreamReader(errorStream)).use { it.readText() }
            } else {
                "No error stream response"
            }
            System.err.println("Groq API Error ($responseCode) for model $modelName: $errorText")

            var parsedMessage = ""
            try {
                val errJson = JSONObject(errorText)
                if (errJson.has("error")) {
                    val errObj = errJson.getJSONObject("error")
                    parsedMessage = errObj.optString("message", errorText)
                }
            } catch (_: Exception) {
                parsedMessage = errorText
            }

            throw RuntimeException("HTTP $responseCode: ${parsedMessage.ifBlank { "Request failed" }}")
        }
        throw RuntimeException("Groq API empty choices response for model $modelName")
    }
}
