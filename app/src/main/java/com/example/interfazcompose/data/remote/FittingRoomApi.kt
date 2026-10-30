package com.example.interfazcompose.data.remote

import com.example.interfazcompose.domain.model.FittingRoom
import com.example.interfazcompose.domain.model.RoomStatus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

data class RoomDto(
    val id: Int,
    val name: String,
    val status: String,
    val startedAt: Long?,
    val itemCount: Int,
    val category: String,
    val alertSent: Boolean
) {
    fun toDomain() = FittingRoom(id, name, RoomStatus.valueOf(status), startedAt, itemCount, category, alertSent)
}

data class OccupancyRequest(val itemCount: Int, val category: String, val startedAt: Long)
data class RoomSyncRequest(
    val status: String,
    val startedAt: Long?,
    val itemCount: Int,
    val category: String,
    val alertSent: Boolean
)

interface FittingRoomApi {
    @GET("api/probadores") suspend fun getRooms(): List<RoomDto>
    @PUT("api/probadores/{id}/ocupar") suspend fun occupy(@Path("id") id: Int, @Body body: OccupancyRequest): RoomDto
    @PUT("api/probadores/{id}/liberar") suspend fun release(@Path("id") id: Int): RoomDto
    @PUT("api/probadores/{id}/limpieza-completa") suspend fun finishCleaning(@Path("id") id: Int): RoomDto
    @PUT("api/probadores/{id}/alerta") suspend fun markAlert(@Path("id") id: Int): RoomDto
    @PUT("api/probadores/{id}/sync") suspend fun syncRoom(@Path("id") id: Int, @Body body: RoomSyncRequest): RoomDto
}

object ApiProvider {
    // Android Emulator -> host computer. For a physical phone, replace with the host LAN address.
    private const val BASE_URL = "http://10.0.2.2:8080/"
    val api: FittingRoomApi by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
            .create(FittingRoomApi::class.java)
    }
}
