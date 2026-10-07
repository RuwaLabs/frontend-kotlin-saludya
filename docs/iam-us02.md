# US-02: Registro de credenciales de acceso

Después de verificar su identidad, el paciente registra su correo, contraseña, confirmación y celular. El formulario señala los campos incorrectos y permite corregirlos antes de continuar.

El correo debe tener un formato válido y el celular nueve dígitos numéricos. Las contraseñas deben coincidir y tener al menos ocho caracteres, con letras y números. También se respeta el límite de 72 bytes de BCrypt usado por el backend.

La app envía los datos de identidad y las credenciales a `POST /api/v1/user-accounts`. Si el correo ya está registrado, muestra el error en ese campo. Los errores de DNI y de disponibilidad del servicio se muestran por separado. Durante el registro se bloquean los envíos repetidos; al recibir la confirmación, se borran las contraseñas del estado del formulario.

La verificación será por correo, según el cambio acordado por el equipo. El celular se conserva como dato de contacto y no se utiliza SMS. La pantalla siguiente muestra el correo registrado; el envío y la validación del código siguen pendientes de integración, porque el endpoint actual solo confirma la creación de la cuenta. La app no muestra un código como enviado sin esa confirmación.

Las vistas incluyen previews para Android Studio. La comprobación final ejecutó 43 pruebas unitarias y HTTP, generó el APK Debug y compiló las pruebas de pantalla. Lint terminó sin errores. Las pruebas de pantalla todavía requieren ejecución en un emulador o dispositivo.
