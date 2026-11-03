
package com.example.interfazcompose.domain.validation

// CLASE: AssignmentValidator.
// FUNCIÓN: valida los datos antes de asignar un probador.
// CONEXIÓN: el ViewModel la utiliza para detectar errores.
class AssignmentValidator {

    // VALIDATE: comprueba probador, prendas y categoría.
    // MAP: devuelve los errores asociados a cada campo.
    fun validate(
        roomId: Int?,          // ID del probador; puede ser null.
        itemCountText: String, // Cantidad ingresada como texto.
        category: String       // Categoría seleccionada.
    ): Map<String, String> {

        // MUTABLEMAP: almacena los errores encontrados.
        val errors = mutableMapOf<String, String>()

        // PROBADOR: verifica que se haya seleccionado uno.
        if (roomId == null)
            errors["room"] = "Selecciona un probador disponible."

        // TOINTORNULL: convierte texto a número o devuelve null.
        val items = itemCountText.toIntOrNull()

        // VALIDACIÓN: permite entre 1 y 12 prendas.
        if (items == null || items !in 1..12)
            errors["items"] = "Ingresa entre 1 y 12 prendas."

        // ISBLANK: verifica que la categoría no esté vacía.
        if (category.isBlank())
            errors["category"] = "Selecciona una categoría."

        // RETURN: devuelve los errores encontrados.
        // Si el mapa está vacío, las validaciones pasaron.
        return errors
    }
}

