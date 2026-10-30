package com.example.interfazcompose.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.interfazcompose.data.repository.FittingRoomRepository
import com.example.interfazcompose.domain.model.FittingRoom
import com.example.interfazcompose.domain.validation.AssignmentValidator
import com.example.interfazcompose.platform.OccupancyNotifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FittingRoomViewModel(
    application: Application,
    private val repository: FittingRoomRepository,
    private val validator: AssignmentValidator = AssignmentValidator()
) : AndroidViewModel(application) {
    val rooms: StateFlow<List<FittingRoom>> = repository.rooms.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )
    private val _now = MutableStateFlow(System.currentTimeMillis())
    val now: StateFlow<Long> = _now.asStateFlow()
    private val _formErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val formErrors: StateFlow<Map<String, String>> = _formErrors.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Carga datos iniciales solo en una instalación vacía; luego consulta el servidor.
            repository.seedIfEmpty()
            repository.refreshFromServer()
        }
        viewModelScope.launch {
            // Actualiza el reloj que muestran los tiempos y el límite de ocupación.
            while (true) {
                _now.value = System.currentTimeMillis()
                delay(15_000)
            }
        }
        viewModelScope.launch {
            // Observa los cambios y notifica una sola vez por ocupación prolongada.
            rooms.collect { list ->
                val time = System.currentTimeMillis()
                list.filter { !it.alertSent && it.hasExceededLimit(time) }.forEach { room ->
                    if (OccupancyNotifications.notifyLongOccupancy(getApplication(), room)) {
                        repository.markAlertSent(room)
                    }
                }
            }
        }
    }

    fun assign(room: FittingRoom?, itemCount: String, category: String, occupancyPhotoPath: String? = null, onResult: (Boolean) -> Unit) =
        viewModelScope.launch {
            // Validar antes de guardar evita cerrar el formulario con datos incorrectos.
            val errors = validator.validate(room?.id, itemCount, category)
            _formErrors.value = errors
            if (errors.isNotEmpty() || room == null) {
                onResult(false)
                return@launch
            }
            repository.assign(room, itemCount.toInt(), category, occupancyPhotoPath)
            _formErrors.value = emptyMap()
            onResult(true)
            // La sincronización ocurre después del guardado y cierre del formulario.
            viewModelScope.launch(Dispatchers.IO) { repository.syncPendingRoom(room.id) }
        }

    fun release(room: FittingRoom) = viewModelScope.launch(Dispatchers.IO) { repository.release(room) }

    fun finishCleaning(room: FittingRoom) = viewModelScope.launch(Dispatchers.IO) { repository.finishCleaning(room) }

    fun saveOccupancyPhoto(roomId: Int, path: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveOccupancyPhoto(roomId, path)
    }

    fun saveCleaningPhoto(roomId: Int, path: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveCleaningPhoto(roomId, path)
    }
}

class FittingRoomViewModelFactory(
    private val application: Application,
    private val repository: FittingRoomRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        FittingRoomViewModel(application, repository) as T
}
