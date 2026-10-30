package com.example.interfazcompose.ui.screen

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RoomPreferences
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.interfazcompose.domain.model.FittingRoom
import com.example.interfazcompose.domain.model.RoomStatus
import com.example.interfazcompose.domain.validation.AdminLoginValidator
import com.example.interfazcompose.platform.DeviceFeedback
import com.example.interfazcompose.ui.viewmodel.FittingRoomViewModel
import java.io.File

private enum class Destination(val label: String) { HOME("Inicio"), ROOMS("Probadores"), ALERTS("Alertas") }
private enum class RoomFilter(val label: String) { ALL("Todos"), FREE("Libres"), OCCUPIED("Ocupados"), ALERT("En alerta") }
private enum class AppRole(val label: String) { ADMIN("Administrador"), USER("Invitado") }
private enum class PhotoKind { OCCUPANCY, CLEANING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(rooms: List<FittingRoom>, now: Long, viewModel: FittingRoomViewModel) {
    var destination by rememberSaveable { mutableStateOf(Destination.HOME) }
    var selectedRole by rememberSaveable { mutableStateOf<AppRole?>(null) }
    var staffName by rememberSaveable { mutableStateOf("") }
    var accessError by rememberSaveable { mutableStateOf(false) }
    var assignRoom by remember { mutableStateOf<FittingRoom?>(null) }
    val context = LocalContext.current
    val errors by viewModel.formErrors.collectAsState()
    var captureRoomId by remember { mutableStateOf<Int?>(null) }
    var captureKind by remember { mutableStateOf<PhotoKind?>(null) }
    var captureFile by remember { mutableStateOf<File?>(null) }
    val photoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        val roomId = captureRoomId
        val kind = captureKind
        val file = captureFile
        if (saved && roomId != null && kind != null && file != null) {
            if (kind == PhotoKind.OCCUPANCY) viewModel.saveOccupancyPhoto(roomId, file.absolutePath)
            else viewModel.saveCleaningPhoto(roomId, file.absolutePath)
            DeviceFeedback.confirm(context)
        } else {
            file?.delete()
        }
        captureRoomId = null
        captureKind = null
        captureFile = null
    }
    fun takePhoto(room: FittingRoom, kind: PhotoKind) {
        runCatching {
            val picturesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: error("El dispositivo no tiene almacenamiento para fotos disponible.")
            val directory = File(picturesDirectory, "probadores")
            if (!directory.exists() && !directory.mkdirs()) error("No se pudo preparar el almacenamiento de fotos.")
            val file = File.createTempFile("probador_${room.id}_${kind.name.lowercase()}_", ".jpg", directory)
            val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            captureRoomId = room.id
            captureKind = kind
            captureFile = file
            photoLauncher.launch(uri)
        }.onFailure {
            Toast.makeText(context, "No se pudo abrir la cámara.", Toast.LENGTH_SHORT).show()
        }
    }
    if (selectedRole == null) {
        RoleEntryScreen(
            showError = accessError,
            onInputChanged = { accessError = false },
            onAdminLogin = { username, password ->
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
    val role = selectedRole ?: AppRole.USER
    val availableDestinations = if (role == AppRole.ADMIN) Destination.entries else listOf(Destination.ROOMS)
    val title = when (destination) {
        Destination.HOME -> "Rincón Sur"
        Destination.ROOMS -> "Probadores"
        Destination.ALERTS -> "Alertas de uso"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(title, fontWeight = FontWeight.Bold)
                        Text("${role.label} · $staffName", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    TextButton(onClick = { selectedRole = null; staffName = ""; destination = Destination.HOME }) {
                        Text("Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                availableDestinations.forEach { tab ->
                    val icon = when (tab) {
                        Destination.HOME -> Icons.Default.Dashboard
                        Destination.ROOMS -> Icons.Default.RoomPreferences
                        Destination.ALERTS -> Icons.Default.NotificationsActive
                    }
                    NavigationBarItem(
                        selected = destination == tab,
                        onClick = { destination = tab },
                        icon = { Icon(icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { insets ->
        Column(Modifier.fillMaxSize().padding(insets)) {
            when (destination) {
                Destination.HOME -> DashboardScreen(rooms, now, onRooms = { destination = Destination.ROOMS })
                Destination.ROOMS -> RoomsScreen(rooms, now, canManage = role == AppRole.ADMIN, onAssign = { assignRoom = it }, onRelease = { room ->
                    viewModel.release(room); DeviceFeedback.confirm(context)
                }, onCleaned = { room ->
                    viewModel.finishCleaning(room); DeviceFeedback.confirm(context)
                }, onOccupancyPhoto = { room ->
                    takePhoto(room, PhotoKind.OCCUPANCY)
                }, onCleaningPhoto = { room ->
                    takePhoto(room, PhotoKind.CLEANING)
                })
                Destination.ALERTS -> AlertsScreen(rooms, now, onRooms = { destination = Destination.ROOMS }, onRelease = { room ->
                    viewModel.release(room); DeviceFeedback.confirm(context)
                }, onOccupancyPhoto = { room -> takePhoto(room, PhotoKind.OCCUPANCY) })
            }
        }
    }
    assignRoom?.let { room ->
        AssignmentDialog(room, errors, onDismiss = { assignRoom = null }, onSubmit = { count, category ->
            viewModel.assign(room, count, category) { valid ->
                if (valid) {
                    DeviceFeedback.confirm(context)
                    assignRoom = null
                }
            }
        })
    }
}

@Composable
private fun RoleEntryScreen(
    showError: Boolean,
    onInputChanged: () -> Unit,
    onAdminLogin: (String, String) -> Unit,
    onGuest: () -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Rincón Sur", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Control de probadores", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(28.dp))
        Text("Acceso de administrador", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it; onInputChanged() },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Usuario") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            isError = showError
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; onInputChanged() },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            singleLine = true,
            isError = showError,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                }
            }
        )
        if (showError) {
            Text("Usuario o contraseña incorrectos.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = { onAdminLogin(username, password) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Iniciar sesión")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onGuest, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp)) {
            Text("Entrar como invitado")
        }
        Spacer(Modifier.height(12.dp))
        Text("El invitado puede consultar los estados, sin modificarlos.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun DashboardScreen(
    rooms: List<FittingRoom>, now: Long,
    onRooms: () -> Unit
) {
    val free = rooms.count { it.status == RoomStatus.AVAILABLE }
    val occupied = rooms.count { it.status == RoomStatus.OCCUPIED }
    val alerts = rooms.count { it.hasExceededLimit(now) }
    val progress by animateFloatAsState(if (rooms.isEmpty()) 0f else occupied.toFloat() / rooms.size, label = "ocupacion")
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(18.dp))
        Text("Resumen de probadores", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MetricCard("Disponibles", free.toString(), Icons.Default.EventAvailable, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            MetricCard("En uso", occupied.toString(), Icons.Default.Person, MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
            MetricCard("Alertas", alerts.toString(), Icons.Default.ErrorOutline, MaterialTheme.colorScheme.error, Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(Modifier.padding(18.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Ocupación actual", fontWeight = FontWeight.SemiBold)
                        Text("$occupied de ${rooms.size} probadores", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape))
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("En uso y limpieza", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            TextButton(onClick = onRooms) { Text("Ver todos") }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(rooms.filter { it.status != RoomStatus.AVAILABLE }.take(5), key = { it.id }) { room ->
                RoomPreviewCard(room, now)
            }
        }
        if (rooms.none { it.status != RoomStatus.AVAILABLE }) {
            EmptyMessage("Sin probadores en uso", "Todos están disponibles.", Icons.Default.CheckCircle)
        }
        Spacer(Modifier.height(22.dp))
        Button(onClick = onRooms, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) {
            Icon(Icons.Default.RoomPreferences, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Gestionar probadores")
        }
        Spacer(Modifier.height(18.dp))
    }
}

@Composable
private fun MetricCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color, modifier: Modifier = Modifier) {
    ElevatedCard(modifier, colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(shape = CircleShape, color = tint.copy(alpha = 0.12f)) { Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.padding(8.dp).size(18.dp)) }
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}

@Composable
private fun RoomPreviewCard(room: FittingRoom, now: Long) {
    val numberColor = if (room.status == RoomStatus.OCCUPIED) MaterialTheme.colorScheme.error else Color(0xFF855500)
    Card(modifier = Modifier.width(210.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                room.id.toString().padStart(2, '0'),
                modifier = Modifier.width(62.dp),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = numberColor
            )
            Spacer(Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    if (room.status == RoomStatus.CLEANING) "Limpieza" else "En uso",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    if (room.status == RoomStatus.CLEANING) "Requerida" else "${room.itemCount} prendas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (room.status == RoomStatus.OCCUPIED) {
                    Text(elapsed(room.startedAt, now), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun RoomsScreen(
    rooms: List<FittingRoom>, now: Long,
    canManage: Boolean,
    onAssign: (FittingRoom) -> Unit,
    onRelease: (FittingRoom) -> Unit,
    onCleaned: (FittingRoom) -> Unit,
    onOccupancyPhoto: (FittingRoom) -> Unit,
    onCleaningPhoto: (FittingRoom) -> Unit
) {
    if (!canManage) {
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp)) {
            Text("Estado de probadores", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            if (rooms.isEmpty()) {
                EmptyMessage("Sin probadores", "Aún no hay estados configurados.", Icons.Default.RoomPreferences)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(rooms, key = { it.id }) { room -> UserStatusCard(room) }
                }
            }
        }
        return
    }
    var filter by rememberSaveable { mutableStateOf(RoomFilter.ALL) }
    val shown = rooms.filter { room ->
        when (filter) {
            RoomFilter.ALL -> true
            RoomFilter.FREE -> room.status == RoomStatus.AVAILABLE
            RoomFilter.OCCUPIED -> room.status == RoomStatus.OCCUPIED
            RoomFilter.ALERT -> room.hasExceededLimit(now)
        }
    }
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text("Filtrar por estado", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(RoomFilter.entries) { item ->
                    FilterChip(selected = filter == item, onClick = { filter = item }, label = { Text(item.label) })
                }
            }
        }
        if (shown.isEmpty()) {
            EmptyMessage("Sin resultados", "No hay probadores en este filtro.", Icons.AutoMirrored.Filled.FactCheck, Modifier.fillMaxWidth().padding(28.dp))
        } else {
            LazyColumn(contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 18.dp, end = 18.dp, bottom = 18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(shown, key = { it.id }) { room ->
                    RoomCard(room, now, onAssign, onRelease, onCleaned, onOccupancyPhoto, onCleaningPhoto)
                }
            }
        }
    }
}

@Composable
private fun UserStatusCard(room: FittingRoom) {
    val statusText = when (room.status) {
        RoomStatus.AVAILABLE -> "Disponible"
        RoomStatus.OCCUPIED -> "Ocupado"
        RoomStatus.CLEANING -> "En limpieza"
    }
    val statusColor = when (room.status) {
        RoomStatus.AVAILABLE -> MaterialTheme.colorScheme.primary
        RoomStatus.OCCUPIED -> MaterialTheme.colorScheme.error
        RoomStatus.CLEANING -> Color(0xFF855500)
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(room.id.toString().padStart(2, '0'), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = statusColor)
            Text(statusText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = statusColor)
        }
    }
}

@Composable
private fun RoomCard(
    room: FittingRoom,
    now: Long,
    onAssign: (FittingRoom) -> Unit,
    onRelease: (FittingRoom) -> Unit,
    onCleaned: (FittingRoom) -> Unit,
    onOccupancyPhoto: (FittingRoom) -> Unit,
    onCleaningPhoto: (FittingRoom) -> Unit
) {
    val alarm = room.hasExceededLimit(now)
    val border = if (alarm) MaterialTheme.colorScheme.error.copy(alpha = 0.45f) else MaterialTheme.colorScheme.outlineVariant
    val numberColor = when {
        room.status == RoomStatus.OCCUPIED -> MaterialTheme.colorScheme.error
        room.status == RoomStatus.AVAILABLE -> MaterialTheme.colorScheme.primary
        room.status == RoomStatus.CLEANING -> Color(0xFF855500)
        else -> MaterialTheme.colorScheme.tertiary
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, border)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = room.id.toString().padStart(2, '0'),
                modifier = Modifier.width(72.dp),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = numberColor,
                maxLines = 1
            )
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                AnimatedVisibility(
                    visible = room.status == RoomStatus.OCCUPIED,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = if (alarm) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            elapsed(room.startedAt, now),
                            fontWeight = FontWeight.SemiBold,
                            color = if (alarm) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            if (alarm) "Revisar" else "Límite 30 min",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (alarm) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (room.status == RoomStatus.OCCUPIED) {
                    Text(
                        "Una foto opcional; la persona elige si aparece o si se fotografían las prendas.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    room.occupancyPhotoPath?.let { PhotoPreview(it, "Foto de ocupación") }
                    OutlinedButton(onClick = { onOccupancyPhoto(room) }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        Text(if (room.occupancyPhotoPath == null) "Tomar foto de ocupación" else "Cambiar foto de ocupación")
                    }
                }
                if (room.status == RoomStatus.CLEANING) {
                    room.cleaningPhotoPath?.let { PhotoPreview(it, "Estado después de limpiar") }
                    OutlinedButton(onClick = { onCleaningPhoto(room) }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        Text(if (room.cleaningPhotoPath == null) "Tomar foto después de limpiar" else "Cambiar foto de limpieza")
                    }
                }
                when (room.status) {
                    RoomStatus.AVAILABLE -> Button(
                        onClick = { onAssign(room) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        Text("Asignar")
                    }
                    RoomStatus.OCCUPIED -> OutlinedButton(
                        onClick = { onRelease(room) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        Text("Finalizar uso")
                    }
                    RoomStatus.CLEANING -> OutlinedButton(
                        onClick = { onCleaned(room) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CleaningServices, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        Text("Marcar disponible")
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoPreview(path: String, label: String) {
    val bitmap = remember(path) {
        runCatching {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(path, bounds)
            val sample = maxOf(1, maxOf(bounds.outWidth, bounds.outHeight) / 640)
            BitmapFactory.decodeFile(path, BitmapFactory.Options().apply { inSampleSize = sample })?.asImageBitmap()
        }.getOrNull()
    }
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = label,
                modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Text("La foto guardada no está disponible.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun StatusPill(room: FittingRoom, now: Long) {
    val warning = room.hasExceededLimit(now)
    val target = when {
        warning -> MaterialTheme.colorScheme.errorContainer
        room.status == RoomStatus.AVAILABLE -> MaterialTheme.colorScheme.primaryContainer
        room.status == RoomStatus.CLEANING -> Color(0xFFFFF0D5)
        else -> MaterialTheme.colorScheme.tertiaryContainer
    }
    val background by animateColorAsState(target, label = "estado")
    val foreground = when {
        warning -> MaterialTheme.colorScheme.onErrorContainer
        room.status == RoomStatus.AVAILABLE -> MaterialTheme.colorScheme.onPrimaryContainer
        room.status == RoomStatus.CLEANING -> Color(0xFF855500)
        else -> MaterialTheme.colorScheme.onTertiaryContainer
    }
    val label = when {
        warning -> "ALERTA"
        room.status == RoomStatus.AVAILABLE -> "LIBRE"
        room.status == RoomStatus.CLEANING -> "LIMPIEZA"
        else -> "OCUPADO"
    }
    Surface(color = background, shape = CircleShape) {
        Text(label, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = foreground, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 0.35.sp)
    }
}

@Composable
private fun AlertsScreen(
    rooms: List<FittingRoom>,
    now: Long,
    onRooms: () -> Unit,
    onRelease: (FittingRoom) -> Unit,
    onOccupancyPhoto: (FittingRoom) -> Unit
) {
    val alerts = rooms.filter { it.hasExceededLimit(now) }.sortedByDescending { now - (it.startedAt ?: now) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Text("Se genera una alerta al superar 30 minutos.", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(20.dp))
        Text("Necesitan atención", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("${alerts.size} ${if (alerts.size == 1) "probador supera" else "probadores superan"} el umbral", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        if (alerts.isEmpty()) {
            EmptyMessage("Sin alertas activas", "No hay probadores sobre el límite de 30 minutos.", Icons.Default.CheckCircle)
        } else {
            alerts.forEach { room ->
                RoomCard(
                    room, now, onAssign = {}, onRelease = onRelease, onCleaned = {},
                    onOccupancyPhoto = onOccupancyPhoto, onCleaningPhoto = {}
                )
                Spacer(Modifier.height(12.dp))
            }
        }
        OutlinedButton(onClick = onRooms, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) { Text("Ver todos los probadores") }
    }
}

@Composable
private fun AssignmentDialog(room: FittingRoom, errors: Map<String, String>, onDismiss: () -> Unit, onSubmit: (String, String) -> Unit) {
    var itemCount by rememberSaveable(room.id) { mutableStateOf("") }
    var category by rememberSaveable(room.id) { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Asignar ${room.name}", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = itemCount, onValueChange = { itemCount = it.filter(Char::isDigit).take(2) },
                    label = { Text("Cantidad de prendas") }, leadingIcon = { Icon(Icons.Default.Inventory2, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true,
                    isError = errors.containsKey("items"), supportingText = { errors["items"]?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )
                Column {
                    OutlinedButton(onClick = { menuExpanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Icon(Icons.Default.LocalMall, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(category.ifBlank { "Seleccionar categoría" })
                    }
                    androidx.compose.material3.DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        listOf("Vestuario", "Calzado", "Accesorios").forEach { option ->
                            androidx.compose.material3.DropdownMenuItem(text = { Text(option) }, onClick = { category = option; menuExpanded = false })
                        }
                    }
                    if (errors.containsKey("category")) Text(errors.getValue("category"), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 14.dp, top = 4.dp))
                }
                errors["room"]?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = { Button(onClick = { onSubmit(itemCount, category) }) { Text("Confirmar asignación") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun EmptyMessage(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Text(title, fontWeight = FontWeight.Bold)
            Text(detail, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun elapsed(startedAt: Long?, now: Long): String {
    if (startedAt == null) return "Sin registro de inicio"
    val minutes = ((now - startedAt).coerceAtLeast(0) / 60_000L).toInt()
    return if (minutes < 60) "$minutes min" else "${minutes / 60} h ${minutes % 60} min"
}
