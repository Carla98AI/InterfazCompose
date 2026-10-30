package com.example.interfazcompose

import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.Manifest
import android.os.Build
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.interfazcompose.data.local.FittingRoomDatabase
import com.example.interfazcompose.data.remote.ApiProvider
import com.example.interfazcompose.data.repository.FittingRoomRepository
import com.example.interfazcompose.platform.OccupancyAlertWorker
import com.example.interfazcompose.platform.OccupancyNotifications
import com.example.interfazcompose.ui.screen.HomeScreen
import com.example.interfazcompose.ui.theme.InterfazComposeTheme
import com.example.interfazcompose.ui.viewmodel.FittingRoomViewModel
import com.example.interfazcompose.ui.viewmodel.FittingRoomViewModelFactory
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        OccupancyNotifications.createChannel(this)
        // Revisa ocupaciones prolongadas aunque la pantalla no esté abierta.
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "alertas-probadores",
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<OccupancyAlertWorker>(15, TimeUnit.MINUTES).build()
        )
        // La interfaz trabaja con el repositorio; este coordina Room y la API.
        val repository = FittingRoomRepository(FittingRoomDatabase.get(this).roomDao(), ApiProvider.api)
        val viewModel = ViewModelProvider(this, FittingRoomViewModelFactory(application, repository))
            .get(FittingRoomViewModel::class.java)
        setContent {
            InterfazComposeTheme {
                if (Build.VERSION.SDK_INT >= 33) {
                    // Android 13 o posterior requiere permiso explícito para notificar.
                    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
                    LaunchedEffect(Unit) { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
                }
                val rooms by viewModel.rooms.collectAsState()
                val now by viewModel.now.collectAsState()
                HomeScreen(rooms, now, viewModel)
            }
        }
    }
}
