# Plan de trabajo · Parcial 2 DSY1105

## Objetivo del MVP

Entregar una interfaz funcional para que el personal de Vestuario revise y gestione probadores, con datos sintéticos, validaciones claras, persistencia local, API REST, dos recursos nativos y navegación entre vistas.

## Tareas para tablero compartido

| Estado | Tarea | Responsable sugerido | Criterio de cierre |
|---|---|---|---|
| Hecho | Acordar caso, perfiles y reglas de negocio | Pareja | Caso Retail Probadores delimitado; umbral de alerta: 30 min |
| Hecho | Diseñar estructura Compose/Material 3 y navegación | Integrante 1 | Inicio, Probadores y Alertas accesibles |
| Hecho | Implementar estados y formulario con validación desacoplada | Integrante 1 | Asignación entre 1–12 prendas; categoría obligatoria |
| Hecho | Crear Room, repositorio y modo sin conexión | Integrante 1 | Los cambios permanecen tras reiniciar la app |
| Hecho | Crear API REST Spring Boot y pruebas unitarias | Integrante 2 | Consulta, asignación, liberación, limpieza y alerta |
| Hecho | Conectar API con Retrofit | Integrante 1 + 2 | Sincroniza con backend; conserva modo local sin servidor |
| Hecho | Integrar notificaciones y vibración nativa | Integrante 2 | Aviso por ocupación prolongada y respuesta háptica |
| Pendiente equipo | Ejecutar revisión en emulador y teléfono físico | Pareja | Verificar permisos, estados, red, persistencia y textos |
| Pendiente equipo | Completar autores y guía de demostración | Pareja | README con nombres reales y pasos probados |
| Pendiente equipo | Crear tablero, repositorio público y registrar commits reales | Pareja | Enlaces compartidos con docente y commits técnicos del equipo |
| Pendiente equipo | Preparar entrega en AVA y defensa | Pareja | Código fuente coincidente con el repositorio |

## Reglas del flujo

1. Una asignación solo se registra si el probador está disponible, la cantidad está entre 1 y 12 y se seleccionó categoría.
2. El sistema no pide nombres ni datos personales de clientes.
3. Un uso que alcanza 30 minutos genera una alerta para revisión del personal.
4. La alerta invita a revisar el estado; el personal confirma antes de finalizar el uso y luego marca disponible tras limpiar.
5. Los cambios locales se guardan primero en Room. La API se sincroniza cuando está disponible.
6. Al terminar una ocupación, el probador pasa a limpieza; el personal lo marca disponible después de revisarlo.

## Revisión de la rúbrica

- **IE 2.1.1**: jerarquía visual, distribución y navegación inferior entre tres vistas.
- **IE 2.1.2**: campo cantidad y categoría con errores visibles e iconografía.
- **IE 2.2.1**: reglas en `AssignmentValidator`, fuera de Compose.
- **IE 2.2.2**: cambios animados de estado y progreso de ocupación.
- **IE 2.3.1**: paquetes por dominio/datos/UI/plataforma; MVVM; Room y SQLite.
- **IE 2.3.2**: preparar tablero, GitHub y commits con la pareja; evidencia real pendiente de acciones del equipo.
- **IE 2.4.1**: notificaciones Android y vibración háptica.
