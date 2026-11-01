package com.example.bandamap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bandamap.navigation.AppNavigation
import com.example.bandamap.ui.theme.BandaMapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BandaMapTheme {
                AppNavigation()
            }
        }
    }
}