package com.example.interfazcompose.domain.validation

/** Cuenta fija para demostrar el acceso administrador sin requerir un servicio de autenticación. */
object AdminLoginValidator {
    const val USERNAME = "admin"
    const val PASSWORD = "RinconSur2026!"

    fun isValid(username: String, password: String): Boolean =
        username.trim() == USERNAME && password == PASSWORD
}
