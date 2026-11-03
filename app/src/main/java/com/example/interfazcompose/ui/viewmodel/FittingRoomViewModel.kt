
package com.example.interfazcompose.ui.viewmodel

// Application: permite acceder al contexto de Android.
import android.app.Application

// AndroidViewModel: ViewModel que puede utilizar Application.
import androidx.lifecycle.AndroidViewModel

// ViewModelProvider: permite crear un ViewModel con parámetros.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// viewModelScope: administra corrutinas ligadas al ViewModel.
import androidx.lifecycle.viewModelScope

// Entidad Room que representa una atención del historial.
import com.example.interfazcompose.data.local.RoomUsageEntity

// Repository: conecta el ViewModel con los datos.
import com.example.interfazcompose.data.repository.FittingRoomRepository

// Modelo de negocio que representa un probador.
import com.example.interfazcompose.domain.model.FittingRoom

// Clase que centraliza las validaciones del formulario.
import com.example.interfazcompose.domain.validation.AssignmentValidator

// Clase que administra las notificaciones de ocupación.
import com.example.interfazcompose.platform.OccupancyNotifications

// Dispatchers.IO: contexto para operaciones de entrada y salida.
import kotlinx.coroutines.Dispatchers

// delay(): suspende una corrutina durante un tiempo.
import kotlinx.coroutines.delay

// MutableStateFlow: estado observable que puede modificarse.
import kotlinx.coroutines.flow.MutableStateFlow

// SharingStarted: configura cuándo se comparte un flujo.
import kotlinx.coroutines.flow.SharingStarted

// StateFlow: flujo observable de estado.
import kotlinx.coroutines.flow.StateFlow

// asStateFlow(): expone un estado sin permitir modificarlo.
import kotlinx.coroutines.flow.asStateFlow

// collect(): recibe las emisiones de un Flow.
import kotlinx.coroutines.flow.collect

// stateIn(): convierte un Flow en StateFlow.
import kotlinx.coroutines.flow.stateIn

// launch(): inicia una corrutina.
import kotlinx.coroutines.launch

// withContext(): cambia el contexto de una corrutina existente.
import kotlinx.coroutines.withContext

// Calendar: permite calcular las fechas del historial.
import java.util.Calendar


