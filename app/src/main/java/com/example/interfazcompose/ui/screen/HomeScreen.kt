package com.example.interfazcompose.ui.screen

// Importa BitmapFactory para utilizarlo en esta pantalla.
import android.graphics.BitmapFactory
// Importa Uri para utilizarlo en esta pantalla.
import android.net.Uri
// Importa Environment para utilizarlo en esta pantalla.
import android.os.Environment
// Importa Toast para utilizarlo en esta pantalla.
import android.widget.Toast
// Importa rememberLauncherForActivityResult para utilizarlo en esta pantalla.
import androidx.activity.compose.rememberLauncherForActivityResult
// Importa ActivityResultContracts para utilizarlo en esta pantalla.
import androidx.activity.result.contract.ActivityResultContracts
// Importa AnimatedVisibility para utilizarlo en esta pantalla.
import androidx.compose.animation.AnimatedVisibility
// Importa animateColorAsState para utilizarlo en esta pantalla.
import androidx.compose.animation.animateColorAsState
// Importa animateFloatAsState para utilizarlo en esta pantalla.
import androidx.compose.animation.core.animateFloatAsState
// Importa fadeIn para utilizarlo en esta pantalla.
import androidx.compose.animation.fadeIn
// Importa fadeOut para utilizarlo en esta pantalla.
import androidx.compose.animation.fadeOut
// Importa BorderStroke para utilizarlo en esta pantalla.
import androidx.compose.foundation.BorderStroke
// Importa Image para utilizarlo en esta pantalla.
import androidx.compose.foundation.Image
// Importa Arrangement para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.Arrangement
// Importa Column para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.Column
// Importa Row para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.Row
// Importa Spacer para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.Spacer
// Importa fillMaxSize para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.fillMaxSize
// Importa fillMaxWidth para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.fillMaxWidth
// Importa height para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.height
// Importa padding para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.padding
// Importa size para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.size
// Importa width para utilizarlo en esta pantalla.
import androidx.compose.foundation.layout.width
// Importa LazyColumn para utilizarlo en esta pantalla.
import androidx.compose.foundation.lazy.LazyColumn
// Importa LazyRow para utilizarlo en esta pantalla.
import androidx.compose.foundation.lazy.LazyRow
// Importa items para utilizarlo en esta pantalla.
import androidx.compose.foundation.lazy.items
// Importa rememberScrollState para utilizarlo en esta pantalla.
import androidx.compose.foundation.rememberScrollState
// Importa CircleShape para utilizarlo en esta pantalla.
import androidx.compose.foundation.shape.CircleShape
// Importa RoundedCornerShape para utilizarlo en esta pantalla.
import androidx.compose.foundation.shape.RoundedCornerShape
// Importa verticalScroll para utilizarlo en esta pantalla.
import androidx.compose.foundation.verticalScroll
// Importa Icons para utilizarlo en esta pantalla.
import androidx.compose.material.icons.Icons
// Importa FactCheck para utilizarlo en esta pantalla.
import androidx.compose.material.icons.automirrored.filled.FactCheck
// Importa AccessTime para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.AccessTime
// Importa CameraAlt para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.CameraAlt
// Importa CheckCircle para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.CheckCircle
// Importa CleaningServices para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.CleaningServices
// Importa Dashboard para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.Dashboard
// Importa ErrorOutline para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.ErrorOutline
// Importa EventAvailable para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.EventAvailable
// Importa Inventory2 para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.Inventory2
// Importa LocalMall para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.LocalMall
// Importa NotificationsActive para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.NotificationsActive
// Importa Person para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.Person
// Importa RoomPreferences para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.RoomPreferences
// Importa Visibility para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.Visibility
// Importa VisibilityOff para utilizarlo en esta pantalla.
import androidx.compose.material.icons.filled.VisibilityOff
// Importa AlertDialog para utilizarlo en esta pantalla.
import androidx.compose.material3.AlertDialog
// Importa Button para utilizarlo en esta pantalla.
import androidx.compose.material3.Button
// Importa Card para utilizarlo en esta pantalla.
import androidx.compose.material3.Card
// Importa CardDefaults para utilizarlo en esta pantalla.
import androidx.compose.material3.CardDefaults
// Importa ElevatedCard para utilizarlo en esta pantalla.
import androidx.compose.material3.ElevatedCard
// Importa ExperimentalMaterial3Api para utilizarlo en esta pantalla.
import androidx.compose.material3.ExperimentalMaterial3Api
// Importa FilterChip para utilizarlo en esta pantalla.
import androidx.compose.material3.FilterChip
// Importa Icon para utilizarlo en esta pantalla.
import androidx.compose.material3.Icon
// Importa LinearProgressIndicator para utilizarlo en esta pantalla.
import androidx.compose.material3.LinearProgressIndicator
// Importa MaterialTheme para utilizarlo en esta pantalla.
import androidx.compose.material3.MaterialTheme
// Importa NavigationBar para utilizarlo en esta pantalla.
import androidx.compose.material3.NavigationBar
// Importa NavigationBarItem para utilizarlo en esta pantalla.
import androidx.compose.material3.NavigationBarItem
// Importa OutlinedButton para utilizarlo en esta pantalla.
import androidx.compose.material3.OutlinedButton
// Importa OutlinedTextField para utilizarlo en esta pantalla.
import androidx.compose.material3.OutlinedTextField
// Importa Scaffold para utilizarlo en esta pantalla.
import androidx.compose.material3.Scaffold
// Importa Surface para utilizarlo en esta pantalla.
import androidx.compose.material3.Surface
// Importa Text para utilizarlo en esta pantalla.
import androidx.compose.material3.Text
// Importa TextButton para utilizarlo en esta pantalla.
import androidx.compose.material3.TextButton
// Importa TopAppBar para utilizarlo en esta pantalla.
import androidx.compose.material3.TopAppBar
// Importa LaunchedEffect para utilizarlo en esta pantalla.
import androidx.compose.runtime.LaunchedEffect
// Importa Composable para utilizarlo en esta pantalla.
import androidx.compose.runtime.Composable
// Importa collectAsState para utilizarlo en esta pantalla.
import androidx.compose.runtime.collectAsState
// Importa getValue para utilizarlo en esta pantalla.
import androidx.compose.runtime.getValue
// Importa mutableStateOf para utilizarlo en esta pantalla.
import androidx.compose.runtime.mutableStateOf
// Importa remember para utilizarlo en esta pantalla.
import androidx.compose.runtime.remember
// Importa rememberSaveable para utilizarlo en esta pantalla.
import androidx.compose.runtime.saveable.rememberSaveable
// Importa setValue para utilizarlo en esta pantalla.
import androidx.compose.runtime.setValue
// Importa Alignment para utilizarlo en esta pantalla.
import androidx.compose.ui.Alignment
// Importa Modifier para utilizarlo en esta pantalla.
import androidx.compose.ui.Modifier
// Importa clip para utilizarlo en esta pantalla.
import androidx.compose.ui.draw.clip
// Importa ContentScale para utilizarlo en esta pantalla.
import androidx.compose.ui.layout.ContentScale
// Importa Color para utilizarlo en esta pantalla.
import androidx.compose.ui.graphics.Color
// Importa asImageBitmap para utilizarlo en esta pantalla.
import androidx.compose.ui.graphics.asImageBitmap
// Importa LocalContext para utilizarlo en esta pantalla.
import androidx.compose.ui.platform.LocalContext
// Importa FontWeight para utilizarlo en esta pantalla.
import androidx.compose.ui.text.font.FontWeight
// Importa TextOverflow para utilizarlo en esta pantalla.
import androidx.compose.ui.text.style.TextOverflow
// Importa KeyboardType para utilizarlo en esta pantalla.
import androidx.compose.ui.text.input.KeyboardType
// Importa PasswordVisualTransformation para utilizarlo en esta pantalla.
import androidx.compose.ui.text.input.PasswordVisualTransformation
// Importa VisualTransformation para utilizarlo en esta pantalla.
import androidx.compose.ui.text.input.VisualTransformation
// Importa KeyboardOptions para utilizarlo en esta pantalla.
import androidx.compose.foundation.text.KeyboardOptions
// Importa dp para utilizarlo en esta pantalla.
import androidx.compose.ui.unit.dp
// Importa sp para utilizarlo en esta pantalla.
import androidx.compose.ui.unit.sp
// Importa FileProvider para utilizarlo en esta pantalla.
import androidx.core.content.FileProvider
// Importa RoomUsageEntity para utilizarlo en esta pantalla.
import com.example.interfazcompose.data.local.RoomUsageEntity
// Importa FittingRoom para utilizarlo en esta pantalla.
import com.example.interfazcompose.domain.model.FittingRoom
// Importa RoomStatus para utilizarlo en esta pantalla.
import com.example.interfazcompose.domain.model.RoomStatus
// Importa AdminLoginValidator para utilizarlo en esta pantalla.
import com.example.interfazcompose.domain.validation.AdminLoginValidator
// Importa DeviceFeedback para utilizarlo en esta pantalla.
import com.example.interfazcompose.platform.DeviceFeedback
// Importa FittingRoomViewModel para utilizarlo en esta pantalla.
import com.example.interfazcompose.ui.viewmodel.FittingRoomViewModel
// Importa File para utilizarlo en esta pantalla.
import java.io.File
// Importa SimpleDateFormat para utilizarlo en esta pantalla.
import java.text.SimpleDateFormat
// Importa Date para utilizarlo en esta pantalla.
import java.util.Date
// Importa Locale para utilizarlo en esta pantalla.
import java.util.Locale

