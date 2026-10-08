# AQUA NEXUS 🌊
> **Next-Generation IoT-Driven Smart Rainwater Harvesting & Groundwater Recharge System**
>
> *Developed for the National Children's Science Congress (NCSC) by Pratyush Rai & Khushi Meena (Kendriya Vidyalaya Tagore Garden).*

---

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Groq AI](https://img.shields.io/badge/Groq%20Cloud%20AI-FF6F00?style=for-the-badge&logo=openai&logoColor=white)
![Build](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

---

## 📌 Project Overview

**AQUA NEXUS** is a state-of-the-art Android application designed to interface with an IoT smart rainwater harvesting and automated groundwater recharge system. The application processes real-time sensor telemetry (water tank levels, rainfall intensity, soil moisture, pH, turbidity, TDS) and uses **Groq Cloud AI** to make intelligent, automated water management decisions.

The project addresses urban water scarcity, groundwater depletion, and urban flooding through real-time telemetry, predictive mathematical modeling, automated actuator controls, and AI-driven conservation guidance.

---

## ✨ Key Features

- **📊 Live Dashboard & Sensor Telemetry**
  - Real-time tank capacity visualizer with animated liquid physics.
  - Telemetry cards for Rainfall rate, Inflow speed, Soil moisture, and Water Quality parameters (pH, Turbidity in NTU, TDS in PPM).
  - Instant AI recommendation banner based on current environmental conditions.

- **🤖 Groq Cloud AI Assistant**
  - Powered by high-speed Groq Llama AI models (`llama-3.3-70b-versatile`, `llama-3.1-8b-instant`, `llama3-70b-8192`).
  - Interactive chatbot for water conservation tips, hardware troubleshooting, and harvest calculations.
  - Resilience engine with automatic model fallback and error mitigation.

- **🎛️ Hardware Actuator & Relay Controls**
  - Manual and Automatic operation modes.
  - Actuator toggles for:
    - Main Inlet Solenoid Valve
    - First-Flush Diverter Valve
    - Groundwater Recharge Pit Injection Valve
    - Smart Irrigation Pump (soil moisture gated)

- **📈 Harvest Calculator & Analytics**
  - Mathematical modeling formula: $V = A \times P \times C_r \times \eta$
    - $A$: Rooftop Area ($m^2$)
    - $P$: Rainfall Amount ($mm$)
    - $C_r$: Runoff Coefficient ($0.85$)
    - $\eta$: Filter Efficiency ($0.90$)
  - Environmental & financial impact summaries (estimated monthly savings and municipal water conserved).

- **⛈️ Climate & Drought Simulation Engine**
  - Interactive scenario generator to test system behavior under extreme conditions:
    - Heavy Monsoon Downpour (Auto-flush & aquifer recharge active)
    - Summer Drought (Water conservation mode)
    - Contaminated First-Flush (Turbidity alert & auto-diverter)

- **📄 Scientific Research Repository**
  - Complete embedded NCSC science congress research project paper, methodology, circuit schematics, and experimental observation datasets.

---

## 🛠️ Architecture & Tech Stack

- **Language:** Kotlin 2.2
- **UI Toolkit:** Jetpack Compose with Material Design 3
- **Architecture Pattern:** MVVM (Model-View-ViewModel) + Clean Architecture
- **State Management:** Kotlin Coroutines & `StateFlow`
- **Network / API:** `HttpURLConnection` REST Client connecting to Groq Cloud API
- **Build System:** Android Gradle Plugin 9.4 (Gradle 8.13)
- **Minimum SDK:** Android 7.0 (API Level 24) | **Target SDK:** Android 15 (API Level 37)

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio Ladybug | 2024.2.1](https://developer.android.com/studio) or newer
- JDK 17 / Java 21
- Groq Cloud API Key (Free key from [Groq Console](https://console.groq.com/keys))

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/aquanexus.git
   cd aquanexus
   ```

2. **Configure API Keys**
   Copy the `.env.example` template to `.env` or edit `local.properties` in the root directory:

   *Option A: Using `.env` file (Recommended)*
   ```bash
   cp .env.example .env
   ```
   Open `.env` and insert your Groq API key:
   ```env
   GROQ_API_KEY=your_groq_api_key_here
   ```

   *Option B: Using `local.properties`*
   Add the following line to `local.properties`:
   ```properties
   GROQ_API_KEY=your_groq_api_key_here
   ```

   > **Note:** `.env` and `local.properties` are listed in `.gitignore` to prevent sensitive credentials from being committed to version control.

3. **Build & Run**
   - Open the project in **Android Studio**.
   - Sync Gradle project (`File > Sync Project with Gradle Files`).
   - Select an Android Virtual Device (AVD) or connected physical device.
   - Click **Run** (`Shift + F10`).

---

## 🔧 Resolving Groq API Errors (e.g. 404 / 401)

If you encounter an API error (e.g., `Groq API Error 404`):

1. **Check API Key:** Ensure your `GROQ_API_KEY` in `.env` or `local.properties` is valid and active on [Groq Console](https://console.groq.com/keys).
2. **Automatic Model Fallback:** AQUA NEXUS includes an automatic fallback chain:
   $$\text{llama-3.3-70b-versatile} \longrightarrow \text{llama-3.1-8b-instant} \longrightarrow \text{llama3-70b-8192}$$
   If a specific model ID is updated or unavailable, the system automatically tries the next valid model.
3. **Internet Connectivity:** Ensure your device/emulator has active internet access.

---

## 👥 Authors & Credits

- **Student Researchers:** Pratyush Rai & Khushi Meena
- **School:** Kendriya Vidyalaya Tagore Garden
- **Event:** National Children's Science Congress (NCSC) Project Presentation

---

## 📄 License

This project is open-source and released under the [MIT License](LICENSE).
