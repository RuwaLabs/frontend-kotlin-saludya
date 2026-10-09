# US-24: Restablecimiento de contraseña

El enlace de recuperación abre el formulario para definir una nueva contraseña. La app comprueba la política de contraseña y que la confirmación coincida antes de enviar `token`, `password` y `confirmPassword` a `POST /api/v1/user-accounts/reset-password`. Solo una respuesta `204` muestra la confirmación de cambio y permite volver al inicio de sesión.

El servidor valida la vigencia de quince minutos y el uso único del enlace. Si devuelve `IAM_RESET_TOKEN_INVALID`, la app indica que expiró o dejó de ser válido, borra las contraseñas y solicita un enlace nuevo. Después de actualizar la contraseña, también se retira la sesión local; la revocación de las sesiones anteriores corresponde al backend.

El enlace debe usar HTTPS, la ruta `/reset-password` y el dominio configurado en la propiedad Gradle `saludya.resetLinkHost`. El valor provisional es `example.invalid`, igual que en la configuración actual del backend. Para la entrega, el equipo debe sustituirlo por su dominio y hacer coincidir `iam.password-reset.url` con `https://<dominio>/reset-password`. La apertura automática requiere el archivo de asociación de Android en `/.well-known/assetlinks.json`, con el paquete y el certificado de firma de la app. Esa configuración del dominio queda pendiente; desde la recuperación también se puede pegar el enlace recibido.

El token y las contraseñas permanecen en memoria y se omiten de las representaciones de estado. Las pruebas cubren enlaces ajenos, tokens duplicados, contraseñas distintas, respuestas del servidor y bloqueo de envíos repetidos.
