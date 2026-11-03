package com.example.interfazcompose.ui.viewmodel

import android.app.Application

import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.viewModelScope

import com.example.interfazcompose.data.local.RoomUsageEntity

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

import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext

import java.util.Calendar

// VIEWMODEL: controla estados y acciones de los probadores.
// Recibe Repository y Validator para trabajar con datos y reglas.
class FittingRoomViewModel(
    application: Application,
    private val repository: FittingRoomRepository,
    private val validator: AssignmentValidator = AssignmentValidator()
) : AndroidViewModel(application) {

    // STATEFLOW: probadores observables; stateIn adapta el flujo del Repository.
    val rooms: StateFlow<List<FittingRoom>> = repository.rooms.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    // MUTABLESTATEFLOW: guarda la hora actual y permite actualizarla.
    private val _now: MutableStateFlow<Long> =
        MutableStateFlow(System.currentTimeMillis())

    // STATEFLOW: expone la hora a la Vista en modo solo lectura.
    val now: StateFlow<Long> = _now.asStateFlow()

    // VALIDACIONES: guarda errores por campo del formulario.
    private val _formErrors: MutableStateFlow<Map<String, String>> =
        MutableStateFlow(emptyMap())

    // ESTADO PÚBLICO: la Vista observa los errores.
    val formErrors: StateFlow<Map<String, String>> =
        _formErrors.asStateFlow()

    // HISTORIAL: atenciones del día seleccionado.
    private val _dailyHistory:
            MutableStateFlow<List<RoomUsageEntity>> =
        MutableStateFlow(emptyList())

    // STATEFLOW: la pantalla observa el historial.
    val dailyHistory: StateFlow<List<RoomUsageEntity>> =
        _dailyHistory.asStateFlow()

    // CONTADOR: cantidad de atenciones del día.
    private val _dailyUsageCount: MutableStateFlow<Int> =
        MutableStateFlow(0)

    // STATEFLOW: expone el contador a la pantalla.
    val dailyUsageCount: StateFlow<Int> =
        _dailyUsageCount.asStateFlow()

    // ERROR DE EDICIÓN: mensaje si falla la corrección de prendas.
    private val _editError: MutableStateFlow<String?> =
        MutableStateFlow(null)

    // STATEFLOW: muestra el error sin permitir cambiarlo desde la Vista.
    val editError: StateFlow<String?> = _editError.asStateFlow()

    // INIT: se ejecuta al crear el ViewModel.
    init {

        // CORRUTINA IO: prepara los datos locales sin bloquear la Vista.
        viewModelScope.launch(Dispatchers.IO) {

            // REPOSITORY: carga datos iniciales solo si la BD está vacía.
            repository.seedIfEmpty()

        }

        // CORRUTINA: se cancela al destruirse el ViewModel.
        viewModelScope.launch {

            // RELOJ: actualiza la hora cada 15 segundos con delay().
            while (true) {

                _now.value = System.currentTimeMillis()

                delay(15_000)
            }
        }

        viewModelScope.launch {

            // COLLECT: observa cambios de probadores para revisar alertas.
            rooms.collect { list ->

                val time: Long = System.currentTimeMillis()

                // FILTER: detecta ocupaciones largas sin alerta previa.
                list.filter {
                    !it.alertSent && it.hasExceededLimit(time)
                }.forEach { room ->

                    if (
                        OccupancyNotifications.notifyLongOccupancy(
                            getApplication(),
                            room
                        )
                    ) {

                        // REPOSITORY: registra que ya se notificó este probador.
                        repository.markAlertSent(room)
                    }
                }
            }
        }
    }

    // ASSIGN (CREATE): valida prendas y registra una asignación.
    fun assign(
        room: FittingRoom?,
        itemCount: String,
        category: String,
        occupancyPhotoPath: String? = null,
        onResult: (Boolean) -> Unit
    ) = viewModelScope.launch {

        // VALIDATOR: comprueba probador, cantidad y categoría.
        val errors: Map<String, String> =
            validator.validate(room?.id, itemCount, category)

        _formErrors.value = errors

        if (errors.isNotEmpty() || room == null) {

            onResult(false)

            return@launch
        }

        // RUNCATCHING: captura excepciones del Repository.
        val result = runCatching {

            // WITHCONTEXT(IO): ejecuta el guardado fuera del hilo principal.
            withContext(Dispatchers.IO) {
                repository.assign(
                    room,
                    itemCount.toInt(),
                    category,
                    occupancyPhotoPath
                )
            }
        }

        if (result.isSuccess) {

            _formErrors.value = emptyMap()

            onResult(true)

        } else {

            onResult(false)
        }
    }

    // EDIT (UPDATE): corrige prendas sin registrar otra atención.
    fun editItemCount(
        roomId: Int,
        newItemCount: String,
        onResult: (Boolean) -> Unit
    ) = viewModelScope.launch {

        // TOINTORNULL: convierte texto; null si no es número.
        val quantity: Int? = newItemCount.toIntOrNull()

        if (quantity == null || quantity <= 0) {

            _editError.value = "Ingresa una cantidad válida"

            onResult(false)
            return@launch
        }

        _editError.value = null

        val result = runCatching {

            withContext(Dispatchers.IO) {

                repository.editItemCount(roomId, quantity)
            }
        }

        if (result.isSuccess) {

            onResult(true)

        } else {

            _editError.value =
                result.exceptionOrNull()?.message
                    ?: "No se pudo corregir la cantidad"

            onResult(false)
        }
    }

    // READ: observa el historial diario de un probador.
    fun loadDailyHistory(
        roomId: Int,
        dayMillis: Long = System.currentTimeMillis()
    ) = viewModelScope.launch {

        repository.observeDailyHistory(
            roomId,
            dayMillis
        ).collect { history ->

            _dailyHistory.value = history

            _dailyUsageCount.value = history.size
        }
    }

    // READ: observa el historial de todos los probadores del día.
    fun loadAllDailyHistory(
        dayMillis: Long = System.currentTimeMillis()
    ) = viewModelScope.launch {

        repository.observeAllDailyHistory(
            dayMillis
        ).collect { history ->

            _dailyHistory.value = history

            _dailyUsageCount.value = history.size
        }
    }

    // RELEASE: libera el probador y registra la salida.
    fun release(room: FittingRoom) = viewModelScope.launch(
        Dispatchers.IO
    ) {

        repository.release(room)
    }

    // CLEANING: finaliza la limpieza y habilita el probador.
    fun finishCleaning(room: FittingRoom) = viewModelScope.launch(
        Dispatchers.IO
    ) {

        repository.finishCleaning(room)
    }

    // FOTO OCUPACIÓN: guarda la ruta de la imagen mediante Repository.
    fun saveOccupancyPhoto(
        roomId: Int,
        path: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        repository.saveOccupancyPhoto(roomId, path)
    }

    // FOTO LIMPIEZA: guarda la ruta de la imagen mediante Repository.
    fun saveCleaningPhoto(
        roomId: Int,
        path: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        repository.saveCleaningPhoto(roomId, path)
    }
}

// FACTORY: crea el ViewModel entregándole Application y Repository.
class FittingRoomViewModelFactory(
    private val application: Application,
    private val repository: FittingRoomRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")

    // CREATE: instancia el ViewModel solicitado.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return FittingRoomViewModel(
            application,
            repository
        ) as T
    }
}
