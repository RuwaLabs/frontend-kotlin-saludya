package pe.edu.upc.saludya.features.configuration.presentation.navigation

object ConfigurationRoutes {
    const val CONFIGURATION = "configuration"
    const val MY_DATA = "configuration/my-data"
    const val CHANGE_PHONE = "configuration/change-phone"
    const val CHANGE_EMAIL = "configuration/change-email"
    const val LINKED_RELATIVES = "configuration/linked-relatives"
    const val CHANGE_PASSWORD = "configuration/change-password"

    const val ARG_DESTINATION = "destination"
    const val VERIFY_CODE = "configuration/verify-code"
    const val VERIFY_CODE_WITH_ARG = "$VERIFY_CODE/{$ARG_DESTINATION}"

    const val DEST_PHONE = "phone"
    const val DEST_EMAIL = "email"

    const val ARG_MINOR_ID = "minorId"
    const val LINKED_MINOR = "configuration/linked-minor"
    const val LINKED_MINOR_WITH_ARG = "$LINKED_MINOR/{$ARG_MINOR_ID}"

    const val LINK_MINOR = "configuration/link-minor"
    const val VERIFY_IDENTITY = "configuration/verify-identity"

    // MINOR_HOME con argumento minorId
    const val MINOR_HOME = "configuration/minor-home"
    const val MINOR_HOME_WITH_ARG = "$MINOR_HOME/{$ARG_MINOR_ID}"

    fun verifyCode(destination: String) = "$VERIFY_CODE/$destination"
    fun linkedMinor(minorId: String) = "$LINKED_MINOR/$minorId"
    fun minorHome(minorId: String) = "$MINOR_HOME/$minorId"
}