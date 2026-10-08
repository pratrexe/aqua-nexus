package com.example.aquanexus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aquanexus.ui.navigation.AquaNexusMainApp
import com.example.aquanexus.ui.theme.AquanexusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AquanexusTheme {
                AquaNexusMainApp()
            }
        }
    }
}
