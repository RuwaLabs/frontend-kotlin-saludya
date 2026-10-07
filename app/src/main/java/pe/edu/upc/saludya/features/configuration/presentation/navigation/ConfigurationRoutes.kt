package pe.edu.upc.saludya.features.configuration.presentation.navigation

object ConfigurationRoutes {
    const val CONFIGURATION = "configuration"
    const val MY_DATA = "configuration/my-data"
    const val CHANGE_PHONE = "configuration/change-phone"
    const val CHANGE_EMAIL = "configuration/change-email"
    const val LINKED_RELATIVES = "configuration/linked-relatives"
    const val CHANGE_PASSWORD = "configuration/change-password"

    // VERIFY_CODE con argumento "destination"
    const val ARG_DESTINATION = "destination"
    const val VERIFY_CODE = "configuration/verify-code"
    const val VERIFY_CODE_WITH_ARG = "$VERIFY_CODE/{$ARG_DESTINATION}"

    const val DEST_PHONE = "phone"
    const val DEST_EMAIL = "email"

    fun verifyCode(destination: String) = "$VERIFY_CODE/$destination"
}