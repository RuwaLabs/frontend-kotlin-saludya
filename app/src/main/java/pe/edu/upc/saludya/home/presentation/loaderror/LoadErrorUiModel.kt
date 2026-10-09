package pe.edu.upc.saludya.home.presentation.loaderror

data class LoadErrorUiModel(
    val title: String = "Mis citas",
    val heading: String = "No pudimos cargar tus citas",
    val description: String = "Revisa tu conexión y vuelve a intentarlo.",
    val buttonText: String = "Reintentar",
)
