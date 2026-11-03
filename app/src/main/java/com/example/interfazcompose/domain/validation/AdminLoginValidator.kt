
package com.example.interfazcompose.domain.validation

// OBJETO: AdminLoginValidator.
// FUNCIÓN: valida el usuario y contraseña del administrador.
// CONEXIÓN: HomeScreen lo utiliza para permitir el acceso.
// SEGURIDAD: credenciales fijas solo para demostración académica.
object AdminLoginValidator {

    // CONST VAL: constantes que no cambian durante la ejecución.
    const val USERNAME = "admin"
    const val PASSWORD = "RinconSur2026!"

    // ISVALID: verifica que las credenciales sean correctas.
    // TRIM: elimina espacios al inicio y final del usuario.
    // &&: ambas condiciones deben cumplirse.
    // BOOLEAN: devuelve true si son correctas y false si no.
    fun isValid(username: String, password: String): Boolean =
        username.trim() == USERNAME && password == PASSWORD
}
