package pe.edu.upc.saludya.features.configuration.presentation.navigation

/**
 * Rutas de navegación del feature `configuration`.
 *
 * Cada constante corresponde a una pantalla del PDF:
 *  - CONFIGURATION      → H01
 *  - ADDITIONAL_RULES   → H02
 *  - ATTENTION_INTERVALS→ H13
 *  - EDIT_RULE          → H03 (con argumento ruleId)
 *  - DAILY_INDICATORS   → H06
 *  - SPECIALTY_DEMAND   → H07
 *  - EXPORT_REPORT      → H08
 *
 * Los modales (H04, H05, H10, H11, H12) NO son rutas: son estados internos
 * de las pantallas que los muestran.
 */
object ConfigurationRoutes {
    const val CONFIGURATION = "configuration"
    const val ADDITIONAL_RULES = "configuration/additional-rules"
    const val ATTENTION_INTERVALS = "configuration/attention-intervals"
    const val EDIT_RULE = "configuration/edit-rule"
    const val DAILY_INDICATORS = "configuration/daily-indicators"
    const val SPECIALTY_DEMAND = "configuration/specialty-demand"
    const val EXPORT_REPORT = "configuration/export-report"

    // Argumento de EDIT_RULE
    const val ARG_RULE_ID = "ruleId"
    const val EDIT_RULE_WITH_ARG = "$EDIT_RULE/{$ARG_RULE_ID}"

    fun editRule(ruleId: String) = "$EDIT_RULE/$ruleId"
}