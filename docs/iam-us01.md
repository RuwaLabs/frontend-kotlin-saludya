# US-01 — Verificación de identidad por DNI

La aplicación permite al paciente verificar su identidad antes de crear su cuenta. El formulario solicita DNI, nombres, apellidos y fecha de nacimiento. La verificación se realiza contra el backend de SaludYa; la aplicación no consulta al proveedor oficial directamente ni contiene sus credenciales.

## Comportamiento

| Escenario | Resultado en la aplicación |
| --- | --- |
| Datos coincidentes | Una respuesta `verified: true` habilita **Continuar registro**. |
| DNI inexistente o datos no coincidentes | `422` con `IAM_IDENTITY_MISMATCH` muestra el rechazo y permite corregir los campos. |
| DNI con formato inválido | Se indica que debe contener ocho dígitos numéricos y no se envía ninguna solicitud. |
| Servicio no disponible | Errores de conexión, timeout o respuesta `5xx` mantienen bloqueada la continuación y permiten reintentar. |

La fecha visible utiliza `DD/MM/AAAA`, se valida estrictamente y debe ser anterior al día actual en Lima. El request utiliza una fecha ISO `AAAA-MM-DD`. Los nombres y apellidos son obligatorios y admiten hasta 100 caracteres, según el contrato del backend. El DNI conserva ceros iniciales y no se corrige ni trunca silenciosamente.

Mientras se consulta el servicio, el formulario y la verificación quedan bloqueados. Modificar un dato después de verificarlo elimina el estado de identidad verificada. Las respuestas vacías o incompletas no habilitan el registro. Los datos solo permanecen en memoria y no se registran en logs ni se guardan en preferencias.

## Integración

`POST /api/v1/identity-verifications`, público, sin token de sesión:

```json
{
  "dni": "01234567",
  "name": "Lucía",
  "lastname": "Torres",
  "birthDate": "2000-05-01"
}
```

El ejemplo solo ilustra el formato; no representa una identidad oficial validada.

La respuesta exitosa es `{"verified": true}`. Los errores de dominio incluyen `code` y `message`; la aplicación usa el código y presenta mensajes en español.

La URL del backend se configura con la propiedad Gradle `saludya.apiBaseUrl`, incluyendo `/` al final y sin agregar `/api/v1`:

```properties
# ~/.gradle/gradle.properties, o parámetro -Psaludya.apiBaseUrl=...
saludya.apiBaseUrl=https://backend-del-equipo.example/
```

Sin una configuración local, Debug apunta a `http://10.0.2.2:8080/`, que representa la máquina anfitriona desde el emulador Android. Release utiliza `https://localhost/` como valor sin servidor productivo: el equipo debe indicar su URL HTTPS antes de distribuir la app. HTTP solo se permite en el manifiesto Debug. No se incorpora SMS a esta historia.

`IdentityVerificationRoute` entrega `VerifiedIdentity` mediante `onContinueRegistration` únicamente después de una respuesta positiva. La actividad muestra el resumen de identidad como destino de esta historia; los formularios de creación de cuenta pertenecen a la siguiente historia de registro. Este estado de UI no sustituye la validación del backend al crear la cuenta.

La disponibilidad del registro oficial depende del adaptador de identidad configurado en el backend. Las pruebas HTTP usan un servidor local controlado, sin afirmar una consulta real a RENIEC.

## Validación del código

- `IdentityValidatorTest`: formato del DNI, ceros iniciales, fechas inválidas o futuras y nombres obligatorios.
- `IdentityViewModelTest`: los cuatro escenarios de aceptación, revocación de la verificación y solicitudes duplicadas.
- `RemoteIdentityRepositoryTest`: request real de Retrofit, respuesta positiva, rechazo, indisponibilidad y respuestas incompletas.
- `IdentityScreenTest`: disponibilidad del botón de continuación y recuperación ante indisponibilidad, mediante Compose.

```shell
./gradlew :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
./gradlew :app:connectedDebugAndroidTest
```

La segunda tarea requiere un emulador o dispositivo Android conectado.

### Resultado de la ejecución — 7 de octubre de 2026

Se ejecutaron 20 pruebas unitarias y de contrato HTTP: 5 de validación de datos, 6 del estado de verificación y 9 de integración de Retrofit con un servidor local. Todas finalizaron sin fallos. Se generaron el APK Debug y el APK de pruebas de pantalla.

Las tres pruebas de Compose quedaron compiladas. No se ejecutaron en un dispositivo porque el emulador de esta máquina no completó su inicio. La consulta al registro oficial debe comprobarse con el backend del equipo y su proveedor configurado; los resultados anteriores corresponden al contrato HTTP controlado.
