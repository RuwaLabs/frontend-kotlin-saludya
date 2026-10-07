# US-01: Verificación de identidad por DNI

El paciente ingresa su DNI, nombres, apellidos y fecha de nacimiento antes de continuar con el registro. La app envía los datos al backend de SaludYa y habilita **Continuar registro** cuando recibe la confirmación de identidad.

El DNI debe tener ocho dígitos numéricos. Si el formato es incorrecto, se muestra el error sin enviar la solicitud. Cuando los datos no coinciden, el paciente puede corregirlos. Si el servicio no responde, la continuación queda bloqueada y se permite reintentar.

La fecha se ingresa como `DD/MM/AAAA` y se envía como `AAAA-MM-DD`. Los nombres y apellidos son obligatorios. Si se modifica algún campo después de verificar la identidad, se debe verificar nuevamente.

## Conexión con el backend

La consulta utiliza `POST /api/v1/identity-verifications` con los campos `dni`, `name`, `lastname` y `birthDate`. La respuesta esperada es `{"verified": true}`. El rechazo de identidad corresponde a `422` con el código `IAM_IDENTITY_MISMATCH`.

La URL se define en la propiedad Gradle `saludya.apiBaseUrl`, con `/` al final. En Debug, el valor por defecto es `http://10.0.2.2:8080/` para conectar el emulador con el backend local. Para Release se debe configurar la URL HTTPS del servidor; `https://localhost/` es el valor provisional.

La pantalla entrega los datos verificados a través de `onContinueRegistration`. La creación de la cuenta se integra en la siguiente historia. La consulta al registro oficial se realiza desde el backend.

## Pruebas

Pasaron 20 pruebas de validación, estado de la pantalla y respuestas HTTP. También se generaron el APK Debug y el APK de pruebas, y Lint terminó sin errores.

Las tres pruebas de pantalla quedaron compiladas, pero pendientes de ejecución en un dispositivo porque el emulador no inició. Las pruebas HTTP utilizaron un servidor local de pruebas; queda pendiente comprobar la conexión con el proveedor de identidad del backend.
