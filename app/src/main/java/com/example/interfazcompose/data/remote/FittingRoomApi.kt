
package com.example.interfazcompose.data.remote

// MODEL: clases que representan los probadores.
import com.example.interfazcompose.domain.model.FittingRoom
import com.example.interfazcompose.domain.model.RoomStatus

// RETROFIT: permite comunicarse con el backend mediante HTTP.
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// CLASE: RoomDto (Data Transfer Object).
// FUNCIÓN: representa los datos recibidos desde el backend.
// CONEXIÓN: convierte la respuesta de la API al modelo FittingRoom.
data class RoomDto(
    val id: Int,              // Identificador del probador.
    val name: String,         // Nombre del probador.
    val status: String,       // Estado recibido como texto.
    val startedAt: Long?,     // Hora de ingreso; admite null.
    val itemCount: Int,       // Cantidad de prendas.
    val category: String,     // Categoría seleccionada.
    val alertSent: Boolean    // Indica si se envió una alerta.
) {

    // TODOMAIN: convierte el DTO al modelo FittingRoom.
    fun toDomain() = FittingRoom(
        id,
        name,
        RoomStatus.valueOf(status),
        startedAt,
        itemCount,
        category,
        alertSent
    )
}

// CLASE: OccupancyRequest.
// FUNCIÓN: contiene los datos para ocupar un probador.
// CONEXIÓN: Retrofit la envía al backend mediante @Body.
data class OccupancyRequest(
    val itemCount: Int,   // Cantidad de prendas.
    val category: String, // Categoría de las prendas.
    val startedAt: Long   // Hora de inicio de la atención.
)

// CLASE: RoomSyncRequest.
// FUNCIÓN: contiene los datos para sincronizar un probador.
// CONEXIÓN: el Repository la envía al backend mediante la API.
data class RoomSyncRequest(
    val status: String,       // Estado actual.
    val startedAt: Long?,     // Hora de ingreso.
    val itemCount: Int,       // Cantidad de prendas.
    val category: String,     // Categoría.
    val alertSent: Boolean    // Estado de la alerta.
)

// INTERFAZ: FittingRoomApi.
// FUNCIÓN: define las operaciones HTTP del backend.
// CONEXIÓN: el Repository utiliza Retrofit para llamarlas.
interface FittingRoomApi {

    // GET: obtiene todos los probadores desde el servidor.
    @GET("api/probadores")
    suspend fun getRooms(): List<RoomDto>

    // PUT: ocupa un probador y envía los datos de la atención.
    @PUT("api/probadores/{id}/ocupar")
    suspend fun occupy(
        @Path("id") id: Int,
        @Body body: OccupancyRequest
    ): RoomDto

    // PUT: libera un probador ocupado.
    @PUT("api/probadores/{id}/liberar")
    suspend fun release(
        @Path("id") id: Int
    ): RoomDto

    // PUT: marca como finalizada la limpieza del probador.
    @PUT("api/probadores/{id}/limpieza-completa")
    suspend fun finishCleaning(
        @Path("id") id: Int
    ): RoomDto

    // PUT: informa al servidor que se marcó una alerta.
    @PUT("api/probadores/{id}/alerta")
    suspend fun markAlert(
        @Path("id") id: Int
    ): RoomDto

    // PUT: sincroniza el estado del probador con el backend.
    @PUT("api/probadores/{id}/sync")
    suspend fun syncRoom(
        @Path("id") id: Int,
        @Body body: RoomSyncRequest
    ): RoomDto
}

// OBJETO: ApiProvider.
// FUNCIÓN: configura Retrofit y la dirección del servidor.
// CONEXIÓN: proporciona FittingRoomApi al Repository.
object ApiProvider {

    // BASE_URL: 10.0.2.2 permite al emulador acceder al PC.
    // 8080: puerto donde se ejecuta Spring Boot.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // LAZY: crea la API solamente cuando se utiliza por primera vez.
    val api: FittingRoomApi by lazy {

        // BUILDER: configura la conexión HTTP con Retrofit.
        Retrofit.Builder()
            .baseUrl(BASE_URL)

            // GSON: convierte automáticamente JSON a objetos Kotlin.
            .addConverterFactory(GsonConverterFactory.create())

            // BUILD: construye Retrofit.
            .build()

            // CREATE: genera la implementación de FittingRoomApi.
            .create(FittingRoomApi::class.java)
    }
}

