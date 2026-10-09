# US-23: Solicitud de recuperación de contraseña

Desde “¿Olvidaste tu contraseña?”, el paciente ingresa su correo y solicita un enlace. La app valida el formato antes de enviar `POST /api/v1/user-accounts/recover-password` con el campo `email`.

La confirmación siempre indica que, si existe una cuenta activa, recibirá un enlace. No revela si el correo está registrado. Si el servicio falla, se muestra el error y se permite reintentar. Los envíos repetidos se bloquean mientras la solicitud está en curso.

La pantalla permite solicitar otro enlace al mismo correo. El backend genera el enlace temporal, aplica la vigencia de quince minutos e invalida el anterior. “Ya tengo un enlace” abre el restablecimiento de US-24, y “No tengo acceso a mi correo ni celular” abre la ayuda de US-25. Las pruebas verifican las solicitudes y la respuesta genérica, pero la entrega del correo requiere comprobar el servidor desplegado.