// class: representa la capa ViewModel de MVVM.
// Recibe Application, Repository y Validator mediante el constructor.
class FittingRoomViewModel(
    application: Application, // Contexto de la aplicación.
    private val repository: FittingRoomRepository, // Acceso a datos.
    private val validator: AssignmentValidator = AssignmentValidator()
    // Validator: objeto encargado de validar asignaciones.
) : AndroidViewModel(application) {

    // val: propiedad de solo lectura. // StateFlow<List<FittingRoom>>: estado observable con los probadores.
    // stateIn(): transforma el Flow del Repository en StateFlow.
    val rooms: StateFlow<List<FittingRoom>> = repository.rooms.stateIn(
        viewModelScope, // Alcance de corrutinas del ViewModel.
        SharingStarted.WhileSubscribed(5_000), // Mantiene el flujo 5 segundos.
        emptyList() // Valor inicial: lista vacía.
    )

    // private: solo puede modificarse dentro de esta clase.
    // MutableStateFlow<Long>: almacena la hora actual.
    private val _now: MutableStateFlow<Long> =
        MutableStateFlow(System.currentTimeMillis())

    // StateFlow<Long>: expone la hora a la interfaz.
    val now: StateFlow<Long> = _now.asStateFlow()

    // Map<String, String>: relaciona campos con mensajes de error.
    // MutableStateFlow: permite actualizar los errores del formulario.
    private val _formErrors: MutableStateFlow<Map<String, String>> =
        MutableStateFlow(emptyMap())

    // Estado público de solo lectura para mostrar validaciones.
    val formErrors: StateFlow<Map<String, String>> =
        _formErrors.asStateFlow()

    // MutableStateFlow: almacena el historial del día seleccionado.
    private val _dailyHistory:
            MutableStateFlow<List<RoomUsageEntity>> =
        MutableStateFlow(emptyList())

    // StateFlow: permite que la pantalla observe el historial.
    val dailyHistory: StateFlow<List<RoomUsageEntity>> =
        _dailyHistory.asStateFlow()

    // MutableStateFlow<Int>: contador diario de atenciones.
    private val _dailyUsageCount: MutableStateFlow<Int> =
        MutableStateFlow(0)

    // StateFlow<Int>: contador observable desde la interfaz.
    val dailyUsageCount: StateFlow<Int> =
        _dailyUsageCount.asStateFlow()

    // MutableStateFlow<String?>: error opcional de edición.
    private val _editError: MutableStateFlow<String?> =
        MutableStateFlow(null)

    // Estado público para mostrar errores al corregir prendas.
    val editError: StateFlow<String?> = _editError.asStateFlow()


    // init: bloque que se ejecuta al crear el ViewModel.
    init {

        // CORRUTINA: launch inicia una corrutina en viewModelScope.
        // Dispatchers.IO: apropiado para consultas de datos.
        viewModelScope.launch(Dispatchers.IO) {

            // suspend fun del Repository: inicializa SQLite si está vacío.
            repository.seedIfEmpty()

            // No consultamos el servidor automáticamente. // Así protegemos las modificaciones locales del historial.
        }

        // CORRUTINA: actualiza periódicamente la hora.
        viewModelScope.launch {

            // while(true): repite el proceso mientras siga activa.
            // La corrutina se cancela al destruirse el ViewModel.
            while (true) {

                // .value: modifica el valor de MutableStateFlow.
                _now.value = System.currentTimeMillis()

                // delay(): suspende 15 segundos sin bloquear el hilo.
                delay(15_000)
            }
        }

        // CORRUTINA: observa los probadores y revisa las alertas.
        viewModelScope.launch {

            // collect(): recibe las listas emitidas por StateFlow.
            rooms.collect { list ->

                // Long: fecha y hora actual en milisegundos.
                val time: Long = System.currentTimeMillis()

                // filter(): selecciona los probadores que requieren alerta. // !: operador lógico de negación.
                // forEach(): recorre cada probador seleccionado.
                list.filter {
                    !it.alertSent && it.hasExceededLimit(time)
                }.forEach { room ->

                    // notifyLongOccupancy(): intenta enviar notificación. // if: comprueba si pudo notificarse.
                    if (
                        OccupancyNotifications.notifyLongOccupancy(
                            getApplication(),
                            room
                        )
                    ) {

                        // suspend fun: registra que la alerta fue enviada.
                        repository.markAlertSent(room)
                    }
                }
            }
        }
    }


    // fun: función normal que inicia una corrutina.
    // CREATE: valida y asigna prendas a un probador.
    fun assign(
        room: FittingRoom?, // ? indica que puede ser null.
        itemCount: String, // Cantidad ingresada como texto.
        category: String, // Categoría seleccionada.
        occupancyPhotoPath: String? = null, // Fotografía opcional.
        onResult: (Boolean) -> Unit // Callback que informa éxito o fallo.
    ) = viewModelScope.launch {

        // validate(): comprueba los campos mediante el Validator.
        // ?. permite obtener el ID solo si room no es null.
        val errors: Map<String, String> =
            validator.validate(room?.id, itemCount, category)

        // Actualiza los mensajes de validación observables.
        _formErrors.value = errors

        // ||: operador OR; basta con que una condición sea verdadera.
        if (errors.isNotEmpty() || room == null) {

            // Callback: informa que no se pudo asignar.
            onResult(false)

            // return@launch: termina esta corrutina.
            return@launch
        }

        // runCatching: captura errores del guardado local.
        val result = runCatching {

            // withContext(IO): cambia a un contexto para datos.
            // suspend fun: guarda el probador y su atención.
            withContext(Dispatchers.IO) {
                repository.assign(
                    room,
                    itemCount.toInt(), // Convierte String a Int.
                    category,
                    occupancyPhotoPath
                )
            }
        }

        // isSuccess: Boolean que indica si el guardado funcionó.
        if (result.isSuccess) {

            // emptyMap(): limpia los errores anteriores.
            _formErrors.value = emptyMap()

            // Callback: informa que la asignación terminó correctamente.
            onResult(true)

        } else {

            // Callback: informa que ocurrió un error al guardar.
            onResult(false)
        }
    }


    // fun: inicia una corrutina para corregir prendas.
    // UPDATE: modifica la atención actual, sin crear otro uso.
    fun editItemCount(
        roomId: Int, // ID del probador.
        newItemCount: String, // Nueva cantidad ingresada como texto.
        onResult: (Boolean) -> Unit // Callback de éxito o fallo.
    ) = viewModelScope.launch {

        // toIntOrNull(): convierte texto a Int o devuelve null.
        val quantity: Int? = newItemCount.toIntOrNull()

        // ?: permite establecer un valor si la conversión falla.
        // if: valida que la cantidad sea mayor que cero.
        if (quantity == null || quantity <= 0) {

            // MutableStateFlow: guarda el mensaje de error.
            _editError.value = "Ingresa una cantidad válida"

            // Informa que la edición no fue exitosa.
            onResult(false)
            return@launch
        }

        // Limpia los errores de ediciones anteriores.
        _editError.value = null

        // runCatching: captura errores durante la actualización.
        val result = runCatching {

            // CORRUTINA: withContext cambia al dispatcher IO.
            withContext(Dispatchers.IO) {

                // suspend fun del Repository: actualiza SQLite.
                repository.editItemCount(roomId, quantity)
            }
        }

        // isSuccess: comprueba si terminó sin excepciones.
        if (result.isSuccess) {

            // Informa que la edición fue exitosa.
            onResult(true)

        } else {

            // Guarda un mensaje para mostrar en la pantalla.
            _editError.value =
                result.exceptionOrNull()?.message
                    ?: "No se pudo corregir la cantidad"

            // Informa que ocurrió un error.
            onResult(false)
        }
    }


    // fun: inicia una corrutina para cargar el historial.
    // READ: obtiene las atenciones de un probador en un día.
    fun loadDailyHistory(
        roomId: Int, // Identificador del probador.
        dayMillis: Long = System.currentTimeMillis()
        // Fecha consultada; por defecto, el día actual.
    ) = viewModelScope.launch {

        // collect(): observa las actualizaciones del historial.
        repository.observeDailyHistory(
            roomId,
            dayMillis
        ).collect { history ->

            // .value: actualiza la lista observable.
            _dailyHistory.value = history

            // .size: cuenta las atenciones de la lista.
            _dailyUsageCount.value = history.size
        }
    }


    // fun: consulta todas las atenciones del día.
    fun loadAllDailyHistory(
        dayMillis: Long = System.currentTimeMillis()
    ) = viewModelScope.launch {

        // collect(): recibe cambios en el historial general.
        repository.observeAllDailyHistory(
            dayMillis
        ).collect { history ->

            // Actualiza el estado observable del historial.
            _dailyHistory.value = history

            // Calcula el total de atenciones registradas.
            _dailyUsageCount.value = history.size
        }
    }


    // fun: inicia una corrutina para liberar un probador.
    fun release(room: FittingRoom) = viewModelScope.launch(
        Dispatchers.IO // Dispatcher para operaciones de datos.
    ) {

        // suspend fun del Repository: registra salida y limpieza.
        repository.release(room)
    }


    // fun: inicia una corrutina para terminar la limpieza.
    fun finishCleaning(room: FittingRoom) = viewModelScope.launch(
        Dispatchers.IO
    ) {

        // suspend fun del Repository: deja el probador disponible.
        repository.finishCleaning(room)
    }


    // fun: inicia una corrutina para guardar una fotografía.
    fun saveOccupancyPhoto(
        roomId: Int, // Identificador del probador.
        path: String // Ruta local de la imagen.
    ) = viewModelScope.launch(Dispatchers.IO) {

        // suspend fun: guarda la ruta en la base de datos.
        repository.saveOccupancyPhoto(roomId, path)
    }


    // fun: inicia una corrutina para guardar foto de limpieza.
    fun saveCleaningPhoto(
        roomId: Int,
        path: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        // suspend fun: actualiza la ruta de fotografía en SQLite.
        repository.saveCleaningPhoto(roomId, path)
    }
}


// Factory: permite crear un ViewModel que recibe parámetros.
// ViewModelProvider.Factory: interfaz de Android Lifecycle.
class FittingRoomViewModelFactory(
    private val application: Application, // Contexto Android.
    private val repository: FittingRoomRepository // Acceso a datos.
) : ViewModelProvider.Factory {

    // @Suppress: anotación que evita una advertencia del compilador.
    // UNCHECKED_CAST: indica una conversión genérica no verificable.
    @Suppress("UNCHECKED_CAST")

    // override: implementa una función de la interfaz Factory.
    // <T : ViewModel>: tipo genérico que hereda de ViewModel.
    override fun <T : ViewModel> create(
        modelClass: Class<T> // Clase del ViewModel solicitado.
    ): T {

        // Crea el ViewModel con sus dependencias.
        // as T: convierte el objeto al tipo genérico esperado.
        return FittingRoomViewModel(
            application,
            repository
        ) as T
    }
}