// enum class: define opciones fijas y seguras para la navegación o los estados de la interfaz.
private enum class Destination(val label: String) { HOME("Inicio"), ROOMS("Probadores"), ALERTS("Alertas"), HISTORY("Historial") }
// enum class: define opciones fijas y seguras para la navegación o los estados de la interfaz.
private enum class RoomFilter(val label: String) { ALL("Todos"), FREE("Libres"), OCCUPIED("Ocupados"), ALERT("En alerta") }
// enum class: define opciones fijas y seguras para la navegación o los estados de la interfaz.
private enum class AppRole(val label: String) { ADMIN("Administrador"), USER("Invitado") }
// enum class: define opciones fijas y seguras para la navegación o los estados de la interfaz.
private enum class PhotoKind { OCCUPANCY, CLEANING }

// @OptIn: autoriza el uso de componentes Material 3 marcados como experimentales.
@OptIn(ExperimentalMaterial3Api::class)
// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun HomeScreen(): pantalla principal: coordina navegación, permisos y diálogos; no inicia una corrutina por sí sola.
fun HomeScreen(rooms: List<FittingRoom>, now: Long, viewModel: FittingRoomViewModel) {
    // var destination: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var destination by rememberSaveable { mutableStateOf(Destination.HOME) }
    // var selectedRole: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var selectedRole by rememberSaveable { mutableStateOf<AppRole?>(null) }
    // var staffName: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var staffName by rememberSaveable { mutableStateOf("") }
    // var accessError: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var accessError by rememberSaveable { mutableStateOf(false) }
    // var assignRoom: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    var assignRoom by remember { mutableStateOf<FittingRoom?>(null) }
    // Estado Compose: probador seleccionado para corregir sus prendas.
    // var editRoom: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    var editRoom by remember { mutableStateOf<FittingRoom?>(null) }
    // LocalContext.current: obtiene el Context de Android para cámara, archivos o mensajes.
    val context = LocalContext.current
    // collectAsState(): observa un StateFlow del ViewModel y actualiza la pantalla cuando cambia.
    val errors by viewModel.formErrors.collectAsState()
    // collectAsState(): observa los StateFlow del ViewModel en Compose.
    // collectAsState(): observa un StateFlow del ViewModel y actualiza la pantalla cuando cambia.
    val history by viewModel.dailyHistory.collectAsState()
    // collectAsState(): observa un StateFlow del ViewModel y actualiza la pantalla cuando cambia.
    val editError by viewModel.editError.collectAsState()
    // var captureRoomId: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    var captureRoomId by remember { mutableStateOf<Int?>(null) }
    // var captureKind: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    var captureKind by remember { mutableStateOf<PhotoKind?>(null) }
    // var captureFile: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    var captureFile by remember { mutableStateOf<File?>(null) }
    // val photoLauncher: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    val photoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        // val roomId: variable de solo lectura utilizada en esta función.
        val roomId = captureRoomId
        // val kind: variable de solo lectura utilizada en esta función.
        val kind = captureKind
        // val file: variable de solo lectura utilizada en esta función.
        val file = captureFile
        // if: ejecuta este bloque solo cuando se cumple la condición booleana.
        if (saved && roomId != null && kind != null && file != null) {
            // if: ejecuta este bloque solo cuando se cumple la condición booleana.
            if (kind == PhotoKind.OCCUPANCY) viewModel.saveOccupancyPhoto(roomId, file.absolutePath)
            // Llamada al ViewModel: solicita una operación de negocio sin acceder directamente al DAO.
            else viewModel.saveCleaningPhoto(roomId, file.absolutePath)
            DeviceFeedback.confirm(context)
        } else {
            file?.delete()
        }
        captureRoomId = null
        captureKind = null
        captureFile = null
    }
    // fun takePhoto(): ejecuta esta acción de la interfaz; no inicia una corrutina por sí sola.
    fun takePhoto(room: FittingRoom, kind: PhotoKind) {
        // runCatching: ejecuta código y captura excepciones como Result.
        runCatching {
            // val picturesDirectory: variable de solo lectura utilizada en esta función.
            val picturesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: error("El dispositivo no tiene almacenamiento para fotos disponible.")
            // val directory: File representa una ruta o archivo local.
            val directory = File(picturesDirectory, "probadores")
            // if: ejecuta este bloque solo cuando se cumple la condición booleana.
            if (!directory.exists() && !directory.mkdirs()) error("No se pudo preparar el almacenamiento de fotos.")
            // val file: File representa una ruta o archivo local.
            val file = File.createTempFile("probador_${room.id}_${kind.name.lowercase()}_", ".jpg", directory)
            // val uri: File representa una ruta o archivo local.
            val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            captureRoomId = room.id
            captureKind = kind
            captureFile = file
            photoLauncher.launch(uri)
        }.onFailure {
            Toast.makeText(context, "No se pudo abrir la cámara.", Toast.LENGTH_SHORT).show()
        }
    }
    // if: ejecuta este bloque solo cuando se cumple la condición booleana.
    if (selectedRole == null) {
        RoleEntryScreen(
            showError = accessError,
            onInputChanged = { accessError = false },
            onAdminLogin = { username, password ->
                // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                if (AdminLoginValidator.isValid(username, password)) {
                    staffName = AdminLoginValidator.USERNAME
                    selectedRole = AppRole.ADMIN
                    destination = Destination.HOME
                } else {
                    accessError = true
                }
            },
            onGuest = {
                staffName = "Invitado"
                selectedRole = AppRole.USER
                destination = Destination.ROOMS
            }
        )
        return
    }
    // val role: el operador ?: usa un valor alternativo cuando el anterior es null.
    val role = selectedRole ?: AppRole.USER
    // val availableDestinations: variable de solo lectura utilizada en esta función.
    val availableDestinations = if (role == AppRole.ADMIN) Destination.entries else listOf(Destination.ROOMS)
    // val title: when selecciona un valor según el estado.
    val title = when (destination) {
        Destination.HOME -> "Rincón Sur"
        Destination.ROOMS -> "Probadores"
        Destination.ALERTS -> "Alertas de uso"
        Destination.HISTORY -> "Historial diario"
    }

    // Scaffold: organiza la barra superior, navegación inferior y contenido principal.
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        // Text: muestra información visible al usuario.
                        Text(title, fontWeight = FontWeight.Bold)
                        // Text: muestra información visible al usuario.
                        Text("${role.label} · $staffName", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    // TextButton: botón de texto para acciones secundarias.
                    TextButton(onClick = { selectedRole = null; staffName = ""; destination = Destination.HOME }) {
                        // Text: muestra información visible al usuario.
                        Text("Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = {
            // NavigationBar: muestra las pestañas de navegación inferior.
            NavigationBar {
                availableDestinations.forEach { tab ->
                    // val icon: when selecciona un valor según el estado.
                    val icon = when (tab) {
                        Destination.HOME -> Icons.Default.Dashboard
                        Destination.ROOMS -> Icons.Default.RoomPreferences
                        Destination.ALERTS -> Icons.Default.NotificationsActive
                        Destination.HISTORY -> Icons.Default.AccessTime
                    }
                    // NavigationBarItem: representa una pestaña seleccionable.
                    NavigationBarItem(
                        selected = destination == tab,
                        // onClick: callback que se ejecuta cuando el usuario pulsa el componente.
                        onClick = { destination = tab },
                        icon = { Icon(icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { insets ->
        // Column: organiza sus componentes verticalmente.
        Column(Modifier.fillMaxSize().padding(insets)) {
            // when: selecciona una alternativa según el estado o las condiciones.
            when (destination) {
                Destination.HOME -> DashboardScreen(rooms, now, onRooms = { destination = Destination.ROOMS })
                Destination.ROOMS -> RoomsScreen(rooms, now, canManage = role == AppRole.ADMIN, onAssign = { assignRoom = it }, onEdit = { editRoom = it }, onRelease = { room ->
                    // Llamada al ViewModel: solicita una operación de negocio sin acceder directamente al DAO.
                    viewModel.release(room); DeviceFeedback.confirm(context)
                }, onCleaned = { room ->
                    // Llamada al ViewModel: solicita una operación de negocio sin acceder directamente al DAO.
                    viewModel.finishCleaning(room); DeviceFeedback.confirm(context)
                }, onOccupancyPhoto = { room ->
                    takePhoto(room, PhotoKind.OCCUPANCY)
                }, onCleaningPhoto = { room ->
                    takePhoto(room, PhotoKind.CLEANING)
                })
                Destination.HISTORY -> {
                    // LaunchedEffect: ejecuta la carga al abrir esta sección.
                    // LaunchedEffect: inicia una corrutina asociada al ciclo de vida de este Composable.
                    LaunchedEffect(Unit) { viewModel.loadAllDailyHistory() }
                    DailyHistoryScreen(rooms, history)
                }
                Destination.ALERTS -> AlertsScreen(rooms, now, onRooms = { destination = Destination.ROOMS }, onRelease = { room ->
                    // Llamada al ViewModel: solicita una operación de negocio sin acceder directamente al DAO.
                    viewModel.release(room); DeviceFeedback.confirm(context)
                }, onOccupancyPhoto = { room -> takePhoto(room, PhotoKind.OCCUPANCY) })
            }
        }
    }
    // let: abre el diálogo solo si existe un probador seleccionado.
    editRoom?.let { room ->
        EditItemsDialog(
            room = room,
            error = editError,
            onDismiss = { editRoom = null },
            onSubmit = { quantity ->
                // Callback: el ViewModel confirma si SQLite se actualizó.
                // Llamada al ViewModel: solicita una operación de negocio sin acceder directamente al DAO.
                viewModel.editItemCount(room.id, quantity) { success ->
                    // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                    if (success) {
                        DeviceFeedback.confirm(context)
                        editRoom = null
                    }
                }
            }
        )
    }
    assignRoom?.let { room ->
        AssignmentDialog(room, errors, onDismiss = { assignRoom = null }, onSubmit = { count, category ->
            // Llamada al ViewModel: solicita una operación de negocio sin acceder directamente al DAO.
            viewModel.assign(room, count, category) { valid ->
                // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                if (valid) {
                    DeviceFeedback.confirm(context)
                    assignRoom = null
                }
            }
        })
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun RoleEntryScreen(): muestra el acceso de administrador e invitado; no inicia una corrutina por sí sola.
private fun RoleEntryScreen(
    showError: Boolean,
    onInputChanged: () -> Unit,
    onAdminLogin: (String, String) -> Unit,
    onGuest: () -> Unit
) {
    // var username: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var username by rememberSaveable { mutableStateOf("") }
    // var password: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var password by rememberSaveable { mutableStateOf("") }
    // var passwordVisible: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    // Column: organiza sus componentes verticalmente.
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Text: muestra información visible al usuario.
        Text("Rincón Sur", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        // Text: muestra información visible al usuario.
        Text("Control de probadores", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(28.dp))
        // Text: muestra información visible al usuario.
        Text("Acceso de administrador", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))
        // OutlinedTextField: campo de texto editable de Material 3.
        OutlinedTextField(
            value = username,
            // onValueChange: callback que actualiza el estado al escribir.
            onValueChange = { username = it; onInputChanged() },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Usuario") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            isError = showError
        )
        Spacer(Modifier.height(10.dp))
        // OutlinedTextField: campo de texto editable de Material 3.
        OutlinedTextField(
            value = password,
            // onValueChange: callback que actualiza el estado al escribir.
            onValueChange = { password = it; onInputChanged() },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            singleLine = true,
            isError = showError,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                // TextButton: botón de texto para acciones secundarias.
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    // Icon: representa una acción o estado con un símbolo.
                    Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                }
            }
        )
        // if: ejecuta este bloque solo cuando se cumple la condición booleana.
        if (showError) {
            // Text: muestra información visible al usuario.
            Text("Usuario o contraseña incorrectos.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(18.dp))
        // Button: botón principal que ejecuta una acción al pulsarlo.
        Button(
            // onClick: callback que se ejecuta cuando el usuario pulsa el componente.
            onClick = { onAdminLogin(username, password) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            // Text: muestra información visible al usuario.
            Text("Iniciar sesión")
        }
        Spacer(Modifier.height(8.dp))
        // OutlinedButton: botón secundario con borde.
        OutlinedButton(onClick = onGuest, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp)) {
            // Text: muestra información visible al usuario.
            Text("Entrar como invitado")
        }
        Spacer(Modifier.height(12.dp))
        // Text: muestra información visible al usuario.
        Text("El invitado puede consultar los estados, sin modificarlos.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun DashboardScreen(): muestra el resumen de ocupación; no inicia una corrutina por sí sola.
private fun DashboardScreen(
    rooms: List<FittingRoom>, now: Long,
    onRooms: () -> Unit
) {
    // val free: count calcula cuántos elementos cumplen la condición.
    val free = rooms.count { it.status == RoomStatus.AVAILABLE }
    // val occupied: count calcula cuántos elementos cumplen la condición.
    val occupied = rooms.count { it.status == RoomStatus.OCCUPIED }
    // val alerts: count calcula cuántos elementos cumplen la condición.
    val alerts = rooms.count { it.hasExceededLimit(now) }
    // val progress: estado animado que Compose actualiza cuando cambia su valor.
    val progress by animateFloatAsState(if (rooms.isEmpty()) 0f else occupied.toFloat() / rooms.size, label = "ocupacion")
    // Column: organiza sus componentes verticalmente.
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(18.dp))
        // Text: muestra información visible al usuario.
        Text("Resumen de probadores", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        // Row: organiza sus componentes horizontalmente.
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MetricCard("Disponibles", free.toString(), Icons.Default.EventAvailable, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            MetricCard("En uso", occupied.toString(), Icons.Default.Person, MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
            MetricCard("Alertas", alerts.toString(), Icons.Default.ErrorOutline, MaterialTheme.colorScheme.error, Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        // Card: agrupa información y controles dentro de una tarjeta.
        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            // Column: organiza sus componentes verticalmente.
            Column(Modifier.padding(18.dp)) {
                // Row: organiza sus componentes horizontalmente.
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        // Text: muestra información visible al usuario.
                        Text("Ocupación actual", fontWeight = FontWeight.SemiBold)
                        // Text: muestra información visible al usuario.
                        Text("$occupied de ${rooms.size} probadores", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    // Text: muestra información visible al usuario.
                    Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape))
            }
        }
        Spacer(Modifier.height(24.dp))
        // Row: organiza sus componentes horizontalmente.
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            // Text: muestra información visible al usuario.
            Text("En uso y limpieza", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            // TextButton: botón de texto para acciones secundarias.
            TextButton(onClick = onRooms) { Text("Ver todos") }
        }
        // LazyRow: lista horizontal de composición diferida.
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // items(): crea una tarjeta por cada elemento de la lista.
            items(rooms.filter { it.status != RoomStatus.AVAILABLE }.take(5), key = { it.id }) { room ->
                RoomPreviewCard(room, now)
            }
        }
        // if: ejecuta este bloque solo cuando se cumple la condición booleana.
        if (rooms.none { it.status != RoomStatus.AVAILABLE }) {
            EmptyMessage("Sin probadores en uso", "Todos están disponibles.", Icons.Default.CheckCircle)
        }
        Spacer(Modifier.height(22.dp))
        // Button: botón principal que ejecuta una acción al pulsarlo.
        Button(onClick = onRooms, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) {
            // Icon: representa una acción o estado con un símbolo.
            Icon(Icons.Default.RoomPreferences, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            // Text: muestra información visible al usuario.
            Text("Gestionar probadores")
        }
        Spacer(Modifier.height(18.dp))
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun MetricCard(): dibuja una tarjeta con una métrica; no inicia una corrutina por sí sola.
private fun MetricCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color, modifier: Modifier = Modifier) {
    // Card: agrupa información y controles dentro de una tarjeta.
    ElevatedCard(modifier, colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        // Column: organiza sus componentes verticalmente.
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(shape = CircleShape, color = tint.copy(alpha = 0.12f)) { Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.padding(8.dp).size(18.dp)) }
            // Text: muestra información visible al usuario.
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            // Text: muestra información visible al usuario.
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun RoomPreviewCard(): muestra una vista resumida de un probador; no inicia una corrutina por sí sola.
private fun RoomPreviewCard(room: FittingRoom, now: Long) {
    // val numberColor: variable de solo lectura utilizada en esta función.
    val numberColor = if (room.status == RoomStatus.OCCUPIED) MaterialTheme.colorScheme.error else Color(0xFF855500)
    // Card: agrupa información y controles dentro de una tarjeta.
    Card(modifier = Modifier.width(210.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        // Row: organiza sus componentes horizontalmente.
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text: muestra información visible al usuario.
            Text(
                room.id.toString().padStart(2, '0'),
                modifier = Modifier.width(62.dp),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = numberColor
            )
            Spacer(Modifier.width(8.dp))
            // Column: organiza sus componentes verticalmente.
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                // Text: muestra información visible al usuario.
                Text(
                    // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                    if (room.status == RoomStatus.CLEANING) "Limpieza" else "En uso",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                // Text: muestra información visible al usuario.
                Text(
                    // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                    if (room.status == RoomStatus.CLEANING) "Requerida" else "${room.itemCount} prendas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                if (room.status == RoomStatus.OCCUPIED) {
                    // Text: muestra información visible al usuario.
                    Text(elapsed(room.startedAt, now), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun RoomsScreen(): muestra la lista de probadores y sus filtros; no inicia una corrutina por sí sola.
private fun RoomsScreen(
    rooms: List<FittingRoom>, now: Long,
    canManage: Boolean,
    onAssign: (FittingRoom) -> Unit,
    onEdit: (FittingRoom) -> Unit,
    onRelease: (FittingRoom) -> Unit,
    onCleaned: (FittingRoom) -> Unit,
    onOccupancyPhoto: (FittingRoom) -> Unit,
    onCleaningPhoto: (FittingRoom) -> Unit
) {
    // if: ejecuta este bloque solo cuando se cumple la condición booleana.
    if (!canManage) {
        // Column: organiza sus componentes verticalmente.
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp)) {
            // Text: muestra información visible al usuario.
            Text("Estado de probadores", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            // if: ejecuta este bloque solo cuando se cumple la condición booleana.
            if (rooms.isEmpty()) {
                EmptyMessage("Sin probadores", "Aún no hay estados configurados.", Icons.Default.RoomPreferences)
            } else {
                // LazyColumn: lista vertical que compone elementos según se necesitan.
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // items(): crea una tarjeta por cada elemento de la lista.
                    items(rooms, key = { it.id }) { room -> UserStatusCard(room) }
                }
            }
        }
        return
    }
    // var filter: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var filter by rememberSaveable { mutableStateOf(RoomFilter.ALL) }
    // val shown: filter selecciona elementos de una lista según una condición.
    val shown = rooms.filter { room ->
        // when: selecciona una alternativa según el estado o las condiciones.
        when (filter) {
            RoomFilter.ALL -> true
            RoomFilter.FREE -> room.status == RoomStatus.AVAILABLE
            RoomFilter.OCCUPIED -> room.status == RoomStatus.OCCUPIED
            RoomFilter.ALERT -> room.hasExceededLimit(now)
        }
    }
    // Column: organiza sus componentes verticalmente.
    Column(Modifier.fillMaxSize()) {
        // Column: organiza sus componentes verticalmente.
        Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            // Text: muestra información visible al usuario.
            Text("Filtrar por estado", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            // LazyRow: lista horizontal de composición diferida.
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // items(): crea una tarjeta por cada elemento de la lista.
                items(RoomFilter.entries) { item ->
                    FilterChip(selected = filter == item, onClick = { filter = item }, label = { Text(item.label) })
                }
            }
        }
        // if: ejecuta este bloque solo cuando se cumple la condición booleana.
        if (shown.isEmpty()) {
            EmptyMessage("Sin resultados", "No hay probadores en este filtro.", Icons.AutoMirrored.Filled.FactCheck, Modifier.fillMaxWidth().padding(28.dp))
        } else {
            // LazyColumn: lista vertical que compone elementos según se necesitan.
            LazyColumn(contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 18.dp, end = 18.dp, bottom = 18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // items(): crea una tarjeta por cada elemento de la lista.
                items(shown, key = { it.id }) { room ->
                    RoomCard(room, now, onAssign, onEdit, onRelease, onCleaned, onOccupancyPhoto, onCleaningPhoto)
                }
            }
        }
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun UserStatusCard(): muestra el estado de un probador al invitado; no inicia una corrutina por sí sola.
private fun UserStatusCard(room: FittingRoom) {
    // val statusText: when selecciona un valor según el estado.
    val statusText = when (room.status) {
        RoomStatus.AVAILABLE -> "Disponible"
        RoomStatus.OCCUPIED -> "Ocupado"
        RoomStatus.CLEANING -> "En limpieza"
    }
    // val statusColor: when selecciona un valor según el estado.
    val statusColor = when (room.status) {
        RoomStatus.AVAILABLE -> MaterialTheme.colorScheme.primary
        RoomStatus.OCCUPIED -> MaterialTheme.colorScheme.error
        RoomStatus.CLEANING -> Color(0xFF855500)
    }
    // Card: agrupa información y controles dentro de una tarjeta.
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        // Row: organiza sus componentes horizontalmente.
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Text: muestra información visible al usuario.
            Text(room.id.toString().padStart(2, '0'), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = statusColor)
            // Text: muestra información visible al usuario.
            Text(statusText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = statusColor)
        }
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun RoomCard(): dibuja el probador y sus acciones según el estado; no inicia una corrutina por sí sola.
private fun RoomCard(
    room: FittingRoom,
    now: Long,
    onAssign: (FittingRoom) -> Unit,
    onEdit: (FittingRoom) -> Unit,
    onRelease: (FittingRoom) -> Unit,
    onCleaned: (FittingRoom) -> Unit,
    onOccupancyPhoto: (FittingRoom) -> Unit,
    onCleaningPhoto: (FittingRoom) -> Unit
) {
    // val alarm: variable de solo lectura utilizada en esta función.
    val alarm = room.hasExceededLimit(now)
    // val border: variable de solo lectura utilizada en esta función.
    val border = if (alarm) MaterialTheme.colorScheme.error.copy(alpha = 0.45f) else MaterialTheme.colorScheme.outlineVariant
    // val numberColor: when selecciona un valor según el estado.
    val numberColor = when {
        room.status == RoomStatus.OCCUPIED -> MaterialTheme.colorScheme.error
        room.status == RoomStatus.AVAILABLE -> MaterialTheme.colorScheme.primary
        room.status == RoomStatus.CLEANING -> Color(0xFF855500)
        else -> MaterialTheme.colorScheme.tertiary
    }
    // Card: agrupa información y controles dentro de una tarjeta.
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, border)
    ) {
        // Row: organiza sus componentes horizontalmente.
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text: muestra información visible al usuario.
            Text(
                text = room.id.toString().padStart(2, '0'),
                modifier = Modifier.width(72.dp),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = numberColor,
                maxLines = 1
            )
            Spacer(Modifier.width(12.dp))
            // Column: organiza sus componentes verticalmente.
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Text: muestra información visible al usuario.
                Text(
                    text = when (room.status) {
                        RoomStatus.AVAILABLE -> "Disponible para asignación"
                        RoomStatus.OCCUPIED -> "${room.itemCount} prendas · ${room.category}"
                        RoomStatus.CLEANING -> "Limpieza requerida"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // AnimatedVisibility: muestra u oculta contenido con animación.
                AnimatedVisibility(
                    visible = room.status == RoomStatus.OCCUPIED,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    // Row: organiza sus componentes horizontalmente.
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Icon: representa una acción o estado con un símbolo.
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = if (alarm) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        // Text: muestra información visible al usuario.
                        Text(
                            elapsed(room.startedAt, now),
                            fontWeight = FontWeight.SemiBold,
                            color = if (alarm) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.weight(1f))
                        // Text: muestra información visible al usuario.
                        Text(
                            // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                            if (alarm) "Revisar" else "Límite 30 min",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (alarm) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                if (room.status == RoomStatus.OCCUPIED) {
                    // Botón disponible solo para administradores en probadores ocupados.
                    // OutlinedButton: botón secundario con borde.
                    OutlinedButton(
                        // onClick: callback que se ejecuta cuando el usuario pulsa el componente.
                        onClick = { onEdit(room) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Icon: representa una acción o estado con un símbolo.
                        Icon(Icons.Default.Inventory2, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        // Text: muestra información visible al usuario.
                        Text("Editar prendas")
                    }
                    // Text: muestra información visible al usuario.
                    Text(
                        "Una foto opcional; la persona elige si aparece o si se fotografían las prendas.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    room.occupancyPhotoPath?.let { PhotoPreview(it, "Foto de ocupación") }
                    // OutlinedButton: botón secundario con borde.
                    OutlinedButton(onClick = { onOccupancyPhoto(room) }, modifier = Modifier.fillMaxWidth()) {
                        // Icon: representa una acción o estado con un símbolo.
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        // Text: muestra información visible al usuario.
                        Text(if (room.occupancyPhotoPath == null) "Tomar foto de ocupación" else "Cambiar foto de ocupación")
                    }
                }
                // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                if (room.status == RoomStatus.CLEANING) {
                    room.cleaningPhotoPath?.let { PhotoPreview(it, "Estado después de limpiar") }
                    // OutlinedButton: botón secundario con borde.
                    OutlinedButton(onClick = { onCleaningPhoto(room) }, modifier = Modifier.fillMaxWidth()) {
                        // Icon: representa una acción o estado con un símbolo.
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        // Text: muestra información visible al usuario.
                        Text(if (room.cleaningPhotoPath == null) "Tomar foto después de limpiar" else "Cambiar foto de limpieza")
                    }
                }
                // when: selecciona una alternativa según el estado o las condiciones.
                when (room.status) {
                    RoomStatus.AVAILABLE -> Button(
                        // onClick: callback que se ejecuta cuando el usuario pulsa el componente.
                        onClick = { onAssign(room) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        // Icon: representa una acción o estado con un símbolo.
                        Icon(Icons.Default.Person, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        // Text: muestra información visible al usuario.
                        Text("Asignar")
                    }
                    RoomStatus.OCCUPIED -> OutlinedButton(
                        // onClick: callback que se ejecuta cuando el usuario pulsa el componente.
                        onClick = { onRelease(room) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        // Icon: representa una acción o estado con un símbolo.
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        // Text: muestra información visible al usuario.
                        Text("Finalizar uso")
                    }
                    RoomStatus.CLEANING -> OutlinedButton(
                        // onClick: callback que se ejecuta cuando el usuario pulsa el componente.
                        onClick = { onCleaned(room) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        // Icon: representa una acción o estado con un símbolo.
                        Icon(Icons.Default.CleaningServices, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        // Text: muestra información visible al usuario.
                        Text("Marcar disponible")
                    }
                }
            }
        }
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun PhotoPreview(): carga y muestra una fotografía guardada; no inicia una corrutina por sí sola.
private fun PhotoPreview(path: String, label: String) {
    // val bitmap: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    val bitmap = remember(path) {
        // runCatching: ejecuta código y captura excepciones como Result.
        runCatching {
            // val bounds: variable de solo lectura utilizada en esta función.
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(path, bounds)
            // val sample: variable de solo lectura utilizada en esta función.
            val sample = maxOf(1, maxOf(bounds.outWidth, bounds.outHeight) / 640)
            BitmapFactory.decodeFile(path, BitmapFactory.Options().apply { inSampleSize = sample })?.asImageBitmap()
        }.getOrNull()
    }
    // Column: organiza sus componentes verticalmente.
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        // Text: muestra información visible al usuario.
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        // if: ejecuta este bloque solo cuando se cumple la condición booleana.
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = label,
                modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            // Text: muestra información visible al usuario.
            Text("La foto guardada no está disponible.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        }
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun StatusPill(): muestra una etiqueta visual del estado; no inicia una corrutina por sí sola.
private fun StatusPill(room: FittingRoom, now: Long) {
    // val warning: variable de solo lectura utilizada en esta función.
    val warning = room.hasExceededLimit(now)
    // val target: when selecciona un valor según el estado.
    val target = when {
        warning -> MaterialTheme.colorScheme.errorContainer
        room.status == RoomStatus.AVAILABLE -> MaterialTheme.colorScheme.primaryContainer
        room.status == RoomStatus.CLEANING -> Color(0xFFFFF0D5)
        else -> MaterialTheme.colorScheme.tertiaryContainer
    }
    // val background: estado animado que Compose actualiza cuando cambia su valor.
    val background by animateColorAsState(target, label = "estado")
    // val foreground: when selecciona un valor según el estado.
    val foreground = when {
        warning -> MaterialTheme.colorScheme.onErrorContainer
        room.status == RoomStatus.AVAILABLE -> MaterialTheme.colorScheme.onPrimaryContainer
        room.status == RoomStatus.CLEANING -> Color(0xFF855500)
        else -> MaterialTheme.colorScheme.onTertiaryContainer
    }
    // val label: when selecciona un valor según el estado.
    val label = when {
        warning -> "ALERTA"
        room.status == RoomStatus.AVAILABLE -> "LIBRE"
        room.status == RoomStatus.CLEANING -> "LIMPIEZA"
        else -> "OCUPADO"
    }
    Surface(color = background, shape = CircleShape) {
        // Text: muestra información visible al usuario.
        Text(label, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = foreground, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 0.35.sp)
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun AlertsScreen(): muestra los probadores con alertas; no inicia una corrutina por sí sola.
private fun AlertsScreen(
    rooms: List<FittingRoom>,
    now: Long,
    onRooms: () -> Unit,
    onRelease: (FittingRoom) -> Unit,
    onOccupancyPhoto: (FittingRoom) -> Unit
) {
    // val alerts: filter selecciona elementos de una lista según una condición.
    val alerts = rooms.filter { it.hasExceededLimit(now) }.sortedByDescending { now - (it.startedAt ?: now) }
    // Column: organiza sus componentes verticalmente.
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        // Row: organiza sus componentes horizontalmente.
        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            // Icon: representa una acción o estado con un símbolo.
            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            // Text: muestra información visible al usuario.
            Text("Se genera una alerta al superar 30 minutos.", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(20.dp))
        // Text: muestra información visible al usuario.
        Text("Necesitan atención", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        // Text: muestra información visible al usuario.
        Text("${alerts.size} ${if (alerts.size == 1) "probador supera" else "probadores superan"} el umbral", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        // if: ejecuta este bloque solo cuando se cumple la condición booleana.
        if (alerts.isEmpty()) {
            EmptyMessage("Sin alertas activas", "No hay probadores sobre el límite de 30 minutos.", Icons.Default.CheckCircle)
        } else {
            alerts.forEach { room ->
                RoomCard(
                    room, now, onAssign = {}, onEdit = {}, onRelease = onRelease, onCleaned = {},
                    onOccupancyPhoto = onOccupancyPhoto, onCleaningPhoto = {}
                )
                Spacer(Modifier.height(12.dp))
            }
        }
        // OutlinedButton: botón secundario con borde.
        OutlinedButton(onClick = onRooms, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) { Text("Ver todos los probadores") }
    }
}

// @Composable: construye el formulario visual para corregir prendas.
// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun EditItemsDialog(): formulario para corregir prendas; no inicia una corrutina por sí sola.
private fun EditItemsDialog(
    room: FittingRoom, // Modelo del probador que se editará.
    error: String?, // Mensaje opcional enviado por el ViewModel.
    onDismiss: () -> Unit, // Callback para cerrar el diálogo.
    onSubmit: (String) -> Unit // Callback que envía la cantidad ingresada.
) {
    // rememberSaveable: conserva el texto durante recomposiciones y rotaciones.
    // mutableStateOf: al cambiar el valor, Compose actualiza la pantalla.
    // var quantity: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var quantity by rememberSaveable(room.id) {
        mutableStateOf(room.itemCount.toString())
    }

    // AlertDialog: componente Material 3 que muestra un formulario modal.
    // AlertDialog: abre un cuadro de diálogo con acciones de confirmar o cancelar.
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Corregir prendas · ${room.name}") },
        text = {
            // Column: organiza sus componentes verticalmente.
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // OutlinedTextField: campo de texto editable de Material 3.
                OutlinedTextField(
                    value = quantity,
                    // filter(): permite escribir únicamente dígitos.
                    // onValueChange: callback que actualiza el estado al escribir.
                    onValueChange = { quantity = it.filter(Char::isDigit).take(2) },
                    label = { Text("Cantidad correcta de prendas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = error != null,
                    supportingText = { error?.let { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Text: muestra información visible al usuario.
                Text(
                    "La corrección no contará como un nuevo uso.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            // Button: botón principal que ejecuta una acción al pulsarlo.
            Button(onClick = { onSubmit(quantity) }) { Text("Guardar corrección") }
        },
        dismissButton = {
            // TextButton: botón de texto para acciones secundarias.
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// @Composable: presenta el historial local registrado por Room.
// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun DailyHistoryScreen(): ejecuta esta acción de la interfaz; no inicia una corrutina por sí sola.
private fun DailyHistoryScreen(
    rooms: List<FittingRoom>, // Lista para mostrar el nombre de cada probador.
    history: List<RoomUsageEntity> // Atenciones observadas desde StateFlow.
) {
    // SimpleDateFormat: convierte milisegundos a fecha y hora legibles.
    // val dayFormat: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    val dayFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    // val timeFormat: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    // LazyColumn: muestra registros desplazables sin dibujarlos todos a la vez.
    // LazyColumn: lista vertical que compone elementos según se necesitan.
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Text: muestra información visible al usuario.
            Text("Atenciones de hoy · ${dayFormat.format(Date())}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold)
            // Text: muestra información visible al usuario.
            Text("Total de atenciones: ${history.size}",
                style = MaterialTheme.typography.bodyMedium)
        }

        // items(): dibuja el resumen de usos por cada probador.
        // items(): crea una tarjeta por cada elemento de la lista.
        items(rooms, key = { "summary_${it.id}" }) { room ->
            // val count: count calcula cuántos elementos cumplen la condición.
            val count: Int = history.count { it.roomId == room.id }
            // Card: agrupa información y controles dentro de una tarjeta.
            Card(modifier = Modifier.fillMaxWidth()) {
                // Row: organiza sus componentes horizontalmente.
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Text: muestra información visible al usuario.
                    Text(room.name, fontWeight = FontWeight.SemiBold)
                    // Text: muestra información visible al usuario.
                    Text("$count usos hoy")
                }
            }
        }

        // if: ejecuta este bloque solo cuando se cumple la condición booleana.
        if (history.isEmpty()) {
            item { Text("Todavía no hay atenciones registradas hoy.") }
        } else {
            item {
                // Text: muestra información visible al usuario.
                Text("Detalle de atenciones", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
            }
            // items(): cada elemento corresponde a una atención, no a un probador.
            // items(): crea una tarjeta por cada elemento de la lista.
            items(history, key = { "usage_${it.id}" }) { usage ->
                // val roomName: variable de solo lectura utilizada en esta función.
                val roomName = rooms.firstOrNull { it.id == usage.roomId }?.name
                    ?: "Probador ${usage.roomId}"
                // Card: agrupa información y controles dentro de una tarjeta.
                Card(modifier = Modifier.fillMaxWidth()) {
                    // Column: organiza sus componentes verticalmente.
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        // Text: muestra información visible al usuario.
                        Text(roomName, fontWeight = FontWeight.Bold)
                        // Text: muestra información visible al usuario.
                        Text("Entrada: ${timeFormat.format(Date(usage.entryTime))}")
                        // ?: muestra 'En uso' cuando exitTime es null.
                        // Text: muestra información visible al usuario.
                        Text("Salida: ${usage.exitTime?.let { timeFormat.format(Date(it)) } ?: "En uso"}")
                        // Text: muestra información visible al usuario.
                        Text("Prendas: ${usage.itemCount} · ${usage.category}")
                        // Text: muestra información visible al usuario.
                        Text(if (usage.wasEdited) "Cantidad corregida: sí" else "Cantidad corregida: no")
                    }
                }
            }
        }
    }
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun AssignmentDialog(): formulario para asignar prendas; no inicia una corrutina por sí sola.
private fun AssignmentDialog(room: FittingRoom, errors: Map<String, String>, onDismiss: () -> Unit, onSubmit: (String, String) -> Unit) {
    // var itemCount: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var itemCount by rememberSaveable(room.id) { mutableStateOf("") }
    // var category: estado de Compose que se conserva al recrear la pantalla; mutableStateOf permite actualizarlo.
    var category by rememberSaveable(room.id) { mutableStateOf("") }
    // var menuExpanded: estado recordado durante la recomposición; mutableStateOf notifica cambios a Compose.
    var menuExpanded by remember { mutableStateOf(false) }
    // AlertDialog: abre un cuadro de diálogo con acciones de confirmar o cancelar.
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Asignar ${room.name}", fontWeight = FontWeight.Bold) },
        text = {
            // Column: organiza sus componentes verticalmente.
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // OutlinedTextField: campo de texto editable de Material 3.
                OutlinedTextField(
                    value = itemCount, onValueChange = { itemCount = it.filter(Char::isDigit).take(2) },
                    label = { Text("Cantidad de prendas") }, leadingIcon = { Icon(Icons.Default.Inventory2, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true,
                    isError = errors.containsKey("items"), supportingText = { errors["items"]?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )
                Column {
                    // OutlinedButton: botón secundario con borde.
                    OutlinedButton(onClick = { menuExpanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        // Icon: representa una acción o estado con un símbolo.
                        Icon(Icons.Default.LocalMall, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        // Text: muestra información visible al usuario.
                        Text(category.ifBlank { "Seleccionar categoría" })
                    }
                    androidx.compose.material3.DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        listOf("Vestuario", "Calzado", "Accesorios").forEach { option ->
                            androidx.compose.material3.DropdownMenuItem(text = { Text(option) }, onClick = { category = option; menuExpanded = false })
                        }
                    }
                    // if: ejecuta este bloque solo cuando se cumple la condición booleana.
                    if (errors.containsKey("category")) Text(errors.getValue("category"), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 14.dp, top = 4.dp))
                }
                errors["room"]?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = { Button(onClick = { onSubmit(itemCount, category) }) { Text("Confirmar asignación") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

// @Composable: anotación que permite construir interfaz declarativa con Jetpack Compose.
@Composable
// fun EmptyMessage(): mensaje cuando una lista no tiene resultados; no inicia una corrutina por sí sola.
private fun EmptyMessage(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f), shape = RoundedCornerShape(18.dp)) {
        // Column: organiza sus componentes verticalmente.
        Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(7.dp)) {
            // Icon: representa una acción o estado con un símbolo.
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            // Text: muestra información visible al usuario.
            Text(title, fontWeight = FontWeight.Bold)
            // Text: muestra información visible al usuario.
            Text(detail, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// fun elapsed(): calcula el tiempo transcurrido; no inicia una corrutina por sí sola.
private fun elapsed(startedAt: Long?, now: Long): String {
    // if: ejecuta este bloque solo cuando se cumple la condición booleana.
    if (startedAt == null) return "Sin registro de inicio"
    // val minutes: variable de solo lectura utilizada en esta función.
    val minutes = ((now - startedAt).coerceAtLeast(0) / 60_000L).toInt()
    // return: devuelve el resultado y termina esta función.
    return if (minutes < 60) "$minutes min" else "${minutes / 60} h ${minutes % 60} min"
}
