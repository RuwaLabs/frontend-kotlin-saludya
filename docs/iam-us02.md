# US-02: Registro de credenciales de acceso

Después de verificar su identidad, el paciente registra su correo, contraseña, confirmación y celular. El formulario señala los campos incorrectos y permite corregirlos antes de continuar.

El correo debe tener un formato válido y el celular nueve dígitos numéricos. Las contraseñas deben coincidir y tener al menos ocho caracteres, con letras y números. También se respeta el límite de 72 bytes de BCrypt usado por el backend.

Al continuar, la app solicita el código mediante `POST /api/v1/user-accounts/send-verification-code`. Los datos verificados y las credenciales se conservan en memoria hasta completar US-03. La cuenta todavía no se crea en este paso. Si falla la solicitud, el formulario permite reintentar.

La verificación se realiza por correo, según el cambio acordado por el equipo. El celular se conserva como dato de contacto y no se utiliza SMS. El backend comprueba la disponibilidad del correo al crear la cuenta en US-03; si ya está registrado, la app vuelve a este formulario y señala el campo para corregirlo.

Las vistas incluyen previews para Android Studio. Las pruebas cubren los formatos, la confirmación de contraseña, la solicitud de código y el bloqueo de envíos repetidos. Las pruebas de pantalla requieren ejecución en un emulador o dispositivo.
