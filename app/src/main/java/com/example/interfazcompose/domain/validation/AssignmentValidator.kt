package com.example.interfazcompose.domain.validation

class AssignmentValidator {
    fun validate(roomId: Int?, itemCountText: String, category: String): Map<String, String> {
        // Centraliza las reglas para que la pantalla solo muestre los errores recibidos.
        val errors = mutableMapOf<String, String>()
        if (roomId == null) errors["room"] = "Selecciona un probador disponible."
        val items = itemCountText.toIntOrNull()
        if (items == null || items !in 1..12) errors["items"] = "Ingresa entre 1 y 12 prendas."
        if (category.isBlank()) errors["category"] = "Selecciona una categoría."
        return errors
    }
}
