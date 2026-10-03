package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.MainScreen
import com.example.ui.theme.AarogyaRakshaTheme
import com.example.ui.viewmodel.AarogyaViewModel
import com.example.ui.viewmodel.AarogyaViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: AarogyaViewModel by viewModels {
        AarogyaViewModelFactory((application as AarogyaApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AarogyaRakshaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
