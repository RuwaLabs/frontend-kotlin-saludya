package pe.edu.upc.saludya.main

import kotlinx.serialization.Serializable

/** Destino raíz del área autenticada (tabs + reservas). */
@Serializable
data object MainRoute

@Serializable
data object InicioRoute

@Serializable
data object AsistenciaRoute

@Serializable
data object ConfiguracionRoute
