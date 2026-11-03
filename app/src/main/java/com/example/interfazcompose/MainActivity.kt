
package com.example.interfazcompose

// IMPORTS: permiten utilizar las herramientas de Android y Compose.
import android.os.Bundle
import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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

// MAINACTIVITY: punto de entrada de la aplicación.
class MainActivity : ComponentActivity() {

    // ONCREATE: se ejecuta al crear la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // EDGE TO EDGE: permite dibujar la interfaz hasta los bordes.
        enableEdgeToEdge()

        // NOTIFICACIONES: crea el canal para alertar sobre ocupaciones.
        OccupancyNotifications.createChannel(this)

        // WORKMANAGER: programa revisiones periódicas en segundo plano.
        // KEEP: evita reemplazar un trabajo periódico ya existente.
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "alertas-probadores",
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<OccupancyAlertWorker>(
                15,
                TimeUnit.MINUTES
            ).build()
        )

        // REPOSITORY: conecta la base de datos Room con la API.
        val repository = FittingRoomRepository(
            FittingRoomDatabase.get(this).roomDao(),
            ApiProvider.api
        )

        // VIEWMODEL: administra los datos y la lógica de la pantalla.
        // FACTORY: permite crear el ViewModel con sus dependencias.
        val viewModel = ViewModelProvider(
            this,
            FittingRoomViewModelFactory(application, repository)
        ).get(FittingRoomViewModel::class.java)

        // SETCONTENT: comienza a construir la interfaz con Compose.
        setContent {

            // THEME: aplica los colores y estilos de la aplicación.
            InterfazComposeTheme {

                // PERMISOS: Android 13+ requiere autorización para notificar.
                if (Build.VERSION.SDK_INT >= 33) {

                    // LAUNCHER: solicita el permiso de notificaciones.
                    val permissionLauncher =
                        rememberLauncherForActivityResult(
                            ActivityResultContracts.RequestPermission()
                        ) { }

                    // LAUNCHEDEFFECT: solicita el permiso al entrar.
                    LaunchedEffect(Unit) {
                        permissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                }

                // COLLECTASSTATE: observa los probadores y actualiza la Vista.
                val rooms by viewModel.rooms.collectAsState()

                // NOW: observa la hora actual desde el ViewModel.
                val now by viewModel.now.collectAsState()

                // HOMESCREEN: muestra la pantalla principal.
                // Recibe los probadores, la hora y el ViewModel.
                HomeScreen(rooms, now, viewModel)
            }
        }
    }
}
